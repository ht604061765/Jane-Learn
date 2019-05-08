<div class="modal-dialog" role="document">
    <div class="modal-content">
	    <div class="modal-header">
	    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">添加设备</h4>
	    </div>
	    <div class="modal-body">
			<form id="addDeviceForm">
				<div class="form-group">
					<div class="input-group regItem">
						<span class="input-group-addon"><span class="glyphicon glyphicon-education"></span></span> 
						<input name="name" type="text" class="form-control deviceName" placeholder="输入设备简称" required>
					</div>
					<div class="input-group regItem">
						<span class="input-group-addon"><span class="glyphicon glyphicon-user"></span></span> 
						<input name="account" type="text" class="form-control deviceAccount" placeholder="输入设备账号" required>
					</div>
					<div class="input-group regItem">
						<span class="input-group-addon"><span class="glyphicon glyphicon-lock"></span></span> 
						<input name="pwd" type="password" class="form-control devicePwd" placeholder="输入设备密码" required>
					</div>
				</div>
			</form>
	    </div>
	    <div class="modal-footer">
	      <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
	      <button type="button" class="btn btn-primary" onclick="addDeviceSubmit()">提交</button>
		</div>
	</div><!-- /.modal-content -->
</div><!-- /.modal-dialog -->

<script>
addDeviceSubmit = function(){
	var deviceName = $('.deviceName').val();
	var deviceAccount = $('.deviceAccount').val();
	var devicePwd = $('.devicePwd').val();
	if(deviceName.length == 0){
		alert("请输入设备名称");
		return;
	}else if(deviceAccount.length == 0){
		alert("请输入设备账号");
        return;
	}else if(devicePwd.length == 0){
		alert("请输入设备密码");
        return;
    }
	if(deviceName.length > 512){
		alert("设备简称超出最大长度，请检查重试");
        return;
	}
    $.ajax({
        type: "POST",
        url: "/addDeviceSubmit",
        data: $("#addDeviceForm").serialize(),  
        success: function(data) {
            if(data.status == '0'){
                alert(data.message);
                return;
            }else{
            	$('#addDeviceModel').modal('hide');
            	$('#tb_deviceList').bootstrapTable('refresh', null);// 注意，这个地方可以加参数，可以控制刷新到哪个页
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