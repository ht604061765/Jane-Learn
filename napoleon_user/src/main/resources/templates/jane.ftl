<!doctype html>
<html lang="zh-cn" xmlns="">
<head>
    <title>用户登陆</title>
    <#include "basic.ftl">
</head>

<body>
    <div>
        <form action="/submitTest" method="post">
        <p>username: <input type="text" name="username" /></p>
        <p>password: <input type="password" name="password" /></p>
        <input type="submit" value="Submit" />
        </form>

        <table style="text-align:center;FONT-SIZE: 11pt; WIDTH: 600px; FONT-FAMILY: 宋体; BORDER-COLLAPSE: collapse" borderColor=#3399ff cellSpacing=0 cellPadding=0 align=center border=1>
            <tr>
                <td><b>id</b></td>
                <td><b>username</b></td>
                <td><b>password</b></td>
                <td><b>createTime</b></td>
                <td>delete</td>
            </tr>
            <#list uList as jane>
                <tr>
                    <td>${jane.id}</td>
                    <td>${jane.username}</td>
                    <td>${jane.password}</td>
                    <td>${jane.createTime}</td>
                    <td><button type="button" class="btn btn-danger btn-sm addDevice" onclick="deleteqa('${jane.id}')">删除</button></td>
                </tr>
            </#list>
        </table>
    </div>

<script>
deleteqa = function(id){
    $.ajax({
        type: "POST",
        url: "/deleData",
        data: id,
        success: function(data) {
            alert(id)
        },
        error: function(request) {
            alert(id)
            alert("异步请求失败");
        },
    });
}
</script>
</body>

</html>