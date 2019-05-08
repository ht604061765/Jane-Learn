<div class="modal-dialog" role="document">
    <div class="modal-content">
	    <div class="modal-header">
	    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">添加用户</h4>
	    </div>
	    <div class="modal-body">
			<form class="regForm">
				<div class="form-group">
					<div class="input-group regItem">
						<span class="input-group-addon"><span class="glyphicon glyphicon-education"></span></span> 
						<input name="name" type="text" class="form-control regName" placeholder="输入用户昵称" required>
					</div>
					<div class="input-group regItem">
						<span class="input-group-addon"><span class="glyphicon glyphicon-user"></span></span> 
						<input name="account" type="text" class="form-control regAccount" placeholder="输入用户账号" required>
					</div>
					<div class="input-group regItem">
						<span class="input-group-addon"><span class="glyphicon glyphicon-lock"></span></span> 
						<input name="pwd" type="password" class="form-control regPwd" placeholder="输入用户密码" required>
					</div>
				</div>
			</form>
	    </div>
	    <div class="modal-footer">
	      <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
	      <button type="button" class="btn btn-primary" onclick="regSubmit()">提交</button>
		</div>
	</div><!-- /.modal-content -->
</div><!-- /.modal-dialog -->

<script>
regSubmit = function(){
	var regName = $('.regName').val();
	var regAccount = $('.regAccount').val();
	var regPwd = $('.regPwd').val();
	if(regName.length == 0){
		alert("请输入用户昵称");
		return;
	}else if(regAccount.length == 0){
		alert("请输入用户账号");
        return;
	}else if(regPwd.length == 0){
		alert("请输入用户密码");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/regSubmit",
        data: $(".regForm").serialize(),  
        success: function(data) {  
            if(data.status == '0'){
                alert(data.message);
                return;
            }else{
            	$('.addUserModel').modal('hide');
                $('#tb_userList').bootstrapTable('refresh', null);// 注意，这个地方可以加参数，可以控制刷新到哪个页
            }
            
        },
        error: function(request) {  
            alert("请求提交失败");  
        },  
    });
}
</script>
<style>
.regItem{
	margin: 20px;
}
</style>