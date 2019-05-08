<div class="projectList">
    <div id="toolbar_projectList">
        <button type="button" class="btn btn-danger btn-sm" onclick="waitCode()">
            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>预留按钮
        </button>
    </div>
    <table id="tb_projectList"></table>
</div>

<!-- 添加项目模态窗口 -->
<div id="projectDetailsModel" class="projectDetailsModel modal fade" tabindex="-1" role="dialog"></div>


<script>
waitCode = function(){
alert("待开发。。。");
}

projectDetails = function(platformProjectId){
    $.ajax({
        type: "POST",
        url: "/modelProjectDetails",
        data: {
            platformProjectId : platformProjectId
        },
        success: function(data) {
            $(".projectDetailsModel").empty();
            $('.projectDetailsModel').append(data);
            $('.projectDetailsModel').modal('show')
        },
        error: function(request) {
            alert("异步请求失败");
        },
    });
}

//刷新项目列表
refreshProjectList = function(){
    var columns = [{
        field: 'name',
        title: '项目名称',
        align: 'left',
    }, {
        field: 'platformId',
        title: '平台名称',
        align: 'center',
    }, {
        field: 'platformProjectId',
        title: '平台项目ID',
        align: 'center',
    },{
        field: 'syncTime',
        title: '白名单时间',
        align: 'center',
    },{
        title: '操作',
        align: 'center',
        valign: 'middle',
        formatter:function(value, row, index) {
            var text = '<button type="button" class="btn btn-warning btn-xs" onclick="projectDetails(\'' + row.platformProjectId + '\')">详情</button>';
            return text;
        }
    }]

    var pageParam = {}
    createTable('tb_projectList','/getProjectList2','toolbar_projectList', 10, 600, columns, pageParam);
}

$(document).ready(function(){
    refreshProjectList();
});

</script>
<style>
.projectList {
    padding-top:70px;
}
</style>