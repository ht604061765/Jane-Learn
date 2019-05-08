<div class="attendanceListPage">
	<div id="toolbar_attendanceList">
		<button type="button" class="btn btn-danger btn-sm" onclick="">
			<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>预留按钮
		</button>
	</div>
	<table id="tb_attendanceList"></table>  
</div>

<!-- 添加用户模态窗口 -->
<div id="attendanceDetailsModel" class="attendanceDetailsModel modal fade" tabindex="-1" role="dialog"></div>

<script type="text/javascript">
attendanceDetails = function(attendanceId){
	$.ajax({
        type: "POST",
        url: "/modelAttendanceDetails",
        data: {
        	attendanceId : attendanceId
        },
        success: function(data) {  
            $(".attendanceDetailsModel").empty();
            $('.attendanceDetailsModel').append(data);
            $('.attendanceDetailsModel').modal('show')
        },
        error: function(request) {  
            alert("异步请求失败");
        },  
    });
}

refreshAttendList = function(){
    var columns = [{
        field: 'personName',
        title: '人员名称',
        align: 'center',
        width: '100px'
    }, {
        field: 'createTime',
        title: '考勤时间',
        align: 'center'
    }, {
        field: 'personIdNo',
        title: '身份证号码',
        align: 'center',
        width: '200px'
    }, {
        title: '是否上传',
        align: 'center',
        width: '100px',
        formatter: function (value, row, index){
        	var text = "";
	        if(row.state == "1"){
	        	text = "已提交";
	        }else if(row.state == "0"){
	        	text = "未提交";
	        }
            return text;
        }
    }, {
        field: 'matchScore',
        title: '考勤匹配度',
        align: 'center',
        width: '200px'
    }, {
        field: 'attendancePlace',
        title: '考勤地点',
        align: 'center',
        valign: 'middle'
    }, {
        title: '操作',
        align: 'center',
        valign: 'middle',
        formatter: function (value, row, index){
        	var text = '<button type="button" class="btn btn-warning btn-xs" onclick="attendanceDetails(\'' + row.id + '\')">详情</button>';
            return text;
        }
    }]
    var pageParam = {}
    createTable('tb_attendanceList','/getAttendanceList','toolbar_attendanceList', 10, 635, columns);
}

$(function () {
	refreshAttendList();
});

</script>
<style>

.attendanceListPage{
	padding-top:70px;
}

</style>