<div class="modal-dialog" role="document">
    <div class="modal-content">
	    <div class="modal-header">
	    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">关联机构</h4>
	    </div>
	    <div class="modal-body">
			userId: <input type="text" name="id" value="${userOneM.id}"></input>
			<table style="text-align:center;FONT-SIZE: 11pt; WIDTH: 600px; FONT-FAMILY: 宋体; BORDER-COLLAPSE: collapse" borderColor=#3399ff cellSpacing=0 cellPadding=0 align=center border=1>
				<tr>
					<td><b>id</b></td>
					<td><b>orgname</b></td>
					<td><b>duty</b></td>
					<td><b>createTime</b></td>
					<td><b>remark</b></td>
					<td><b>pid</b></td>
					<td><b>确定</b></td>
				</tr>
				<#list orgM as org>
					<tr>
						<td>${(org.id)!''}</td>
						<td>${(org.orgname)!''}</td>
						<td>${(org.duty)!''}</td>
						<td>${(org.createTime)!''}</td>
						<td>${(org.remark)!''}</td>
						<td>${(org.pid)!''}</td>
						<td><button type="button" class="btn btn-danger btn-sm addDevice" onclick="confirm('${org.id}','${userOneM.id}')">关联机构</button></td>
					</tr>
				</#list>
			</table>
	    </div>
<#--	    <div class="modal-footer">-->
<#--	      <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>-->
<#--	      <button type="button" class="btn btn-primary" onclick="modifyUserInfo()">提交</button>-->
<#--		</div>-->
	</div><!-- /.modal-content -->
</div><!-- /.modal-dialog -->

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