package com.tenfine.napoleon.framework.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpRequestUtil {
	// 发送请求，处理返回结果。
	public static String httpsRequest(String url, String postOrGet, String content) {
		HttpResponse resp = request(url, postOrGet, content);
		if (resp != null && resp.getStatusLine().getStatusCode() == 200) {
			String result = getStringForResponse(resp);
			return result;
		} else {
			return null;
		}	
	}

	private static HttpResponse request(String url, String method, String content) {
		try {
			HttpUriRequest m = null;
			if (equalsIgnoreCase(method, "GET")) {
				String requesturl = url;
				HttpGet request = new HttpGet(requesturl);
				m = request;
			} else {
				HttpPost post = new HttpPost(url);
				post.setEntity(new StringEntity(content, "UTF-8"));
				m = post;
			}
			HttpResponse response = HttpClients.createDefault().execute(m);
			return response;
		} catch (Exception ex) {
		}
		return null;
	}

	// 比较两个字符串是否相等（忽略大小写）。 只要有一个字符串为null，返回false。
	public static boolean equalsIgnoreCase(String str1, String str2) {
		if (str1 == null || str2 == null) {
			return false;
		}
		return str1.equalsIgnoreCase(str2);
	}

	private static String getStringForResponse(HttpResponse response) {
		try {
			String result = EntityUtils.toString(response.getEntity(), "UTF-8");
			return result;
		} catch (Exception ex) {
		}
		return null;
	}
}
