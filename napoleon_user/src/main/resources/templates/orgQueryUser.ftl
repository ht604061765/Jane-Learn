<!doctype html>
<html lang="zh-cn" xmlns="">
<head>
	<title>用户管理</title>
	<#include "basic.ftl">
</head>

<body>
<div class="" role="document">
    <div class="">
	    <div class="">
			<table style="text-align:center;FONT-SIZE: 11pt; WIDTH: 600px; FONT-FAMILY: 宋体; BORDER-COLLAPSE: collapse" borderColor=#3399ff cellSpacing=0 cellPadding=0 align=center border=1 id = 'orgList'>
				<tr>
					<td><b>id</b></td>
					<td><b>orgname</b></td>
					<td><b>查询</b></td>
				</tr>
				<#list orgL as org>
					<tr>
						<td>${(org.id)!''}</td>
						<td>${(org.orgname)!''}</td>
						<td><button type="button" class="btn btn-danger btn-sm addDevice" onclick="search('${org.id}')">查询</button></td>
					</tr>
				</#list>
			</table>
			<table style="text-align:center;FONT-SIZE: 11pt; WIDTH: 600px; FONT-FAMILY: 宋体; BORDER-COLLAPSE: collapse" borderColor=#3399ff cellSpacing=0 cellPadding=0 align=center border=1 id="userList">
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
				</tr>
				<#list userM as user>
					<tr>
						<td>${(user.id)!''}</td>
						<td>${(user.username)!''}</td>
						<td>${(user.password)!''}</td>
						<td>${(user.createTime)!''}</td>
						<td>${(user.sex)!''}</td>
						<td>${(user.phone)!''}</td>
						<td>${(user.job)!''}</td>
						<td>${(user.account)!''}</td>
						<td>${(user.remark)!''}</td>
						<td>${(user.orgId)!''}</td>
					</tr>
				</#list>
			</table>
	    </div>
</body>
<script>
	search = function(id) {
		var paramData = {
			id: id
		}
		$.ajax({
			type: "POST",
			url: "/orgQueryUser",
			data: paramData,
			success: function (data) {
				$("#userList").empty();
				$('#userList').append(data)
				$('#orgList').hide()
			},
			error: function (data) {
				alert(id);
				alert("异步请求失败");
			},
		});
	}
</script>
<style>
</style>