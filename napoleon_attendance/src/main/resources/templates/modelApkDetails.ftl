<div class="modal-dialog" role="document">
    <div class="modal-content">
	    <div class="modal-header">
		    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title">APK详细</h4>
	    </div>
	    <div class="modal-body">
			<form id="apkDetailForm">
				<div class="form-group">
				    <input type="hidden" name="id" value="${apk.id!''}"/>
					<div class="input-group regItem">
						<span>版本号：</span> 
						<input name="version" type="text" class="form-control apkVersion" value="${apk.version!''}">
					</div>
					<div class="input-group regItem">
						<span>版本描述：</span> 
						<input name="summary" type="text" class="form-control apkSummary" value="${apk.summary!''}">
					</div>
					<div class="input-group regItem">
						<span>是否启用：</span>
						<input name="state" class="form-control apkState" type="checkbox" >
						<span>(同一时刻，有且仅有一个包为有效状态)</span>
					</div>
				</div>
			</form>
	    </div>
	    <div class="modal-footer">
	      <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
	      <button type="button" class="btn btn-primary" onclick="editApkSubmit()">提交</button>
		</div>
	</div><!-- /.modal-content -->
</div><!-- /.modal-dialog -->

<script>
editApkSubmit = function(){
	debugger
	var apkVersion = $('.apkVersion').val();
	var apkSummary = $('.apkSummary').val();
	if(apkVersion.length == 0){
		alert("请输入版本号");
		return;
	}else if(apkSummary.length == 0){
		alert("请输入版本描述");
        return;
	}
    $.ajax({
        type: "POST",
        url: "/editApkSubmit",
        data: $("#apkDetailForm").serialize(),  
        success: function(data) {
            if(data.status == '0'){
                alert(data.message);
                return;
            }else{
            	$('#apkDetailsModel').modal('hide');
            	$('#tb_apkList').bootstrapTable('refresh', null);// 注意，这个地方可以加参数，可以控制刷新到哪个页
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