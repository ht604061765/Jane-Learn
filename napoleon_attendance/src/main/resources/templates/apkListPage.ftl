<div class="apkListPage">
	<div id="toolbar_apkList">
		<button type="button" class="btn btn-danger btn-sm" onclick="fileSelect()">
			<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>上传APK
		</button>
		<form id="uploadForm" enctype="multipart/form-data">
			<input class="fileSelect" onchange="uploadFile()" type="file" name="packageFile" style="filter:alpha(opacity=0);opacity:0;width: 0;height: 0;"/>   
		</form>
	</div>
	<table id="tb_apkList"></table>  
</div>

<!-- apk编辑模态窗口 -->
<div id="apkDetailsModel" class="apkDetailsModel modal fade" tabindex="-1" role="dialog"></div>

<script type="text/javascript">

uploadFile = function(){
	var formData = new FormData($('#uploadForm')[0])
	$.ajax({
        type: "POST",
        url: "/uploadApk",
        data: formData,
        cache: false,
        processData: false,
        contentType: false,
        success: function(data) {
        	if(data.status == 1){
            	$('#tb_apkList').bootstrapTable('refresh'); // 注意，这个地方可以加参数，可以控制刷新到哪个页
            	alert(data.message);
            }
        },
        error: function(request) {  
            alert("异步请求失败");
        },  
    });
}

fileSelect = function(){
	$(".fileSelect").trigger("click");
}

apkDetails = function(apkId){
	$.ajax({
        type: "POST",
        url: "/modelApkDetails",
        data: {
        	apkId : apkId
        },
        success: function(data) {  
            $(".apkDetailsModel").empty();
            $('.apkDetailsModel').append(data);
            $('.apkDetailsModel').modal('show')
        },
        error: function(request) {  
            alert("异步请求失败");
        },  
    });
}

refreshApkList = function(){
    var columns = [{
        field: 'name',
        title: 'APK名称',
        align: 'center',
        width: '100px'
    }, {
        field: 'createTime',
        title: '上传时间',
        align: 'center'
    }, {
        field: 'version',
        title: '版本号',
        align: 'center'
    }, {
        field: 'summary',
        title: '版本描述',
        align: 'center',
    }, {
        title: '是否有效',
        align: 'center',
        width: '100px',
        formatter: function (value, row, index){
        	var text = "";
	        if(row.state == "1"){
	        	text = "<span style='color:green;''>有效</span>";
	        }else if(row.state == "0"){
	        	text = "<span style='color:red;''>无效</span>";
	        }
            return text;
        }
    }, {
        title: '操作',
        align: 'center',
        valign: 'middle',
        formatter: function (value, row, index){
        	var text = '<button type="button" class="btn btn-warning btn-xs" onclick="apkDetails(\'' + row.id + '\')">编辑</button>';
            return text;
        }
    }]
    var pageParam = {}
    createTable('tb_apkList','/getApkList','toolbar_apkList', 10, 635, columns);
}

$(function () {
	refreshApkList();
});

</script>
<style>

.apkListPage{
	padding-top:72px;
}

</style>