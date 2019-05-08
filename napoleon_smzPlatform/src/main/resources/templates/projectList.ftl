<!doctype html>
<html lang="en">
<head>
    <title>项目列表</title>
    <#include "basic.ftl">
</head>
	
<body>
<div class="projectList row" style="">
    <div class="col-md-10">
        <div id="toolbar_projectList">
            <strong>项目列表</strong>
        </div>
       <table id="projectList"></table>
    </div>
</div>

</body>
<script type="text/javascript">

//刷新平台列表
refreshAttendList = function(){
    var columns = [{
        field: 'name',
        title: '项目名称',
        align: 'left',
        width: '100px'
    }, {
        field: 'areaId',
        title: '所属平台',
        align: 'left',
        width: '200px'
    }, {
        field: 'platformId',
        title: '平台项目ID',
        align: 'left',
        width: '100px'
    },{
        field: 'syncTime',
        title: '最后同步时间',
        align: 'left',
        width: '100px'
    },{
        field: '',
        title: '操作',
        align: 'left',
        valign: 'middle',
        formatter:function(value, row, index) {
            return '<a type="button" class="btn btn-success" href="/projectUserList?platformProjectId='+ row.platformProjectId +'">项目人员</a>'
        }
    }]

    var pageParam = {
        platformId:'${(platformId)}'
    }
    createTable('projectList','/getProjectList','toolbar_projectList', 15, 900, columns, pageParam);
}

$(document).ready(function(){
    refreshAttendList();
});

</script>
<style>
.projectList {
	height: 32px;
	color: #5e5e5e;
    background-color: transparent;
}

</style>
</html>