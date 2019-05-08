<div class="modal-dialog" role="document">
    <div class="modal-content">
	    <div class="modal-header">
	    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">考勤详情</h4>
	    </div>
	    <div class="modal-body">
		    <div class="attendanceDetail" style="float:left;">
				<div><span>工人编号：</span><strong>${(attendance.workerNo)!''}</strong></div>
				<div><span>人员名称：</span><strong>${(attendance.personName)!''}</strong></div>
				<div><span>身份证：</span><strong>${(attendance.personIdNo)!''}</strong></div>
				<div><span>考勤设备：</span><strong>${(attendance.deviceNo)!''}</strong></div>
				<div><span>推送状态：</span><strong>${(attendance.state == '1')?string('已推送','未推送')!''} </strong></div>
				<div><span>考勤地点：</span><strong>${(attendance.attendancePlace)!''}</strong></div>
				<div><span>匹配度：</span><strong>${(attendance.matchScore)!''}</strong></div>
				<div><span>考勤经度：</span><strong>${(attendance.longitude)!''}</strong></div>
				<div><span>考勤纬度：</span><strong>${(attendance.latitude)!''}</strong></div>
			</div>
			<div class="attendancePhoto" style="float:right;">
				<img src="/getImg?type=attend&idNo=${(attendance.personIdNo)!''}&timeStamp=${(attendance.timeStamp)!''}" style="width: 100px;">
			</div>
	    </div>
	    <div class="modal-footer">
	      <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
		</div>
	</div><!-- /.modal-content -->
</div><!-- /.modal-dialog -->

<script>

</script>
<style>

</style>