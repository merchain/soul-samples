package cn.merchain.soul.applet.service;

import cn.merchain.soul.applet.domain.ChainArgs;
import cn.merchain.soul.applet.domain.Enroll;
import cn.merchain.soul.applet.domain.Peer;
import cn.merchain.soul.applet.service.impl.WeixinServiceImpl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @version V1.0
 * @Package: cn.merchain.soul.applet.service
 * @ClassName: TestWeixinServiceI
 * @Description:
 * @author: taoyouxian
 * @date: Create in 2018-08-13 14:29
 **/
@SpringBootTest(classes = WeixinServiceImpl.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class TestWeixinServiceI {


    private final Logger log = Logger.getLogger(TestWeixinServiceI.class);

    @Autowired
    protected WeixinServiceImpl wxService;

    @Test
    public void testGetOpenId() {
        String code = "1234";
        JSONObject jsonObject = wxService.getOpenId(code);
        log.info("Result: " + jsonObject.toString());
        String errcode = String.valueOf(jsonObject.get("errcode"));
        String errmsg = String.valueOf(jsonObject.get("errmsg"));
        System.out.println("Result: " + errcode + "," + errmsg);
    }


    @Test
    public void testOpenIdEnroll() {
        String openId = "oenTD5NOlXcaRvcLkqT_pzYlOs2g";
        Random random = new Random();
        int randNum = random.nextInt(2) + 1;
        Enroll enroll = new Enroll(openId, "Org" + randNum);
        JSONObject jsonObject = wxService.getTokenByOpenId(enroll);
        log.info("Result: " + jsonObject.toString());
        log.info("Result: " + jsonObject.get("token"));
    }

    @Test
    public void testRandom() {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int randNum = random.nextInt(2) + 1;
            log.info("Result: " + randNum);
        }
    }

    @Test
    public void testString() {
        String avatarUrl = "https://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKoXDjKcngv9B3pw20DmgQX52f8K1omrk0zSmpeVUA97UOHuVkXiavBS7pP8RZyfXuPibkrcMxPibDSQ/132";
        log.info("length: " + avatarUrl.length());
        String trxId = "44d7d10fb295a87000d6e56aaac53acfd94257402dd9fbd41efe1d84bfdac0c7";
        log.info("length: " + trxId.length());
    }

    @Test
    public void testList() {
        List<String> property = Arrays.asList("1", "2", "3", "4");
        String orgName = "org1";
        String peer0 = "peer0." + orgName + ".example.com";
        String peer1 = "peer0." + orgName + ".example.com";
        List<String> peer = Arrays.asList(peer0, peer1);
        Peer peers = new Peer(peer);
        log.info("Peers: " + peers);
        System.out.println("Begin " + peers + " End");

        ChainArgs chainArgs = new ChainArgs(peer, "invoke", property);
        String outStr = JSON.toJSONString(chainArgs);
        System.out.println(outStr);
    }

    @Test
    public void testAddChainByOpenId() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MzQ3ODY2NDAsInVzZXJuYW1lIjoib2VuVEQ1Tk9sWGNhUnZjTGtxVF9wellsT3MyZyIsIm9yZ05hbWUiOiJPcmcxIiwiaWF0IjoxNTM0NzUwNjQwfQ.wToVgdx4MJWL_QkMbGdvk4yhWIKW7PN59RhCky7vCPg";
        String chainArgs = "{\n" +
                "\t\"peers\": [\"peer0.org1.example.com\",\"peer1.org1.example.com\"],\n" +
                "\t\"fcn\":\"invoke\",\n" +
                "\t\"args\":[\"property1\",\"property2\",\"property3\",\"How it is happen 1 1 1.\"]\n" +
                "}";
        String res = wxService.addChainByOpenId(token, chainArgs);
        log.info("result: " + res.length());
    }

    @Test
    public void testAddChainByParam() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MzUwNzUzMDcsInVzZXJuYW1lIjoidGFvIiwib3JnTmFtZSI6Ik9yZzEiLCJpYXQiOjE1MzUwMzkzMDd9.D3NM8XXn79cQM5LacoGTt5dTJtKvRsGqMwkO35sA56k";
        String chainArgs = "{\n" +
                "\t\"args\": [\"1\", \"呐喊\", \"2\", \"让我们红尘作伴活得潇潇洒洒\"],\n" +
                "\t\"fcn\": \"invoke\",\n" +
                "\t\"peers\": [\"peer0.org1.example.com\", \"peer1.org1.example.com\"]\n" +
                "}";
        String res = wxService.addChainByOpenId(token, chainArgs);
        log.info("result: " + res.length());
    }

}
