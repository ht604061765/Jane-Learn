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
                <td><b>modify</b></td>
                <td><b>delete</b></td>
            </tr>
            <#list uList as userM>
                <tr>
                    <td>${userM.id}</td>
                    <td>${userM.username}</td>
                    <td>${userM.password}</td>
                    <td>${userM.createTime}</td>
                    <td>${userM.sex}</td>
                    <td>${userM.phone}</td>
                    <td>${userM.job}</td>
                    <td>${userM.account}</td>
                    <td>${userM.remark}</td>
                    <td><button type="button" class="btn btn-danger btn-sm addDevice" onclick="modifyqa('${userM.id}')">编辑</button></td>
                    <td><button type="button" class="btn btn-danger btn-sm addDevice" onclick="deleteqa('${userM.id}')">删除</button></td>
                </tr>
            </#list>
        </table>

            <div class="operation" style="margin-bottom:20px">
                <p id="test">修改用户</p>
                姓名：<input type="text" name="username" value='${(userOneM.username)!''}'></input>
                性别：<input type="text" name="sex" value='${(userOneM.sex)!''}'></input>
                电话：<input type="text" name="phone" value='${(userOneM.phone)!''}'></input>
                账号：<input type="text" name="account" value='${(userOneM.account)!''}'></input>
                密码：<input type="password" name="password" value='${(userOneM.password)!''}'></input>
                职业：<input type="text" name="job" value='${(userOneM.job)!''}'></input>
                备注：<input type="text" name="remark" value='${(userOneM.remark)!''}'></input>
            </div>
            <div class="operation" style="margin-bottom:20px">
                <button id = "addUser">修改</button>
            </div>

    </div>

<script>
    deleteqa = function(id) {
        var data = "id=" + id;
        $.ajax({
            type: "POST",
            url: "/delUserMan",
            data: data,
            dataType: "json",
            success: function (data) {
                alert(id)
            },
            error: function (request) {
                alert(id)
                alert("异步请求失败");
            },
        });
    }

    modifyqa = function(id) {
        var data = "id=" + id;
        $.ajax({
            type: "POST",
            url: "/queryOneUserMan",
            data: data,
            dataType: "json",
            success: function (data) {
                alert(id)
            },
            error: function (request) {
                alert(id)
                alert("异步请求失败");
            },
        });
    }
</script>
</body>

</html>