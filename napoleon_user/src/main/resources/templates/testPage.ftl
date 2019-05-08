<!doctype html>
<html lang="zh-cn">
<head>
<title>横琴新区移动考勤云服务平台</title> 
<#include "basic.ftl">
</head>
<body>
	
	<div class="row" style="height:100%; margin-right:0px;">
		<div class="personDetail" style="float:left;">
				<div><span>平台Id：</span><strong>${(platformId)!''}</strong></div>
				<div><span>平台名称：</span><strong>${(platformName)!''}</strong></div>
				<div><span>平台Ip：</span><strong>${(socketServer)!''}</strong></div>
				<div><span>平台端口：</span><strong>${(socketPort)!''}</strong></div>
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