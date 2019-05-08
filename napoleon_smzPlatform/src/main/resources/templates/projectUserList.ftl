<!doctype html>
<html lang="en">
<head>
    <title>项目人员列表</title>
    <#include "basic.ftl">
</head>
	
<body>
<div class="projectUserList row" style="">
    <div class="col-md-10">
        <div id="toolbar_projectUserList">
            <strong>项目人员列表</strong>
        </div>
       <table id="projectUserList"></table>
    </div>
</div>

<!--模态框-->
<div class="modal fade bs-example-modal-lg" id="personDetail" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="gridSystemModalLabel">人员信息详情</h4>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-xs-6" style="font-size: 16px">
                        <p><span>人员姓名：</span><span id="workerName"></span></p>
                        <p><span>身份证号：</span><span id="idNo"></span></p>
                        <p><span>性别：</span><span id="sex"></span></p>
                        <p><span>民族：</span><span id="nation"></span></p>
                        <p><span>生日：</span><span id="birthday"></span></p>
                        <p><span>地址：</span><span id="address"></span></p>
                        <p><span>签发机关：</span><span id="provide"></span></p>
                        <p><span>有效期：</span><span id="period"></span></p>
                        <p><span>采集设备：</span><span id="collectDevice"></span></p>
                    </div>
                    <div class="col-xs-6">
                        <img id="idNoImage" style="width: 150px;">
                        <img id="collectImage" style="width: 150px;margin-left: 60px">
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" data-dismiss="modal">确定</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>

</body>
<script type="text/javascript">

//刷新平台列表
refreshAttendList = function(){
    var columns = [{
        field: 'platformWorkerName',
        title: '人员名称',
        align: 'left',
        width: '100px'
    }, {
        field: 'idNo',
        title: '身份证号',
        align: 'left',
        width: '200px'
    }, {
        field: 'uploaded',
        title: '推送状态',
        align: 'left',
        width: '100px',
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
        align: 'left',
        width: '100px',
        formatter:function(value, row, index) {
            if(row.state == "1") {
                return "已入职";
            } else if(row.state == "0") {
                return "未入职";
            } else {
                return "";
            }
        }
    },{
        field: '',
        title: '操作',
        align: 'left',
        valign: 'middle',
        formatter:function(value, row, index) {
            return '<button class="btn btn-success" onclick="workerDetail(\''+ row.idNo +'\')">详情</button>'
        }
    }]

    var pageParam = {
        platformProId:'${(platformProjectId)}'
    }
    createTable('projectUserList','/getProjectUserList','toolbar_projectUserList', 15, 900, columns, pageParam);
}

$(document).ready(function(){
    refreshAttendList();
});

function workerDetail(idNo) {
    $.ajax({
       type:"post",
       url:"/personInfoDetail",
       data:{
           idNo:idNo
       },
        success:function(result) {
            $("#workerName").text(result.name);
            $("#idNo").text(result.idNo);
            $("#sex").text(result.sex);
            $("#nation").text(result.nation);
            $("#birthday").text(result.birthday);
            $("#address").text(result.address);
            $("#provide").text(result.provide);
            $("#period").text(result.period);
            $("#collectDevice").text(result.collectDevice);
            $("#idNoImage").attr("src", "/idNoPhoto?idNo=" + result.idNo);
            $("#collectImage").attr("src", "/collectImage?idNo=" + result.idNo);
            $('#personDetail').modal('show');
        }
    });
}

</script>
<style>
.projectUserList {
	height: 32px;
	color: #5e5e5e;
    background-color: transparent;
}

</style>
</html>