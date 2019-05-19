<div class="modal-dialog" role="document">
    <div class="modal-content">
	    <div class="modal-header">
	    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">修改用户</h4>
	    </div>
	    <div class="modal-body">
			<form id="addDeviceForm">
				<div class="operation" style="margin-bottom:20px">
					<input type="hidden" name="id" value='${(userOneM.id)!''}'></input>
					姓名：<input type="text" name="username" value='${(userOneM.username)!''}'></input>
					性别：<input type="text" name="sex" value='${(userOneM.sex)!''}'></input>
					电话：<input type="text" name="phone" value='${(userOneM.phone)!''}'></input>
					账号：<input type="text" name="account" value='${(userOneM.account)!''}'></input>
					密码：<input type="password" name="password" value='${(userOneM.password)!''}'></input>
					职业：<input type="text" name="job" value='${(userOneM.job)!''}'></input>
					备注：<input type="text" name="remark" value='${(userOneM.remark)!''}'></input>
				</div>
			</form>
	    </div>
	    <div class="modal-footer">
	      <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
	      <button type="button" class="btn btn-primary" onclick="modifyUserInfo()">提交</button>
		</div>
	</div><!-- /.modal-content -->
</div><!-- /.modal-dialog -->

<script>
	modifyUserInfo = function() {
		var paramData = $("#addDeviceForm").serialize();
		$.ajax({
			type: "POST",
			url: "/modifyUserInfo",
			data: paramData,
			success: function (data) {
				window.location.reload("/userManage");
			},
			error: function (data) {
				alert("异步请求失败");
			},
		});
	}
</script>
<style>
</style>