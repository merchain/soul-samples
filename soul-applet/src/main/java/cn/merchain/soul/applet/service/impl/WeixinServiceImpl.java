package cn.merchain.soul.applet.service.impl;

import cn.merchain.soul.applet.domain.ChainArgs;
import cn.merchain.soul.applet.domain.Enroll;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @version V1.0
 * @Package: cn.merchain.soul.applet.service.impl
 * @ClassName: WeixinServiceImpl
 * @Description:
 * @author: taoyouxian
 * @date: Create in 2018-08-13 9:47
 **/

@Component
public class WeixinServiceImpl {
    private Logger log = Logger.getLogger(WeixinServiceImpl.class);

    @Value("${getOpenId.url}")
    private String openIdUrl;
    @Value("${AppID}")
    private String appid;
    @Value("${AppSecret}")
    private String appsecret;

    @Value("${register.url}")
    private String registerUrl;
    @Value("${write.url}")
    private String writeUrl;

    public JSONObject doGetStr(String url) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);// 初始化
        JSONObject jsonObject = null;// 接受结果
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);// 接收执行的结果
            HttpEntity entity = httpResponse.getEntity();// 从消息体里拿结果
            if (entity != null) {
                String result = EntityUtils.toString(entity, "UTF-8");// 将结果转成字符串类型
                jsonObject = JSONObject.parseObject(result);
            }
        } catch (Exception e) {
            log.info("doGetStr error: " + e.getMessage());
        }
        return jsonObject;
    }

    public JSONObject doPostStr(String url, String outStr) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpost = new HttpPost(url);
        JSONObject jsonObject = null;
        try {
            if (outStr.length() > 0)
                httpost.setEntity(new StringEntity(outStr, "UTF-8"));
            httpost.setHeader("Content-type", "application/json");
            httpost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            HttpResponse response = httpClient.execute(httpost);
            String result = EntityUtils.toString(response.getEntity(), "UTF-8");
            jsonObject = JSONObject.parseObject(result);
        } catch (Exception e) {
            log.info("doPostStr error: " + e.getMessage());
        }
        return jsonObject;
    }

    public JSONObject getOpenId(String code) {
        String url = String.format(openIdUrl, appid, appsecret, code);
        JSONObject jsonObject = doPostStr(url, "");
        return jsonObject;
    }

    public JSONObject getTokenByOpenId(Enroll enroll) {
        String outStr = JSON.toJSONString(enroll);
        JSONObject jsonObject = doPostStr(registerUrl, outStr);
        return jsonObject;
    }


    public JSONObject writeDataByOpenId(Enroll enroll) {
        String outStr = JSON.toJSONString(enroll);
        JSONObject jsonObject = doPostStr(writeUrl, outStr);
        return jsonObject;
    }

    public String addChainByOpenId(String token, ChainArgs chainArgs) {
        String outStr = JSON.toJSONString(chainArgs);
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpost = new HttpPost(writeUrl);
        String result = null;
        try {
            httpost.setEntity(new StringEntity(outStr, "UTF-8"));
            httpost.setHeader("Authorization", "Bearer " + token);
            httpost.setHeader("Content-Type", "application/json");
            HttpResponse response = httpClient.execute(httpost);
            result = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            log.info("doPostStr error: " + e.getMessage());
        }
        return result;
    }

    public String addChainByOpenId(String token, String chainArgs) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpost = new HttpPost(writeUrl);
        String result = null;
        try {
            httpost.setEntity(new StringEntity(chainArgs, "UTF-8"));
            httpost.setHeader("Authorization", "Bearer " + token);
            httpost.setHeader("Content-Type", "application/json");
            httpost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            HttpResponse response = httpClient.execute(httpost);
            result = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            log.info("doPostStr error: " + e.getMessage());
        }
        return result;
    }
}
