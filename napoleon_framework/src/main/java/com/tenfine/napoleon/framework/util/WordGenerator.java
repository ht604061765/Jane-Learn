package com.tenfine.napoleon.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;  
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;  
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateNotFoundException;
import net.sf.json.JSONObject;

@SuppressWarnings("deprecation")
public class WordGenerator {
	private static Configuration configuration = null;  
    private static Map<String, Template> allTemplates = null;  
      
    static {  
        configuration = new Configuration();  
        configuration.setDefaultEncoding("utf-8");  
        configuration.setClassForTemplateLoading(WordGenerator.class, "/");  
        allTemplates = new HashMap<String, Template>();   // Java 7 钻石语法  
        /*try {  
            allTemplates.put("exportBidPlan", configuration.getTemplate("templates/bidPlan/exportBidPlan.ftl"));  
        } catch (IOException e) {  
            e.printStackTrace();  
            throw new RuntimeException(e);  
        } */ 
    }  
  
    private WordGenerator() {  
        throw new AssertionError();  
    }  
  
    public static File createDoc(Map<?, ?> dataMap, String type, String extName) {  
        String name = "temp" + (int) (Math.random() * 100000) + extName;  
        File f = new File(name);  
        Template t = allTemplates.get(type);  
        try {  
            // 这个地方不能使用FileWriter因为需要指定编码类型否则生成的Word文档会因为有无法识别的编码而无法打开  
            Writer w = new OutputStreamWriter(new FileOutputStream(f), "utf-8");  
            t.process(dataMap, w);  
            w.close();  
        } catch (Exception ex) {  
            ex.printStackTrace();  
            throw new RuntimeException(ex);  
        }  
        return f;  
    }  
    
    /**
     * fileName 文件名称
     * extName 文件后缀 如：.doc、.xls
     * tempName 模板路径
     * map 参数
     * 
     * @throws IOException 
     * @throws ParseException 
     * @throws MalformedTemplateNameException 
     * @throws TemplateNotFoundException 
     * */
    public static void exportDoc(String fileName, String extName,
    		String tempName,
    		Map<String, Object> map, 
    		HttpServletResponse resp) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException {
        if(StringUtil.isEmpty(fileName)) {
        	fileName = "exportTemp";
        }
        // 提示：在调用工具类生成Word文档之前应当检查所有字段是否完整  
        // 否则Freemarker的模板引擎在处理时可能会因为找不到值而报错 这里暂时忽略这个步骤了  
        File file = null;  
        InputStream fin = null;  
        ServletOutputStream out = null;  
        try {  
        	allTemplates.put(tempName, configuration.getTemplate(tempName));  
            // 调用工具类WordGenerator的createDoc方法生成Word文档  
            file = WordGenerator.createDoc(map, tempName, extName);  
            fin = new FileInputStream(file);  
              
            resp.setCharacterEncoding("utf-8");  
            
            //resp.setContentType("application/msword");
            //resp.setContentType("application/msexcel");
            resp.setContentType("application/x-msdownload");  
            // 设置浏览器以下载的方式处理该文件默认名为resume.doc  
            resp.addHeader("Content-Disposition", "attachment;filename="+(new String(fileName.getBytes("utf-8"), "iso-8859-1"))+ extName);  
              
            out = resp.getOutputStream();  
            byte[] buffer = new byte[512];  // 缓冲区  
            int bytesToRead = -1;  
            // 通过循环将读入的Word文件的内容输出到浏览器中  
            while((bytesToRead = fin.read(buffer)) != -1) {  
                out.write(buffer, 0, bytesToRead);  
            }   
        } finally {  
            if(fin != null) fin.close();  
            if(out != null) out.close();  
            if(file != null) file.delete(); // 删除临时文件  
        }  
    }
    
    /**
     * 向指定 URL 上传文件POST方法的请求
     *
     * @param url 发送请求的 URL
     * @param filepath 文件路径
     * @param type 转换类型
     * @return 所代表远程资源的响应结果, json数据
     */
    public static String SubmitPost(String url, String filepath, String type, String htmlTitle) {
        String requestJson = "";
        HttpClient httpclient =  HttpClients.createDefault();
        try {
            HttpPost httppost = new HttpPost(url);
            FileBody file = new FileBody(new File(filepath));
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null,
                    Charset.forName("UTF-8"));
            reqEntity.addPart("file", file); // file为请求后台的File upload;属性
            reqEntity.addPart("convertType", new StringBody(type, Charset.forName("UTF-8")));
            reqEntity.addPart("htmlTitle", new StringBody(htmlTitle, Charset.forName("UTF-8")));
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity resEntity = response.getEntity();
                requestJson = EntityUtils.toString(resEntity);
                EntityUtils.consume(resEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.getConnectionManager().shutdown();
            } catch (Exception ignore) {

            }
        }
        return requestJson;
    }
    
    public static String yl(String fileName, String extName,
    		String tempName,
    		Map<String, Object> map, 
    		HttpServletResponse resp) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException {
    	if(StringUtil.isEmpty(fileName)) {
        	fileName = "printTemp";
        }
        File file = null;  
        InputStream fin = null;  
        ServletOutputStream out = null;  
        try {  
        	allTemplates.put(tempName, configuration.getTemplate(tempName));  
            // 调用工具类WordGenerator的createDoc方法生成Word文档  
            file = WordGenerator.createDoc(map, tempName, extName);  
        } finally {  
            if(fin != null) fin.close();  
            if(out != null) out.close();  
        }
    	//文件上传转换,获取返回数据
        String convertByFile = SubmitPost("http://dcs.yozosoft.com:80/upload", file.getPath(), "0", fileName);
        JSONObject obj = JSONObject.fromObject(convertByFile);
        if ("0".equals(obj.getString("result"))) {// 转换成功
            String urlData = obj.getString("data");
            urlData = urlData.replace("[\"", "");//去掉[
            urlData = urlData.replace("\"]", "");//去掉]
            if(file != null) file.delete(); // 删除临时文件  
            //最后urlData是文件的浏览地址
            return urlData;//打印网络文件预览地址
        } else {// 转换失败
            System.out.println("转换失败");
            if(file != null) file.delete(); // 删除临时文件  
            return null;
        }
    }
   
   
}

