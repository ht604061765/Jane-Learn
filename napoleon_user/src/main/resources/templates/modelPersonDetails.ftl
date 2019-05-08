<div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
	    <div class="modal-header">
        	<h4 class="modal-title">人员详情</h4>
	    </div>
	    <div class="modal-body">
	    	<div><Strong>基本信息</Strong></div>
		    <div class="personDetailBox row" style="margin-left: 15px;">
				<div class="col-md-5 personDetail">姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：${(personInfo.name)!''}</div>
				<div class="col-md-7 personDetail">身份证号：${(personInfo.idNo)!''}</div>
				
				<div class="col-md-5 personDetail">性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别：${(personInfo.sex)!''}</div>
				<div class="col-md-7 personDetail">有效期限：${(personInfo.period)!''}</div>
				
				<div class="col-md-5 personDetail">民&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;族：${(personInfo.nation)!''}</div>
				<div class="col-md-7 personDetail">住&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;址：${(personInfo.address)!''}</div>
				
				<div class="col-md-5 personDetail">生&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日：${(personInfo.birthday)!''}</div>
				<div class="col-md-7 personDetail">签发机关：${(personInfo.provide)!''}</div>
				
				<div class="col-md-5 personDetail">采集人员：${(personInfo.collectUser)!''}</div>
				<div class="col-md-7 personDetail">采集时间：${(personInfo.createTime)!''}</div>
				
			</div>
			<div><Strong>采集图片</Strong></div>
			<div class="personPhotoBox row" style="margin-left: 15px;">
				<div class="col-md-3 collect">
					<img src="/getImg?type=collect&idNo=${(personInfo.idNo)!''}">
					<div class="collectHref downloadHerf pointer"><a href="/getImg?type=collect&idNo=${(personInfo.idNo)!''}" download="collectPhoto.png">点击下载</a></div>
				</div>
				<div class="col-md-3 cardFront">
					<img src="/getImg?type=cardFront&idNo=${(personInfo.idNo)!''}">
					<div class="cardFrontHref downloadHerf pointer"><a href="/getImg?type=cardFront&idNo=${(personInfo.idNo)!''}" download="cardFrontPhoto.png">点击下载</a></div>
				</div>
				<div class="col-md-3 cardBack">
					<img src="/getImg?type=cardBack&idNo=${(personInfo.idNo)!''}">
					<div class="cardBackHref downloadHerf pointer"><a href="/getImg?type=cardBack&idNo=${(personInfo.idNo)!''}" download="cardBackPhoto.png">点击下载</a></div>
				</div>
				
			</div>
	    </div>
	    <div class="modal-footer">
	      <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
		</div>
	</div><!-- /.modal-content -->
</div><!-- /.modal-dialog -->

<script>

trackDownload = function(){
	$(".personPhotoBox .collectHref").hide();
	$(".personPhotoBox .cardFrontHref").hide();
	$(".personPhotoBox .cardBackHref").hide();

	$(".personPhotoBox .collect").mouseover(function (){  
		$(".personPhotoBox .collectHref").show();
	}).mouseout(function (){  
		$(".personPhotoBox .collectHref").hide();
	});  

	$(".personPhotoBox .cardFront").mouseover(function (){  
		$(".personPhotoBox .cardFrontHref").show();
	}).mouseout(function (){  
		$(".personPhotoBox .cardFrontHref").hide();
	});  

	$(".personPhotoBox .cardBack").mouseover(function (){  
		$(".personPhotoBox .cardBackHref").show();
	}).mouseout(function (){  
		$(".personPhotoBox .cardBackHref").hide();
	});
}
$(function($) {
	trackDownload();
});

</script>
<style>

.personPhotoBox a{
	color: white;
}
.personPhotoBox .downloadHerf{
	opacity: 0.8;
	position: absolute;
    margin: 5% 20%;
}

.personPhotoBox{
	background-color: #4b5879;
	border-radius:11px;
	height: 50%;
}

.personPhotoBox img{
	float: left;
	width: 80%;
	margin-top: 10px;
}
.personDetailBox .personDetail{
	margin-top: 7px;
}
.personDetailBox{
	width: 98%;
	height: 150px;
	color: whitesmoke;
	border-radius:11px;
	background-color: #4b5879;
}
.modal-header{

}
.modal-body{
	height: 500px;
}
</style>