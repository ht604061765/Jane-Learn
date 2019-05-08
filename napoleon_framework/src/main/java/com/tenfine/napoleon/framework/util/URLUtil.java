package com.tenfine.napoleon.framework.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.codec.binary.Base64;

public class URLUtil {
	
	public static InputStream getInputStreamByGet(String url) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url)
                    .openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = conn.getInputStream();
                return inputStream;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

	public static String getStreamString(InputStream inputStream){
		if(inputStream != null) {
			try {
				BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));
				StringBuffer stringBuffer = new StringBuffer();
				String tempOneLine = new String("");
				while((tempOneLine = bufferReader.readLine()) != null) {
					stringBuffer.append(tempOneLine);
				}
				return stringBuffer.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null; 
		}
	 public static void saveData(InputStream is, File tempFile) {
		BufferedInputStream bis = new BufferedInputStream(is);
     	BufferedOutputStream bos = null;
	        try {
	            int len;
	            if (!tempFile.getParentFile().exists()) {
	                tempFile.getParentFile().mkdirs();
	            }
	         	bos = new BufferedOutputStream(new FileOutputStream(tempFile.getPath()));
	            byte[] bs = new byte[1024];
	            // 开始读取
	            while ((len = bis.read(bs)) != -1) {
	            	bos.write(bs, 0, len);
	            	bos.flush();
	            }

	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            // 完毕，关闭所有链接
	            try {
	            	bos.close();
	                bis.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }


    /**
     * URL编码，对防止浏览器解释出错或出现乱码.
     * 
     * @param url
     * @return
     */
    public static String encodeSafeURL(String url) {
        try{
            return URLEncoder.encode(url, "utf-8");
        }catch(UnsupportedEncodingException ex){
            return url;
        }
    }

    public static String decodeSafeURL(String url) {
        try{
            return URLDecoder.decode(url, "utf-8");
        }catch(UnsupportedEncodingException ex){
            return url;
        }
    }

    public static String encodeBase64SafeURL(String url) {
        return Base64.encodeBase64URLSafeString(url.getBytes());
    }

    public static String decodeBase64SafeURL(String encode) {
        return new String(Base64.decodeBase64(encode));
    }

    /**
     * 给url添加参数。主要解决问号“？”的问题。
     * 
     * @param url
     * @param params
     *            形如 key=value
     * @return
     */
    public static String urlAddParams(String url, String param) {
        if(url.indexOf("?") >= 0){
            return url + "&" + param;
        }else{
            return url + "?" + param;
        }
    }
    
    
    public static void main(String[] args) {
    }

}



