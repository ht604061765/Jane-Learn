package com.tenfine.napoleon.smzPlatform.client;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tenfine.napoleon.framework.util.ByteUtil;
import com.tenfine.napoleon.framework.util.DateUtil;
import com.tenfine.napoleon.framework.util.NationUtils;
import com.tenfine.napoleon.framework.util.PathStatic;
import com.tenfine.napoleon.smzPlatform.message.SendMessageBo;
import com.tenfine.napoleon.smzPlatform.message.UploadUserVo;

public class MessageUtil {
    private static final Logger logger = LoggerFactory.getLogger(MessageUtil.class);

	private static String deviceNoSuffix = "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";
	
	public static SendMessageBo CreateMsg(byte[] conternt, int cmd, String session, boolean success, int version) {
		byte flag = 0x01;
		if (success) {
			flag = 0x00;
		}
		return new SendMessageBo(conternt, cmd, session, flag, version);
	}
	
	/**
	 * 创建上传考勤数据+考勤图片的消息，指令：825
	 */
	public static SendMessageBo createUploadAttendanceMsg(String session, List<Map<String, Object>> sendList) {
		byte[] content = new byte[0];
		for (Map<String, Object> attendance : sendList) {
			int workNo = Integer.parseInt((String) attendance.get("workerNo"));
			byte[] workerNoByte = ByteUtil.intTo4Bytes(workNo);

			String attendanceTime = (String) attendance.get("timeStamp");
			String YYMMddHHmmii = DateUtil.conversionTimestamp(attendanceTime);
			byte[] attendanceTimeByte = ByteUtil.hexStr2Bytes(YYMMddHHmmii);

			byte[] typeByte = ByteUtil.intTo1Bytes(6);
			content = ByteUtil.concatAll(content, workerNoByte, attendanceTimeByte, typeByte);
		}
		byte[] xor = { 0x00 };
		content = ByteUtil.concatAll(content,xor);
		return CreateMsg(ByteUtil.xor(content), CMD.upload_attendance.getCode(), session, true, 1);
	}
	
	/**
	 * 创建上传考勤数据+考勤图片的消息，指令：842
	 */
	public static SendMessageBo createUploadAttendanceWithFileAddressMsg(String session, List<Map<String, Object>> sendList) {
		byte[] content = new byte[0];
		for (Map<String, Object> attendance : sendList) {
			//工人编号
			int workNo = Integer.parseInt((String) attendance.get("workerNo"));
			byte[] workerNoByte = ByteUtil.intTo4Bytes(workNo);
			//考勤时间
			String attendanceTime = (String) attendance.get("timeStamp");
			String YYMMddHHmmii = DateUtil.conversionTimestamp(attendanceTime);
			byte[] attendanceTimeByte = ByteUtil.hexStr2Bytes(YYMMddHHmmii);
			//考勤类型写死
			byte[] typeByte = ByteUtil.intTo1Bytes(6);
			//考勤照片及长度
			String idNo = (String) attendance.get("personIdNo");
			String attendanceFilePath = PathStatic.getAttendancePath(idNo) + idNo + "/" + attendanceTime + "attend.jpg";
			//ps:文件有效性已经在前边判断过了
			File attendanceFile = new File(attendanceFilePath);
			byte[] fileByte = ByteUtil.file2byte(attendanceFile);
			int fileLength = fileByte.length;
			byte[] fileLengthByte = ByteUtil.intTo4Bytes(fileLength);
			//考勤经度
			String longitude = (String) attendance.get("longitude");
			byte[] longitudeByte = ByteUtil.convertAddressString(longitude);
			//考勤纬度
			String latitude = (String) attendance.get("latitude");
			byte[] latitudeByte = ByteUtil.convertAddressString(latitude);
			
			content = ByteUtil.concatAll(content, workerNoByte, attendanceTimeByte, typeByte, fileLengthByte, fileByte, longitudeByte, latitudeByte);
//			logger.info("【842】【SEND】:组装考勤记录，idNo=" + idNo + ",考勤时间=" + YYMMddHHmmii);
		}
		byte[] xor = { 0x00 };
		content = ByteUtil.concatAll(content,xor);
		return CreateMsg(ByteUtil.xor(content), CMD.upload_attendance_file_address.getCode(), session, true, 1);
	}
	
	public static void main(String[] args) {
		byte[] aaa = "\0\0\0\0\0\0\0\0\0\0".getBytes();
		System.out.println("aaa");
	}
	
	/**
	 * 创建心跳消息，指令：65535
	 */
	public static SendMessageBo createHeartbeatMsg(String session){
		return CreateMsg(new byte[0], CMD.heartbeat.getCode(), session, true, 1);
	}
	
	/**
	 * 创建获取项目白名单消息，指令：824
	 */
	public static SendMessageBo createGetDeviceInfoMsg(String deviceNo, String session, String lastTime){
		byte[] xor = { 0x00 };
		byte[] content = ByteUtil.concatAll((deviceNo + deviceNoSuffix).getBytes(),ByteUtil.hexStr2Bytes(lastTime), xor);
		return CreateMsg(ByteUtil.xor(content), CMD.get_device_info.getCode(), session, true, 1);
	}
	
	/**
	 * 创建获取设备绑定信息指令，指令：838
	 */
	public static SendMessageBo createGetDeviceBindMsg(String deviceNo, String session){
		byte[] thsn = "C7A72EED-976C-415C-83B4-F36DE630".getBytes();
		byte[] xor = { 0x00 };
		byte[] content = ByteUtil.concatAll(thsn, (deviceNo + deviceNoSuffix).getBytes(), xor);
		return CreateMsg(ByteUtil.xor(content), CMD.get_device_bind.getCode(), session, true, 1);
	}
	
	/**
	 * 获取人员特征信息：指令841
	 * @param content：要发送的消息内容
	 * @return ShenZhenSendMessage
	 */
	public static SendMessageBo createGetOutUserMsg(int platformProjectId, int listSize, byte[] content, String session){
		// 16字节的验证票据，写死
		byte[] verifyTicket = "FACESOCKETACCESS".getBytes();
		// 4字节的项目ID
		byte[] proIdByte = ByteUtil.intTo4Bytes(platformProjectId);
		// 1字节得是否全量获取，写死为0（非全量）,
		byte[] isGetAll = { 0x00 };
		// 拼装内容前缀
		byte[] listSizeByte=new byte[1];
		listSizeByte[0] = (byte) listSize;
		byte[] contentPrefix = ByteUtil.concatAll(verifyTicket, proIdByte, isGetAll, listSizeByte);
		// 拼装内容后缀,XOR校验运算
		byte[] contentSuffix = { 0x00 };
		// 组装成最终要发送的消息
		byte[] newContent = ByteUtil.concatAll(contentPrefix, content, contentSuffix);
		return CreateMsg(ByteUtil.xor(newContent), CMD.get_user_info.getCode(), session, true, 1);
	}
	
	public static SendMessageBo createUploadUserMsg(UploadUserVo userInfo, String session) {
		// 1身份证
		byte[] idNoByte = userInfo.getIdNo().getBytes();
		// 2名字
		byte[] nameByte = new byte[30];
		// 3性别
		byte[] sex = (userInfo.getSex() +"\0").getBytes();
		// 4民族
		String nationCode = NationUtils.getCode(userInfo.getNation());
		if(nationCode == null){
			nationCode = NationUtils.getCode("汉族");
		}
		byte[] nationCodeByte = (nationCode + "\0\0").getBytes();
		// 5出生日期
		byte[] birthdayByte = (userInfo.getBirthday() + "\0\0\0\0\0\0\0\0").getBytes();
		// 6地址
		byte[] addressByte = new byte[140];
		// 7虹膜
		byte[] hongmoByte = new byte[2048];
		// 8识别度
		byte[] shibieduByte = ByteUtil.intTo1Bytes(0);
		// 9是否重采
		byte[] reCollectByte = ByteUtil.intTo1Bytes(1);
		// 10发证机关
		byte[] provideByte = new byte[60];
		// 11有效期
		byte[] validityByte = new byte[64];
		// 12是否手工
		byte[] shougongByte = ByteUtil.intTo1Bytes(0);
		// 13 班组平台id
		byte[] teamIdByte = ByteUtil.intTo4Bytes(1);
		// 14保留
		byte[] baoliuByte = new byte[95];
		// 15身份证照片长度
		File idCardImg = new File(userInfo.getIdCardImgPath());
		// 16身份证照片
		byte[] idCardImgByte = ByteUtil.file2byte(idCardImg);
		byte[] idCardImgByteLength = ByteUtil.intTo4Bytes(idCardImgByte.length);
		// 17采集照片长度
		File collectFile = new File(userInfo.getCollectPath());
		byte[] collectFileByte = ByteUtil.file2byte(collectFile);
		byte[] collectFileByteLength =  ByteUtil.intTo4Bytes(collectFileByte.length);
		// 18采集照片
		byte[] xor = { 0x01 };
		
		try {
			byte[] validityTemp = userInfo.getValidity().getBytes("UTF-8");
			System.arraycopy(validityTemp, 0, validityByte, 0, validityTemp.length);
			
			byte[] nameTemp = userInfo.getName().getBytes("UTF-8");
			System.arraycopy(nameTemp, 0, nameByte, 0, nameTemp.length);
			
			byte[] addressTemp = userInfo.getAddress().getBytes("UTF-8");
			System.arraycopy(addressTemp, 0, addressByte, 0, addressTemp.length);
			
			byte[] provideTemp = userInfo.getProvide().getBytes("UTF-8");
			System.arraycopy(provideTemp, 0, provideByte, 0, provideTemp.length);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		byte[] content = ByteUtil.concatAll(
				idNoByte, // 1
				nameByte, // 2
				sex, // 3
				nationCodeByte, // 4
				birthdayByte, // 5
				addressByte, // 6
				hongmoByte, // 7
				shibieduByte, // 8
				reCollectByte, // 9
				provideByte, // 10
				validityByte, // 11
				shougongByte, // 12
				teamIdByte,// 13
				baoliuByte, // 14
				idCardImgByteLength, // 15
				idCardImgByte, // 16
				collectFileByteLength, // 17
				collectFileByte, // 18
				xor);// 19

		return CreateMsg(ByteUtil.xor(content), CMD.upload_user.getCode(), session, true, 1);
	}
}
