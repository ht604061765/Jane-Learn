<!doctype html>
<html lang="en">
<head>
    <title>平台列表</title>
    <#include "basic.ftl">
</head>
	
<body>
<div class="platformList row" style="">
    <div class="col-md-10">
        <div id="toolbar_platformList">
            <strong>平台列表</strong>
        </div>
       <table id="platformList"></table>
    </div>
</div>

</body>
<script type="text/javascript">

//刷新平台列表
refreshAttendList = function(){
    var columns = [{
        field: 'name',
        title: '平台名称',
        align: 'left',
        width: '100px'
    }, {
        field: 'socketServer',
        title: '平台地址',
        align: 'left',
        width: '200px'
    }, {
        field: 'socketPort',
        title: '平台端口',
        align: 'left',
        width: '100px'
    },{
        field: '',
        title: '操作',
        align: 'left',
        valign: 'middle',
        formatter:function(value, row, index) {
            return '<a type="button" class="btn btn-success" href="/projectList?platformId='+ row.id +'">项目列表</a>'
        }
    }]
    
    createTable('platformList','/getPlatformList','toolbar_platformList', 10, 900, columns);
}

$(document).ready(function(){
    refreshAttendList();
});

</script>
<style>
.platformList {
	height: 32px;
	color: #5e5e5e;
    background-color: transparent;
}

</style>
</html>