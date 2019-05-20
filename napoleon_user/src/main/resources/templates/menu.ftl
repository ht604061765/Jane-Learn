<!doctype html>
<html>
<head>
    <head>
        <meta  charset = "utf-8"/>
        <meta name = "viewport" content = "width-device=width,initial = 1.0"/>
        <!--bootstrap.css -->
        <link  href = "css/bootstrap.css" rel = "stylesheet" type = "text/css"/>
        <!-- bootstrap.js -->
        <script src = "js/jquery-1.9.1.js"> </script>
        <!-- bootstrap.js -->
        <script src = "js/bootstrap.js"></script>
    </head>
<body>
<nav class="navbar navbar-default" role="navigation">
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#menu">
            <span class="sr-only">展开导航</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="#">网站标题</a>
    </div>
    <div class="collapse navbar-collapse" id="menu">
        <ul class="nav navbar-nav">
            <li class="active"><a href="#">首页</a></li>
            <li><a href="#">导航标题1</a></li>
            <li><a href="#">导航标题2</a></li>
            <li class="dropdown"> <a href="#" class="dropdown-toggle" data-toggle="dropdown">下拉菜单 <b class="caret"></b></a>
                <ul class="dropdown-menu">
                    <li><a href="#">下拉菜单1</a></li>
                    <li class="divider"></li>
                    <li><a href="#">下拉菜单2</a></li>
                    <li class="divider"></li>
                </ul>
            </li>
        </ul>
    </div>
</nav>
</body>
</html>