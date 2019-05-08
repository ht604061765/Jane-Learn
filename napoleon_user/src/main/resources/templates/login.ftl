<!doctype html>
<html lang="zh-cn">
<head>
    <title>用户登陆</title>
    <#include "basic.ftl">
</head>

<body>
	<div class="loginPage">
		<div class="pageTop">
			<img src="../static/img/logo.png" />			
			<span>移动考勤云服务平台</span>
		</div>
		<div class="pageMid row" style="margin: 0px;">
			<div class="col-md-6 pageLeft">
				<h1>数据快速对接&nbsp;&nbsp;助力移动实名制</h1>
				<img src="../static/img/login_back.png" />
			</div>
			<div class="col-md-6">
		        <div class="login_box">
		        	<div class="loginTitle">
		       	  		<span class="loginTitleStr">移动考勤云服务平台</span>
		       	  		<div class="line" style="height: 1px;background-color: #dcdcdc;"></div>
		       	  	</div>          
			        <form class="loginForm">
						<div class="form-group">
						  <div class="input-group loginItem">
						  	<span class="input-group-addon"><span class="glyphicon glyphicon-user"></span></span>
						  	<input name="account" type="text" class="form-control account" placeholder="请输入登录账号" data-required>
						  </div>
						  <div class="input-group loginItem">
						    <span class="input-group-addon"><span class="glyphicon glyphicon-lock"></span></span>
						    <input name="pwd" type="password" class="form-control pwd" placeholder="请输入登录密码" data-required>
						  </div>
						</div>
						<div class="loginBtn">
						  <button type="button" class="btn btn-danger" onclick="loginSubmit()">登&nbsp;录</button>
						</div>
		            </form>
	
		        </div>
			</div>

		</div>
		<div class="pageFoot">
			<span class="companyName">技术支持.腾辉科技</span>
		</div>
	</div>
	
</body>
<script type="text/javascript">
//文档加载完事件
$(function($) {
});

$('.loginForm').bind('keydown',function(event){
    if(event.keyCode == "13") {
        loginSubmit();
    }
});  

loginSubmit = function(){
    var account = $('.account').val();
    var pwd = $('.pwd').val();
    if(account.length == 0 || pwd.length == 0){
        alert("账号或密码为空！");
        return;
    }
    var ajaxData = {};
    ajaxData.account = account;
    ajaxData.pwd = $.md5(pwd);
    $.ajax({
        type: "POST",
        url: "/loginSubmit",
        data: ajaxData,  
        success: function(data) {  
            if(data.status == '0'){
                alert(data.message);
                return;
            }
            $(location).attr('href', '/');
        },
        error: function(request) {  
            alert("请求提交失败"); 
        },  
    });
}  
</script>
<style>

.pageLeft h1{
    font-family: Georgia;
	font-family: sans-serif;
	font-size: 37px;
	color: #e4242f;
	position: absolute;
	margin: 16% 10px 0 30%;
}

.pageLeft img{
	position: absolute;
	height: 89%;
    margin: 5% 0 5% 30%;
}
.pageLeft {
    height: 100%;
}

.loginItem .input-group-addon{
	border: 0;
}

.pageTop span{
	float: left;
	color: #fff;
	display: block;
	padding-top: 17px;
}

.pageTop img{
	float: left;
	padding-top: 8px;
    padding-left: 12px;
}

.pageFoot span{
	display: block;
	text-align: center;
	padding-top: 40px;
	color: #fff;
	font-size: 16px;
}

.loginBtn button{
	width: 100%;
	height: 55px;
	margin-top: 40px;
	font-size: 22px;
}

.loginItem input{
    height: 55px;
    border: 0px;
    background-color: #e8e8e8;
    border-radius:7px;
}

.loginItem{
	margin-top: 40px;
	height: 55px;
    background-color: #e8e8e8;
    border-radius:7px;
}

.loginTitleStr{
    display: block;
    padding-top: 25px;
}

.loginTitle{
	font-size: 26px;
	height: 82px;
	width: 360px;
}

.login_box{
	padding: 0px 37px;
	margin: 8% 0px 0px 10%;
	height: 426px;
	width: 434px;
	background-color: #FFF;
}

.pageTop{
	height: 8%;
	background-color: #252b3a;
}
.pageMid{
	height: 78%;
	background-color: #e1e8f2;
}
.pageFoot{
	height: 14%;
	background-color: #252b3a;
}

.loginPage{
	height: 100%;
	width: 100%;
}
body {
	height: 100%;
	width: 100%;
}
</style>
</html>