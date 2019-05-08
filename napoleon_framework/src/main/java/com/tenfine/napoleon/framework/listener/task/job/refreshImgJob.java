//package com.tenfine.napoleon.framework.listener.task.job;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.InputStream;
//
//import org.quartz.Job;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.util.ResourceUtils;
//
//import com.tenfine.napoleon.framework.util.StreamUtil;
//import com.tenfine.napoleon.framework.util.URLUtil;
//
//import net.sf.json.JSONObject;
//
//public class refreshImgJob implements Job{
//	private static final Logger logger = LoggerFactory.getLogger(refreshImgJob.class);
//
//	public void execute(JobExecutionContext context) throws JobExecutionException {
//		logger.info("测试[更新背景图片定时任务]开始更新背景图片");
//		String domain = "http://www.bing.com";
//		String jsonUrl = "/HPImageArchive.aspx?format=js&idx=0&n=1";
//		String imgCopyright = "";//后续优化有用
//        String imgName = "bing_backImg.png";
//		
//		try {
//			File fileRoot = new File(ResourceUtils.getURL("classpath:").getPath());
//			if(!fileRoot.exists()) {
//				fileRoot = new File("");
//			} 
//			String rootPath = fileRoot.getAbsolutePath();
//			String webRootPath = rootPath.replace("\\target\\classes", "");
//			String targetPath = webRootPath + "\\src\\main\\resources\\static\\img\\";
//			File targetFile = new File(targetPath,imgName);
//			
//	        String imgDetails =StreamUtil.getString(URLUtil.getInputStreamByGet(domain + jsonUrl));
//	        JSONObject imgDetailsObject = JSONObject.fromObject(imgDetails);
//	        String imagesStr = imgDetailsObject.getString("images");
//	        JSONObject imagesObject = JSONObject.fromObject(imagesStr.substring(1, imagesStr.length()-1));
//			String imgUrl = imagesObject.getString("url");
//			InputStream imgInputStream = URLUtil.getInputStreamByGet(domain + imgUrl);
//			URLUtil.saveData(imgInputStream, targetFile);
//
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		logger.info("[更新背景图片定时任务]更新背景图片结束");
//	}
//
//	public static void main(String[] args) {
//		logger.info("测试[更新背景图片定时任务]开始更新背景图片");
//		String domain = "http://www.bing.com";
//		String jsonUrl = "/HPImageArchive.aspx?format=js&idx=0&n=1";
//		String imgCopyright = "";//后续优化有用
//        String imgName = "bing_backImg.png";
//		
//		try {
//			File fileRoot = new File(ResourceUtils.getURL("classpath:").getPath());
//			if(!fileRoot.exists()) {
//				fileRoot = new File("");
//			} 
//			String rootPath = fileRoot.getAbsolutePath();
//			String webRootPath = rootPath.replace("\\target\\classes", "");
//			String targetPath = webRootPath + "\\src\\main\\resources\\static\\img\\";
//			File targetFile = new File(targetPath,imgName);
//			
//	        String imgDetails = URLUtil.getStreamString(URLUtil.getInputStreamByGet(domain + jsonUrl));
//	        JSONObject imgDetailsObject = JSONObject.fromObject(imgDetails);
//	        String imagesStr = imgDetailsObject.getString("images");
//	        JSONObject imagesObject = JSONObject.fromObject(imagesStr.substring(1, imagesStr.length()-1));
//			String imgUrl = imagesObject.getString("url");
//			InputStream imgInputStream = URLUtil.getInputStreamByGet(domain + imgUrl);
//			URLUtil.saveData(imgInputStream, targetFile);
//
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		logger.info("[更新背景图片定时任务]更新背景图片结束");
//	}
//
//}
