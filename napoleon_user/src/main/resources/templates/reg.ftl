<!doctype html>
<html lang="zh">
<head>
    <title>账户注册</title>
    <#include "basic.ftl">
</head>

<body>
	<div>
		<div class="login_reg_box">
			<span>注册平台：</span><strong>雄安实名制平台</strong>
			<form class="regForm" action="/api/regSubmit"  method="post">
				<input name="platformId" type="text" value="f70bc5e81da14df0b8399b78345bf94a" style="display:none;"></input>
				<input name="platformName" type="text" value="雄安实名制平台" style="display:none;"></input>
			
				<div class="form-group">
					<div class="input-group regItem">
						<span class="input-group-addon" style="width: 57px;"><span class="glyphicon glyphicon-education"></span></span> 
						<input name="name" type="text" class="form-control regName" placeholder="请输入用户名称" data-required>
					</div>
					<div class="input-group regItem">
						<span class="input-group-addon" style="width: 57px;"><span class="glyphicon glyphicon-user"></span></span> 
						<input name="account" type="text" class="form-control regAccount" placeholder="请输入注册账号" data-required>
					</div>
					<div class="input-group regItem" >
						<span class="input-group-addon" style="width: 57px;"><span class="glyphicon glyphicon-lock"></span></span> 
						<input name="pwd" type="password" class="form-control regPwd" placeholder="请输入注册密码" data-required>
					</div>
					<div class="input-group regItem">
						<span class="input-group-addon" style="width: 57px;"><span class="glyphicon glyphicon-lock"></span></span> 
						<input name="phone" type="password" class="form-control regPhone" placeholder="联系人手机号码" data-required>
					</div>
				</div>
				<div class="regButton">
					<input type="submit" value="注册" />
				</div>
			</form>

		</div>

	</div>

</body>
<script type="text/javascript">
	//文档加载完事件
	$(function($) {
		
	});
</script>
<style>

</style>
</html>