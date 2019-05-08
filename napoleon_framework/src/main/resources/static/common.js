//自动执行事件
$(function () {
	});

var createTable = function(tbId, url, toolbar, pageSize, height, columns, pageParam){
	// debugger
	var oTable = new TableInit(pageParam);
	oTable.Init(tbId,url,toolbar,pageSize,height,columns);
}

var TableInit = function (pageParam) {
	if(pageParam == undefined){
		pageParam={}
	}
    var oTableInit = new Object();
    //初始化Table
    oTableInit.Init = function (tbId,url,toolbar,pageSize,height,columns) {
        $('#' + tbId).bootstrapTable({
            url: url,     						//请求后台的URL（*）
            method: 'post',                     //请求方式（*）
            contentType: 'application/x-www-form-urlencoded',
            toolbar: '#'+toolbar,               //工具按钮用哪个容器
            striped: false,                     //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: false,                    //是否启用排序
            sortOrder: "asc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: pageSize,                 //每页的记录行数（*）
            pageList: [5, 10, 20],        			//可供选择的每页的行数（*）
            search: true,                       //是否显示表格搜索
            strictSearch: false,                //严格查找
            showColumns: true,                  //是否显示所有的列
            minimumCountColumns: 2,             //最少允许的列数
            clickToSelect: true,              	//是否启用点击选中行
            singleSelect: true,					//是否单选
            // height: height,                     //行高，如果没有设置height属性，表格自动根据记录条数设置表格高度
            uniqueId: "id",                     //每一行的唯一标识，一般为主键列
            cardView: false,                    //是否显示详细视图
            detailView: false,                  //是否显示父子表
            columns: columns,
            responseHandler: function(res) {
            	return res;
            },
            onClickRow: function(){
            	//alert('aa');
            }
        });
    };
    //得到查询的参数
    oTableInit.queryParams = function (params) {
        var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            pageSize: params.limit,   
            offset: params.offset,
            searchKey: params.search,
            pageParam: JSON.stringify(pageParam)
        };
        return temp;
    };
    return oTableInit;
};

//显示提示内容：消息类别(success,info,warn,danger)，消息内容
tipMsg = function(msgType,msg){
	$msgDiv = $('<div>').addClass('alert').css('color','#ffffff').css('float','right').css('margin','14px').append(msg);
	if(msgType == 'success'){
		$msgDiv.css('color','#3c763d');
		$msgDiv.css('background-color','#dff0d8');
		$msgDiv.css('border-color','#d6e9c6');
	}else if(msgType == 'info'){
		$msgDiv.css('color','#31708f');
		$msgDiv.css('background-color','#d9edf7');
		$msgDiv.css('border-color','#bce8f1');
	}else if(msgType == 'warn'){
		$msgDiv.css('color','#8a6d3b');
		$msgDiv.css('background-color','#fcf8e3');
		$msgDiv.css('border-color','#faebcc');
	}else if(msgType == 'danger'){
		$msgDiv.css('color','#a94442');
		$msgDiv.css('background-color','#f2dede');
		$msgDiv.css('border-color','#ebccd1');
	}
	$('body').append($msgDiv);

	setTimeout(function() {
		$msgDiv.hide();
	}, 3000);
}

//初始化fileinput
var FileInput = function () {
    var oFile = new Object();

    //初始化fileinput控件（第一次初始化）
    oFile.Init = function(ctrlName, uploadUrl, callback) {
    var control = $('#' + ctrlName);

    //初始化上传控件的样式
    control.fileinput({
        language: 'zh', //设置语言
        uploadUrl: uploadUrl, //上传的地址
        allowedFileExtensions: ['jpg', 'gif', 'png', 'txt'],//接收的文件后缀
        showUpload: true, //是否显示上传按钮
        showCaption: false,//是否显示标题
        browseClass: "btn btn-primary", //按钮样式     
        //dropZoneEnabled: false,//是否显示拖拽区域
        //minImageWidth: 50, //图片的最小宽度
        //minImageHeight: 50,//图片的最小高度
        //maxImageWidth: 1000,//图片的最大宽度
        //maxImageHeight: 1000,//图片的最大高度
        //maxFileSize: 0,//单位为kb，如果为0表示不限制文件大小
        //minFileCount: 0,
        //maxFileCount: 10, //表示允许同时上传的最大文件个数
        enctype: 'multipart/form-data',
        validateInitialCount:true,
        previewFileIcon: "<i class='glyphicon glyphicon-king'></i>",
        msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
    });

    //导入文件上传完成之后的事件
    $("#" + ctrlName).on("fileuploaded", function (event, data, previewId, index) {
        $("#testMyModal").modal("hide");
        if (data != undefined) {
        	if('function' == typeof(callback)){
            	callback(data.response);
            }else{
            	alert("上传成功！");
            }
        }else{
        	alert("上传失败，请联系管理员！");
        	return;
        }
        
        var data = data.response.status;
        if (data == undefined) {
            alert('文件格式类型不正确');
            return;
        }
    });
}
    return oFile;
};

function closeModel(modelName){
	$('.' + modelName).modal('hide');
}

function ajaxPost(url,data,callBack){
	$.ajax({
		type: "POST",
		url: url,
		data: data,  
		success: function(data) {  
			if(typeof callBack == "function"){
				callBack(data);
			}
		},
		error: function(request) {  
	        alert("异步提交失败");  
	    },  
	});
}
