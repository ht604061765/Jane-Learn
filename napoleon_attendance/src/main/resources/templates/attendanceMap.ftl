<!doctype html>
<html lang="zh">
<head>
    <title>地图展示</title>
    <#include "basic.ftl">
    <style type="text/css">
        body,
        html,
        #allmap {
            width: 100%;
            height: 100%;
            overflow: hidden;
            margin: 0;
            font-family: "微软雅黑";
        }
    </style>
    <title>地图展示</title>
</head>

<body>
<div id="allmap"></div>
<script type="text/javascript">
    var map = new BMap.Map("allmap", {
        enableMapClick: false
    });
    map.centerAndZoom(new BMap.Point(113.55519, 22.149101), 20); // 地图中心点和缩放比例
    map.setCurrentCity("珠海市");
    map.enableScrollWheelZoom(true);

    var project_icon = new BMap.Icon("../static/images/project.png", new BMap.Size(46, 46));
    var people_icon = new BMap.Icon("../static/images/people.png", new BMap.Size(22, 22));

    // 创建标注函数
    function set_marker(data, icon) {
        var point = new BMap.Point(data.longitude, data.latitude);
        var marker = new BMap.Marker(point, {
            icon: icon
        });
        var opts; // 创建标注
        var div;
        if(icon == project_icon) { // 项目点
            opts = {
                width: 350,
                height: 200,
                title: "",
            };
            div = "<span>" + "项目名称：" + data.projectName + "</span>"
        } else { // 人员点
            opts = {
                width: 350,
                height: 200,
                title: "",
            };
            div = "<span>" + "项目名称：横琴总部大厦" + "</span>" + "<br>"
                + "<span>" + "姓名：" + data.personName + "</span>" + "<br>"
                + "<span>" + "身份证号：" + data.personIdNo + "</span>" + "<br>"
                + "<span>" + "考勤时间：" + data.createTime + "</span>";
        }
        var infoWindow = new BMap.InfoWindow(div, opts);

        marker.addEventListener("onmouseover", function () {
            map.openInfoWindow(infoWindow, point); //开启信息窗口
        });
        if (icon == project_icon) {
            marker.disableMassClear();
            return marker;
        } else {
            return marker;
        }
    }
    // 自动显示考勤标点
    map.addEventListener("zoomend", function () {
        for(var i = 0; i < markers_people.length; i++) {
            map.addOverlay(markers_people[i]);
        }
    })

    var markers_people = [];
    var markers_project = [];

    $.ajax({
        type: "POST",
        url: "/attendanceList",
        data: "",
        success: function(data) {
            // 清除之前的考勤地点和数据
            map.clearOverlays();
            people_marker = []
            data.forEach(function(people){
                var p = set_marker(people,people_icon);
                map.addOverlay(p);
                markers_people.push(p);
            })
        }
    });

    // 初始化项目数据
    var project = {projectName:"横琴总部大厦", longitude:"113.55519", latitude:"22.149101"};
    markers_project.push(set_marker(project, project_icon));

    new BMapLib.MarkerClusterer(map, {
        markers: markers_project
    });
    // 全部人员考勤数据
    setInterval(function(){
        $.ajax({
            type: "POST",
            url: "/attendanceList",
            data: "",
            success: function(data) {
                // 清除之前的考勤地点和数据
                map.clearOverlays();
                people_marker = []
                data.forEach(function(people){
                    var p = set_marker(people,people_icon)
                    map.addOverlay(p);
                    markers_people.push(p);
                })
            }
        });
    }, 2000);
</script>
</body>

</html>