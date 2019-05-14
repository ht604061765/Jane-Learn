<div class="operation" style="margin-bottom:20px">
        <p id="test">用户管理</p>
        姓名：<input type="text" name="username"></input>
        性别：<input type="text" name="sex"></input>
        电话：<input type="text" name="phone"></input>
        账号：<input type="text" name="account"></input>
        密码：<input type="password" name="password"></input>
        职业：<input type="text" name="job"></input>
        备注：<input type="text" name="remark"></input>
    </div>
    <div class="operation" style="margin-bottom:20px">
        <button id = "addUser">新增</button>
    </div>
    <table id = "mytable" border="1">
        <tr>
            <td>姓名</td>
            <td>性别</td>
            <td>电话</td>
            <td>账号</td>
            <td>密码</td>
            <td>职业</td>
            <td>备注</td>
            <td>删除</td>
            <td>修改</td>
        </tr>
        <tr>
            <td>张三</td>
            <td>男</td>
            <td>13973907727</td>
            <td>zhangs</td>
            <td>zhangs</td>
            <td>test</td>
            <td>我是谁</td>
            <td><button id = "deleteUser">删除</button></td>
            <td><button id = "modifyUser">修改</button></td>
        </tr>
     </table>

<!-- 添加用户模态窗口 -->
<div id="addUserModel" class="addUserModel modal fade" tabindex="-1" role="dialog"></div>
<script>

addUser = function(){
	$.ajax({
        type: "POST",
        url: "/modelAddUser",
        data: {},
        success: function(data) {
            $(".addUserModel").empty();
            $('.addUserModel').append(data);
            $('.addUserModel').modal('show')
        },
        error: function(request) {
            alert("异步请求失败");
        },
    });
}

refreshUserList = function(){
	var columns = [{
        field: 'name',
        title: '用户昵称',
        align: 'center'
    }, {
        field: 'account',
        title: '用户账号',
        align: 'center'
    }, {
        field: 'createTime',
        title: '创建时间',
        align: 'center'
    }, {
        title: '用户状态',
        align: 'center',
        formatter: function (value, row, index){ // 单元格格式化函数
            var text = "";
	        if(row.state == "1"){
	        	text = "<span style='color: green;'>启用</span>";
	        }else if(row.state == "0"){
	        	text = "<span style='color: red;'>禁用</span>";
	        }
            return text;
        }
    }, {
        title: '用户类型',
        align: 'center',
        formatter: function (value, row, index){
            var text = "";
	        if(row.type == "ordinary"){
	        	text = "普通用户";
	        }else if(row.type == "admin"){
	        	text = "管理员";
	        }
            return text;
        }
    }, {
        title: '操作',
        align: 'center',
        valign: 'middle',
        formatter: function (value, row, index){
        	var text = "";
        	 if(row.state == "1"){
 	        	text = '<button type="button" class="btn btn-warning btn-xs" onclick="changeUserState(0,\'' + row.id + '\')">禁用</button>';
 	        }else if(row.state == "0"){
 	        	text = '<button type="button" class="btn btn-success btn-xs" onclick="changeUserState(1,\'' + row.id + '\')">启用</button>';
 	        }
            return text;
        }
    }]
    var pageParam = {}
    createTable('tb_userList','/getUserList','toolbar_userList',10,600,columns,pageParam);
}

changeUserState = function(state,userId){
	var paramData = {};
	paramData.state = state;
	paramData.userId = userId;
	$.ajax({
        type: "POST",
        url: "/changeUserState",
        data: paramData,
        success: function(data) {
           if(data.status == 1){
        	 $('#tb_userList').bootstrapTable('refresh', null);// 注意，这个地方可以加参数，可以控制刷新到哪个页
           }else if(data.status == 0){
        	   alert(data.message);
           }
        },
        error: function(request) {
            alert("异步请求失败");
        },
    });
}
$(function () {
	refreshUserList();
});
</script>
<style>
.userListPage{
	padding-top:70px;
}
</style>