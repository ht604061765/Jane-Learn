<div class="socketMapPage">
    <div class="dateFilter" style="padding: 2px; float:left;">
        <strong>过滤条件：</strong>
        <span>考勤日期从</span><input id="startDate" class="" type="date"/>
        <span>到</span><input id="endDate" class="" type="date"/>
    </div>
    <button type="button" class="btn btn-danger btn-sm" onclick="filterAttends()">过滤</button>

    <div id="l-map" style="padding-top: 72px;"></div>
</div>


<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=XHjHhUr8aKFwIb7cCDmF5VbgHAXh2ftk"></script>
<!-- 加载百度地图样式信息窗口 -->
<script type="text/javascript" src="http://api.map.baidu.com/library/SearchInfoWindow/1.5/src/SearchInfoWindow_min.js"></script>
<script type="text/javascript">
    // 初始化地图实例
    var map = new BMap.Map("l-map");

    connectScoket = function () {
        var socket;
        if(typeof(WebSocket) == "undefined") {
            alert("您的浏览器不支持WebSocket");
        }else{
            //建立连接
            socket = new WebSocket("ws://localhost:10100/websocket/${cid}");
            //打开事件
            socket.onopen = function() {
                console.log("Socket 已打开");
                socket.send("这是来自客户端11的消息");
            };
            //获得消息事件
            socket.onmessage = function(msg) {
                //发现消息进入开始处理前端触发逻辑
                if(msg.data != undefined){
                    var attendance = JSON.parse(msg.data)
                    var newPoint = new BMap.Point(attendance.longitude, attendance.latitude);
                    var attendInfo = {};
                    attendInfo.name = attendance.personName;
                    attendInfo.idNo = attendance.personIdNo;
                    attendInfo.time = attendance.createTime;
                    addMarker(newPoint, attendInfo, 0);
                }
            };
            //关闭事件
            socket.onclose = function() {
                // alert("Socket已关闭");
            };
            //发生了错误事件
            socket.onerror = function() {
                // alert("Socket发生了错误");
                //此时可以尝试刷新页面
            }
        }
    }

    /**
     * 创建地图点
     */
    function addMarker(point, attendInfo, index){
        var myIcon = new BMap.Icon("http://api.map.baidu.com/img/markers.png", new BMap.Size(23, 25), {
            // 指定定位位置。
            // 当标注显示在地图上时，其所指向的地理位置距离图标左上
            // 角各偏移10像素和25像素。您可以看到在本例中该位置即是
            // 图标中央下端的尖角位置。
            anchor: new BMap.Size(10, 25),
            // 设置图片偏移。
            // 当您需要从一幅较大的图片中截取某部分作为标注图标时，您
            // 需要指定大图的偏移位置，此做法与css sprites技术类似。
            imageOffset: new BMap.Size(0, 0 - index * 25)   // 设置图片偏移
        });

        // 创建标注对象并添加到地图
        var marker = new BMap.Marker(point, {icon: myIcon});
        map.addOverlay(marker);

        marker.addEventListener("click", function(){
            var tipMsg = "考勤人员：" + attendInfo.name + "</br>考勤时间："+ attendInfo.time +"</br>身份证号码：" + attendInfo.idNo;
            var infoWindow = new BMap.InfoWindow(tipMsg, tipOpts);  // 创建信息窗口对象
            map.openInfoWindow(infoWindow, point);      // 打开信息窗口
        });
    }

    var tipOpts = {
        width : 250,     // 信息窗口宽度
        height: 100,     // 信息窗口高度
        title : "考勤详情"  // 信息窗口标题
    };

    /**
     * 百度地图API功能
     */
    initBaiduMap = function (longitude, latitude) {
        debugger;
        // 如果初始化中心点为空的话，设置默认中心点，珠海市政府(113.583533, 22.276826)
        if(longitude == undefined || latitude == undefined){
            longitude = 113.583533;
            latitude = 22.276826;
        }
        // 先清除所有点
        map.clearOverlays();
        // 创建中心点
        var point = new BMap.Point(longitude, latitude);
        map.centerAndZoom(point, 15);
        map.enableScrollWheelZoom();
        // 点亮中心点
        addMarker(point,0);
        // 启用滚轮缩放
        // map.enableScrollWheelZoom();
    }

    filterAttends = function(){
        var startDate = $('#startDate').val();
        var endDate = $('#endDate').val();
        fetchAttendanceData(startDate, endDate, 0);
    };

    fetchAttendanceData = function (startDate, endDate, proId) {
        // 组装请求参数
        var postData = {};
        postData.startDate =  startDate;
        postData.endDate = endDate;
        postData.proId = proId;
        $.ajax({
            type: "POST",
            url: "/attendanceList",
            data: postData,
            success: function(data) {
                if(data.status == '0'){
                    alert(data.message);
                    return;
                }
                if(data.data != null){
                    var rtn = data.data;
                    for(var i = 0, n = rtn.length ; i < n; i++){
                        if(i == 0){
                            initBaiduMap(rtn[i].longitude, rtn[i].latitude)
                        }
                        var newPoint = new BMap.Point(rtn[i].longitude, rtn[i].latitude);
                        var attendInfo = {};
                        attendInfo.name = rtn[i].personName;
                        attendInfo.idNo = rtn[i].personIdNo;
                        attendInfo.time = rtn[i].createTime;
                        // 缓慢显示考勤点
                        setTimeout(addMarker(newPoint, attendInfo, 0), 1000);
                    }
                }else{
                    initBaiduMap();
                }

            },
            error: function() {
                alert("请求提交失败");
            },
        });
    }


    $(function () {
        connectScoket();
        // 初始化数据和地图
        var now = new Date();
        // 初始化考勤日期
        document.getElementById('startDate').valueAsDate = now;
        document.getElementById('endDate').valueAsDate = now;
        var today = now.getFullYear() + "-" + ((now.getMonth()+1) < 10 ? "0":"") + (now.getMonth() + 1) + "-" + (now.getDate() < 10 ? "0" : "") + now.getDate();
        fetchAttendanceData(today, today, 0);
    });

</script>

<link rel="stylesheet" href="http://api.map.baidu.com/library/SearchInfoWindow/1.5/src/SearchInfoWindow_min.css" />
<style type="text/css">
    body, html{
        width: 100%;
        height: 100%;
        margin: 0;
        font-family:"微软雅黑";
        font-size: 14px;
    }
    #l-map {
        width: 100%;
        height: 93%;
        overflow: hidden;
    }

    li{
        line-height: 28px;
    }
    .map_popup .popup_main {
        background: #fff;
        border: 1px solid #8BA4D8;
        height: 100%;
        overflow: hidden;
        position: absolute;
        width: 100%;
        z-index: 2;
    }
    .map_popup .title {
        background: url("http://map.baidu.com/img/popup_title.gif") repeat scroll 0 0 transparent;
        color: #6688CC;
        font-weight: bold;
        height: 24px;
        line-height: 25px;
        padding-left: 7px;
    }
    .map_popup button {
        background: url("http://map.baidu.com/img/popup_close.gif") no-repeat scroll 0 0 transparent;
        cursor: pointer;
        height: 12px;
        position: absolute;
        right: 4px;
        top: 6px;
        width: 12px;
    }
    .socketMapPage{
        padding-top: 80px;
        height: 100%;
    }
</style>