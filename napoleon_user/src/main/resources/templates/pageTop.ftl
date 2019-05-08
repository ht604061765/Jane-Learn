<div class="strHead">
    <div class="leftStr">
    	<div style="float: left;">
    		<img src="../static/img/logo.png" />
    	</div>
	    <div class="chiStr">${(Session["platformName"])!''}</div>
    </div>
    <div class="rightStr">
        <div class="userNameStr pointer">退出登录&nbsp;&nbsp;<span class="glyphicon glyphicon-off"></span></div>
    </div>
</div>

<script>

$(".rightStr").bind("click",function(){
	logout();
});

$(function(){
});

//切换页面
changePage = function(menuClass){
	var url = '/' + menuClass;
	$.ajax({
        type: "POST",
        url: url,
        data: {},
        success: function(data) {
        	$(".pageRight").empty();
            $('.pageRight').append(data);
        },
        error: function(request) {  
            alert('异步提交失败');
        },  
    });
}
logout = function(){
	$.ajax({
        type: "POST",
        url: "/logout",
        data: {},
        success: function(data) {
        	if(data.status == 1){
        		$(location).attr('href', '/login');
        	}
        },
        error: function(request) {  
            alert('异步提交失败');
        },  
    });
}
</script>

<style>
.leftStr .chiStr{
	float: left;
    font-size: 24px;
    margin: 5% 0 0 5px;
}
.rightStr .loginStr{
     margin: 26px 10px 0 0;
}
.rightStr .userNameStr a{
    color: #fff;
}
.rightStr .userNameStr{
     margin: 26px 10px 0 0;
}
.rightStr{
     float:right;
}

.leftStr img{
    margin: 16px 0 0 16px;
}

.leftStr{
    float:left;
}
.strHead{
    color: #fff;
    font-weight: bold;
}

.strHead{
	background-color: #252b3a;
    height: 72px;
    z-index:10;
    position: absolute;
    width: 100%;
}
</style>