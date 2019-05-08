<div class="deviceListPage">
	<div id="toolbar_deviceList">
		<button type="button" class="btn btn-danger btn-sm addDevice" onclick="addDevice()">
			<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>添加设备
		</button>
	</div>
	<table id="tb_deviceList"></table>  
</div>

<!-- 添加用户模态窗口 -->
<div id="addDeviceModel" class="addDeviceModel modal fade" tabindex="-1" role="dialog"></div>
<script>

addDevice = function(){
	$.ajax({
        type: "POST",
        url: "/modelAddDevice",
        data: {},
        success: function(data) {  
            $(".addDeviceModel").empty();
            $('.addDeviceModel').append(data);
            $('.addDeviceModel').modal('show')
        },
        error: function(request) {  
            alert("异步请求失败");
        },  
    });
}

refreshDeviceList = function(){
	var columns = [{
        field: 'name',
        title: '设备名称',
        align: 'left'
    }, {
        field: 'deviceNo',
        title: '设备编号',
        align: 'center'
    }, {
        field: 'account',
        title: '登录账号',
        align: 'center'
    }, {
        field: 'platformProName',
        title: '所属项目',
        align: 'left'
    }, {
        title: '设备状态',
        align: 'center',
        formatter: function (value, row, index){ // 单元格格式化函数
            var text = "";
	        if(row.state == "1"){
	        	text = "<span style='color: green;'>启用</span>";
	        }else if(row.state == "0"){
	        	text = "<span style='color: red;'>禁用</span>";
	        }else if(row.state == "2"){
	        	text = "<span style='color: red;'>设备换绑</span>";
	        }
            return text;
        }
    }]
    var pageParam = {}
    createTable('tb_deviceList','/getDeviceList','toolbar_deviceList', 10, 600, columns, pageParam);
}

changeDeviceState = function(state, deviceId){
	var paramData = {};
	paramData.state = state;
	paramData.deviceId = deviceId;
	$.ajax({
        type: "POST",
        url: "/changeDeviceState",
        data: paramData,
        success: function(data) {
           if(data.status == 1){
        	 $('#tb_deviceList').bootstrapTable('refresh', null);// 注意，这个地方可以加参数，可以控制刷新到哪个页
           }else if(data.status == 0){
        	   alert(data.message);
           }
        },
        error: function(request) {  
            alert("异步请求失败");
        },  
    });
}

$(function () {
	refreshDeviceList();
});
</script>
<style>
.deviceListPage{
	padding-top:70px;
}
</style>