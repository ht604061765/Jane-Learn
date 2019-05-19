<!doctype html>
<html lang="zh-cn" xmlns="">
<head>
    <title>用户管理</title>
    <#include "basic.ftl">
</head>

<body>
    <div>
        <form action="/submitUserM" method="post">
        <div class="operation" style="margin-bottom:20px">
            <p id="test">用户管理</p>
            姓名：<input type="text" name="username"></input>
            性别：<input type="text" name="sex"></input>
            电话：<input type="text" name="phone"></input>
            账号：<input type="text" name="account"></input>
            密码：<input type="password" name="password"></input>
            职业：<input type="text" name="job"></input>
            备注：<input type="text" name="remark"></input>
        </div>
            <div class="operation" style="margin-bottom:20px">
                <button id = "addUser">新增</button>
            </div>
        </form>

        <table style="text-align:center;FONT-SIZE: 11pt; WIDTH: 600px; FONT-FAMILY: 宋体; BORDER-COLLAPSE: collapse" borderColor=#3399ff cellSpacing=0 cellPadding=0 align=center border=1>
            <tr>
                <td><b>id</b></td>
                <td><b>username</b></td>
                <td><b>password</b></td>
                <td><b>createTime</b></td>
                <td><b>sex</b></td>
                <td><b>phone</b></td>
                <td><b>job</b></td>
                <td><b>account</b></td>
                <td><b>remark</b></td>
                <td><b>orgId</b></td>
                <td><b>modify</b></td>
                <td><b>delete</b></td>
                <td><b>关联机构</b></td>
            </tr>
            <#list userM as user>
                <tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>${user.password}</td>
                    <td>${user.createTime}</td>
                    <td>${user.sex}</td>
                    <td>${user.phone}</td>
                    <td>${user.job}</td>
                    <td>${user.account}</td>
                    <td>${user.remark}</td>
                    <td>${(user.orgId)!''}</td>
                    <td><button type="button" class="btn btn-danger btn-sm addDevice" onclick="modifyqa('${user.id}')">编辑</button></td>
                    <td><button type="button" class="btn btn-danger btn-sm addDevice" onclick="deleteqa('${user.id}')">删除</button></td>
                    <td><button type="button" class="btn btn-danger btn-sm addDevice" onclick="deleteqa('${user.id}')">关联机构</button></td>
                </tr>
            </#list>
        </table>
    </div>

    <!-- 修改用户模态窗口 -->
    <div id="modelModifyJane" class="modelModifyJane modal fade" tabindex="-1" role="dialog"></div>
<script>
    deleteqa = function(id) {
        var data = "id=" + id;
        $.ajax({
            type: "POST",
            url: "/delUserMan",
            data: data,
            dataType: "json",
            success: function (data) {
            },
            error: function (data) {
                alert(data);
                alert("异步请求失败");
            },
        });
    }
    modifyqa = function(id){
        var paramData = {
            id : id
        }
        $.ajax({
            type: "POST",
            url: "/modelModifyJane",
            data: paramData,
            success: function(data) {
                $(".modelModifyJane").empty();
                $('.modelModifyJane').append(data);
                $('.modelModifyJane').modal('show')
            },
            error: function(request) {
                alert("异步请求失败");
            },
        });
    }
</script>
</body>

</html>