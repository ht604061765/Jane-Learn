<!doctype html>
<html lang="en">
<head>
    <title>注册</title>
    <#include "basic.ftl">
</head>

<body>
    <div class="content">
        <div class="row">
            <div class="col-md-10 platform">移动考勤云处理平台</div>
            <div class="col-md-2 already">
                <font color="gray">已有账号！</font><font color="#0f0f0f">登录</font>
            </div>
        </div>
        <div class="topDiv">
            注册账号
        </div>
        <div class="centDiv">
            <form id="registerForm">
                <div class="input-group">
                    <span class="input-group-addon">
                        <img src="../static/img/platform.png">
                    </span>
                    <select id="platform" disabled="disabled" name="platform" class="form-control">
                        <option>横琴实名制</option>
                    </select>
                </div>
                <div class="input-group">
                    <span class="input-group-addon">
                        <img src="../static/img/user.png">
                    </span>
                    <input class="form-control" type="text" name="name" placeholder="请输入用户名">
                </div>
                <div class="input-group">
                    <span class="input-group-addon">
                        <img src="../static/img/account.png">
                    </span>
                    <input class="form-control" type="text" name="account" placeholder="请输入注册账号">
                </div>
                <div class="input-group">
                    <span class="input-group-addon">
                        <img src="../static/img/password.png">
                    </span>
                    <input class="form-control" type="password" name="pwd" placeholder="请输入密码">
                    <span class="input-group-addon">
                        <img src="../static/img/hide.png">
                    </span>
                </div>
                <div class="input-group">
                    <span class="input-group-addon">
                        <img src="../static/img/phone.png">
                    </span>
                    <input class="form-control" type="text" name="phone" placeholder="请输入手机号码">
                </div>
                <div class="input-group">
                    <input class="form-control submit" style="background-color: #0f0f0f;color: white" type="submit" value="注册">
                </div>
            </form>
        </div>
    </div>

    <!--模态框：显示注册用户信息-->
    <div id="checkInfoModel" class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title" id="gridSystemModalLabel">注册信息</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal">
                        <div class="form-group">
                            <label class="col-sm-2 control-label">平台</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" value="横琴实名制" disabled="disabled">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">用户名</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="name" disabled="disabled">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">账号</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="account" disabled="disabled">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">手机号</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="phone" disabled="disabled">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">设备编号</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="deviceNo" name="deviceNo" disabled="disabled">
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" data-dismiss="modal">确定</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>

    <!--模态框：显示错误信息-->
    <div id="errorModel" class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title" id="gridSystemModalLabel">提示信息</h4>
                </div>
                <div class="modal-body">
                    <span id="warnInfo" style="font-size: 16px"></span>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" data-dismiss="modal">确定</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>

</body>

<script type="text/javascript">
    $(function() {
        // 手机号码验证
        jQuery.validator.addMethod("isMobile", function(value, element) {
            var length = value.length;
            var mobile = /^1[3,4,5,7,8][0-9]{9}$/;
            return this.optional(element) || (length == 11 && mobile.test(value));
        }, "请正确填写手机号码");

        // 在键盘按下并释放及提交后验证提交表单
        $("#registerForm").validate({
            rules:{
                name:{
                    required: true
                },
                account:{
                    required:true,
                    minlength:5
                },
                pwd:{
                    required:true
                },
                phone:{
                    required:true,
                    minlength:11,
                    maxlength:11,
                    isMobile:true
                }
            },
            message:{
                name:{
                    required:"用户名不能为空"
                },
                account:{
                    required:"注册账号不能为空",
                    minlength:"账号最小长度是5"
                },
                pwd:{
                    required:"密码不能为空"
                },
                phone:{
                    required:"手机号码不能为空",
                    minlength:"请填写11位数的手机号码",
                    maxlength:"请填写11位数的手机号码",
                    isMobile : "请填写正确的手机号码"
                }
            },
            submitHandler:function(){
                $.ajax({
                    type:"post",
                    url:"/addProUser",
                    dataType:"json",
                    data:{
                        name:$("input[name='name']").val(),
                        account:$("input[name='account']").val(),
                        pwd:$("input[name='pwd']").val(),
                        phone:$("input[name='phone']").val()
                    },
                    success:function(result) {
                        if(result.status == 1) { // 成功
                            $(".modal-body input[name='name']").val(result.data.name)
                            $(".modal-body input[name='account']").val(result.data.account)
                            $(".modal-body input[name='phone']").val(result.data.phone)
                            $(".modal-body input[name='deviceNo']").val(result.data.deviceNo)
                            $('#checkInfoModel').modal('show');
                        } else{ // 失败
                            $("#warnInfo").text(result.message);
                            $("#errorModel").modal('show')
                        }
                    }
                });
            }
        });

    });
</script>
<style>
    label.error {
        color: red;
        position: absolute;
        left: 48px;
        bottom: -25px;
    }
    .content {
        width: 1010px;
        height: 685px;
        margin: 30px auto;
    }
    .topDiv {
        margin-top: 15px;
        text-align: center;
        line-height: 80px;
        overflow: hidden;
        width: 100%;
        height: 80px;
        background-color: #0f0f0f;
        font-size: 20px;
        color: white
    }
    .centDiv {
        margin-top: 42px;
    }
    .input-group {
        width: 362px;
        height: 42px;
        margin: 0 auto;
        margin-top: 32px;
    }
    .centDiv select {
        height: 42px;
        font-size: 16px;
    }
    .centDiv input {
        font-size: 16px;
        height: 42px;
        background-color: #f4f4f4;
    }
    .centDiv span {
        background-color: white;
    }
    submit {
        background-color: #0f0f0f;
        color: white;
    }
    .platform {
        font-size: 22px;
        font-weight: bold
    }
    .already {
        font-size: 16px;
    }
</style>
</html>