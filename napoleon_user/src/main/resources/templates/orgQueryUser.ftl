<div class="modal-dialog" role="document">
    <div class="modal-content">
	    <div class="modal-header">
	    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">关联机构</h4>
	    </div>
	    <div class="modal-body">
			<table style="text-align:center;FONT-SIZE: 11pt; WIDTH: 600px; FONT-FAMILY: 宋体; BORDER-COLLAPSE: collapse" borderColor=#3399ff cellSpacing=0 cellPadding=0 align=center border=1>
				<tr>
					<td><b>id</b></td>
					<td><b>orgname</b></td>
					<td><b>查询</b></td>
				</tr>
				<#list orgM as org>
					<tr>
						<td>${(org.id)!''}</td>
						<td>${(org.orgname)!''}</td>
						<td><button type="button" class="btn btn-danger btn-sm addDevice" onclick="confirm('${org.id}')">查询</button></td>
					</tr>
				</#list>
			</table>
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

<script>
	confirm = function(orgId,userId) {
		var paramData = {
			orgId:orgId,
			id:userId

		}
		$.ajax({
			type: "POST",
			url: "/connectOrg",
			data: paramData,
			success: function (data) {
				window.location.reload("/userManage");
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