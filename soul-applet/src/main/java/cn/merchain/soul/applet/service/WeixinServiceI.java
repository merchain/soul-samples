package cn.merchain.soul.applet.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

/**
 * @version V1.0
 * @Package: cn.merchain.soul.applet.service
 * @ClassName: WeixinServiceI
 * @Description:
 * @author: taoyouxian
 * @date: Create in 2018-08-13 9:56
 **/
@Component
public interface WeixinServiceI {
    JSONObject doGetStr(String url);

    JSONObject doPostStr(String url, String outStr);

    JSONObject getOpenId(String code);
}
