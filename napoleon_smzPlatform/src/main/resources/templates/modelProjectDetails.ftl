<div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
	    <div class="modal-header">
        	<h4 class="modal-title">项目详情</h4>
	    </div>
	    <div class="modal-body">
	    	<div><Strong>基本信息</Strong></div>
		    <div class="projectDetailBox row" style="margin-left: 15px;">
				<div class="col-md-10 projectDetail">项目名称：${(project.name)!''}</div>

				<div class="col-md-5 projectDetail">平台项目ID：${(project.platformProjectId)!''}</div>
				<div class="col-md-7 projectDetail">平台名称：${(project.platformId)!''}</div>

				<div class="col-md-5 projectDetail">白名单时间：${(project.syncTime)!''}</div>
				<div class="col-md-7 projectDetail">创建时间：${(project.createTime)!''}</div>
			</div>

			<div id="toolbar_projectPersonList">
				<strong>项目人员列表</strong>
			</div>
			<table id="tb_projectPersonList"></table>
	    </div>
	    <div class="modal-footer">
	      <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
		</div>
	</div><!-- /.modal-content -->
</div><!-- /.modal-dialog -->

<script>

//刷新项目人员列表
refreshProjectPersonList = function(){
	var columns = [{
		field: 'platformWorkerName',
		title: '人员名称',
		align: 'center',
	}, {
		field: 'idNo',
		title: '身份证号',
		align: 'center',
	}, {
        field: 'platformWorkerId',
        title: '工人编号',
        align: 'center',
    }, {
        field: 'createTime',
        title: '采集时间',
        align: 'center',
    }, {
        field: 'uploadTime',
        title: '推送时间',
        align: 'center',
    }, {
		field: 'uploaded',
		title: '推送状态',
		align: 'center',
		formatter:function(value, row, index) {
			if(row.uploaded == "1") {
				return "已推送";
			} else if(row.uploaded == "0") {
				return "未推送";
			} else {
				return "";
			}
		}
	},{
		field: 'state',
		title: '入职状态',
		align: 'center',
		formatter:function(value, row, index) {
			if(row.state == "1") {
				return "已入职";
			} else if(row.state == "0") {
				return "未入职";
			} else if(row.state == "2") {
				return "未入职";
			} else {
				return "";
			}
		}
	}]

	var pageParam = {platformProId:"${project.platformProjectId}"}
	createTable('tb_projectPersonList','/getProjectUserList','toolbar_projectPersonList', 10, 550, columns, pageParam);
};

$(document).ready(function(){
	refreshProjectPersonList();
});

</script>
<style>

.projectDetail{
	margin-top: 7px;
}
.projectDetailBox{
	width: 98%;
	height: 100px;
	color: whitesmoke;
	border-radius:11px;
	background-color: #4b5879;
}
.modal-header{

}
.modal-footer{

}

.modal-body{
	height: auto;
}
</style>