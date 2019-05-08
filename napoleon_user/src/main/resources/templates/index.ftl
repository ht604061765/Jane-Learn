<!doctype html>
<html lang="zh-cn">
<head>
<title>${(Session["platformName"])!''}</title> 
<#include "basic.ftl">
</head>
<body>
	<#include "pageTop.ftl">
	
	<div class="row" style="height:100%; margin-right:0px;">
		<div class="col-md-2" style="height:100%;">
			<#include "leftMenu.ftl">
		</div>
		<div class="col-md-10 pageRight">
			
		</div>
	</div>
</body>

<script type="text/javascript">
//文档加载完事件
$(function($) {
	
});
   
</script>

<style>

.pageRight{
	box-shadow: 2px 0px 10px #888888;
	height: 99%;
}

</style>
</html>