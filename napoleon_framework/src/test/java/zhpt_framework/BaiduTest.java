package zhpt_framework;

import org.json.JSONObject;

import com.baidu.aip.speech.AipSpeech;

public class BaiduTest {
	//参数初始化
	public static final String APP_ID = "15419641";
    public static final String API_KEY = "tEen6ZE6A0ZRlLjvtHus2psv";
    public static final String SECRET_KEY = "2yA6q224WSs8WGla0mGTiQgWv6ydpZGd";
    
    public static void main(String[] args) {
    	// 初始化一个AipSpeech
        AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        
        // 调用接口
        String path = "D:\\BaiduTest\\16k.pcm"; // 音频内容：“北京科技馆”
        JSONObject res = client.asr(path, "pcm", 16000, null);
        System.out.println(res.toString(2));
	}
    
}
