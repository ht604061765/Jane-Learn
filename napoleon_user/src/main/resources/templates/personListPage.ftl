<div class="personListPage">
	<div id="toolbar_personList">
		<button type="button" class="btn btn-danger btn-sm" onclick="waitCode()">
			<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>预留按钮
		</button>
	</div>
	<table id="tb_personList"></table>  
</div>

<!-- 添加用户模态窗口 -->
<div id="personDetailsModel" class="personDetailsModel modal fade" tabindex="-1" role="dialog"></div>
<!-- 采集频率模态窗口 -->
<div id="collectSpeedModel" class="collectSpeedModel modal fade" tabindex="-1" role="dialog"></div>
<script>

waitCode = function(){
	alert("待开发。。。");
}

personDetails = function(idNo){
	$.ajax({
        type: "POST",
        url: "/modelPersonDetails",
        data: {
        	idNo : idNo
        },
        success: function(data) {  
            $(".personDetailsModel").empty();
            $('.personDetailsModel').append(data);
            $('.personDetailsModel').modal('show')
        },
        error: function(request) {  
            alert("异步请求失败");
        },  
    });
}

refreshPersonList = function(){
	var columns = [{
        field: 'name',
        title: '姓名',
        align: 'center'
    }, {
        field: 'createTime',
        title: '采集时间',
        align: 'center'
    }, {
        field: 'idNo',
        title: '身份证',
        align: 'center'
    }, {
        field: 'sex',
        title: '性别',
        align: 'center'
    }, {
        field: 'collectUser',
        title: '采集设备',
        align: 'center'
    }, {
        title: '操作',
        align: 'center',
        valign: 'middle',
        formatter: function (value, row, index){
        	var text = '<button type="button" class="btn btn-warning btn-xs" onclick="personDetails(\'' + row.idNo + '\')">详情</button>';
            return text;
        }
    }]
    var pageParam = {}
    createTable('tb_personList','/getPersonList','toolbar_personList', 10, 600, columns, pageParam);
}

$(function () {
	refreshPersonList();
});
</script>
<style>
.personListPage{
	padding-top:70px;
}
</style>