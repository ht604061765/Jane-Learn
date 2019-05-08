<div class="headBox">
	<div style="font-size: 19px; margin-top: 10%; margin-left: 23%;">${(Session["userAccount"])!''}</div>
	<div style="font-size: 16px; margin-left: 23%;">${(Session["userName"])!''}</div>
</div>
<div class="menuBox">
    <ul class="nav nav-pills nav-stacked">
    
      <li role="presentation" class="leftMenu">
        <a href="javascript:void(0);" onclick="changeMenu()" class="mainPage"><span class="glyphicon glyphicon-home"></span>&nbsp;&nbsp;主页</a>
      </li>

      <li role="presentation" class="leftMenu">
        <a href="javascript:void(0);" onclick="changeMenu('deviceListPage')" class="deviceListPage"><span class="glyphicon glyphicon-blackboard"></span>&nbsp;&nbsp;APP管理</a>
      </li>
      
      <li role="presentation" class="leftMenu admin">
        <a href="javascript:void(0);" onclick="changeMenu('userListPage')" class="userListPage"><span class="glyphicon glyphicon-tasks"></span>&nbsp;&nbsp;账号管理</a>
      </li>
      
      <li role="presentation" class="leftMenu">
        <a href="javascript:void(0);" onclick="changeMenu('personListPage')" class="personListPage"><span class="glyphicon glyphicon-user"></span>&nbsp;&nbsp;人员查询</a>
      </li>
      
      <li role="presentation" class="leftMenu">
        <a href="javascript:void(0);" onclick="changeMenu('attendanceListPage')" class="attendanceListPage"><span class="glyphicon glyphicon-edit"></span>&nbsp;&nbsp;考勤查询</a>
      </li>
      
      <li role="presentation" class="leftMenu admin">
        <a href="javascript:void(0);" onclick="changeMenu('apkListPage')" class="apkListPage"><span class="glyphicon glyphicon-retweet"></span>&nbsp;&nbsp;设备更新</a>
      </li>

      <li role="presentation" class="leftMenu">
        <a href="javascript:void(0);" onclick="changeMenu('projectListPage')" class="projectListPage"><span class="glyphicon glyphicon-folder-close"></span>&nbsp;&nbsp;项目查询</a>
      </li>

      <li role="presentation" class="leftMenu admin">
        <a href="javascript:void(0);" onclick="changeMenu('socketMapPage')" class="socketMapPage"><span class="glyphicon glyphicon-map-marker"></span>&nbsp;&nbsp;考勤地图</a>
      </li>
      
    </ul>
</div>

<script type="text/javascript">

$(function(){
	powerControl();
	changeMenu();
});

//切换菜单
changeMenu = function(menuClass){
	if(menuClass == undefined){
		menuClass = 'deviceListPage';
	}
	//菜单选中
	$('.menuBox .leftMenu .select').removeClass('select');
	$('.'+menuClass).addClass('select');
	//页面切换
	changePage(menuClass);
}

//权限控制
powerControl = function(){
	$('.admin').hide(); //隐藏管理员菜单
    var userType = '${(Session["userType"])!''}';
    if(userType == 'admin'){ //普通用户
        $('.admin').show(); //隐藏管理员菜单
    }
}

</script>
<style>
.select{
    background-color: #f9f9f9;
}
.nav-pills>li>a {
    padding-top: 16px;
    font-size: 16px;
    border-radius: 0px;
    padding-left: 22%;
    border-bottom: 0px solid #cdc4c4;
}
.leftMenu a{
    color: black;
    height: 50px;
}
.leftMenu img{
    margin: 0px 5px;
}
.menuBox{
    height: 73%;
	margin-top: 7px;
    background-color: #e2e2e2;
    box-shadow: 2px 2px 5px #888888;
}

.headBox{
    background-color: #e2e2e2;
	height: 25%;
    padding-top: 72px;
    box-shadow: 2px 0px 10px #888888;
}

</style>
</html>