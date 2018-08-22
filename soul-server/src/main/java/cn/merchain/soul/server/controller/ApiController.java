package cn.merchain.soul.server.controller;

import cn.merchain.soul.applet.domain.ChainArgs;
import cn.merchain.soul.applet.domain.Enroll;
import cn.merchain.soul.applet.service.impl.WeixinServiceImpl;
import cn.merchain.soul.common.utils.DateUtil;
import cn.merchain.soul.common.utils.ReadSQLUtil;
import cn.merchain.soul.common.utils.SQLTemplate;
import cn.merchain.soul.common.utils.db.DBUtil;
import cn.merchain.soul.common.utils.db.TiDB;
import cn.merchain.soul.server.domain.Json;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class ApiController {
    private Logger log = Logger.getLogger(ApiController.class);

    @Value("${upload}")
    private String uploadDir;
    @Value("${res.path}")
    private String resPath;

    @Autowired
    protected DBUtil dbUtil;
    @Autowired
    protected WeixinServiceImpl wxService;

    protected TiDB tiDB = null;

    @RequestMapping(value = "/")
    public String getAction() {
        String time = DateUtil.formatTime(System.currentTimeMillis());
        Json j = new Json();
        j.setDatas(time);
        j.setState(1);
        String res = JSON.toJSONString(j);
        System.out.println("Time is: " + DateUtil.formatTime(System.currentTimeMillis()));
        return res;
    }

    @RequestMapping(value = "/acGetConn")
    public String acGetConn() {
        String time = DateUtil.formatTime(System.currentTimeMillis());
        Json j = new Json();
        String conn = dbUtil.getConnection().toString();
        j.setDatas(time + ", " + conn);
        j.setState(1);
        String res = JSON.toJSONString(j);
        System.out.println("Time is: " + DateUtil.formatTime(System.currentTimeMillis()));
        return res;
    }

    @RequestMapping(value = "acGetTable")
    public String acGetTable(@RequestParam("Path") String aSqlPath, @RequestParam("Ps") String aSqlPs) {
        String res = "";
        tiDB = TiDB.getTiDB(dbUtil);
        Json j = new Json();
        try {
            String aSql = ReadSQLUtil.ReadSqlFromFile(resPath + aSqlPath);
            Map<String, String> aPs = (Map<String, String>) JSON.parse(aSqlPs);
            JSONArray aJson = tiDB.getTable(aSql, aPs, j.getErrorList());
            j.setDatas(aJson);
            j.setState(1);
        } catch (Exception er) {
            j.setMsg(er.getMessage());
            log.info(er.getMessage());
        }
        res = JSON.toJSONString(j);
        return res;
    }

    @RequestMapping(value = "acExecuteSql")
    public String acExecuteSql(@RequestParam("Path") String aSqlPath, @RequestParam("Ps") String aSqlPs) {
        String res = "";
        tiDB = TiDB.getTiDB(dbUtil);
        Json j = new Json();
        try {
            String aSql = ReadSQLUtil.ReadSqlFromFile(resPath + aSqlPath);
            Map<String, String> aPs = (Map<String, String>) JSON.parse(aSqlPs);
            boolean flag = tiDB.executeSql(aSql, aPs, j.getErrorList());
            if (flag) {
                j.setState(1);
            }
        } catch (Exception er) {
            j.setMsg(er.getMessage());
            log.info(er.getMessage());
        }
        res = JSON.toJSONString(j);
        return res;
    }

    @RequestMapping(value = "acAddUser")
    public String acAddUser(@RequestParam("Path") String aSqlPaths, @RequestParam("Ps") String aSqlPs) {
        String res = "";
        tiDB = TiDB.getTiDB(dbUtil);
        Json j = new Json();
        try {
            String[] aTableList = aSqlPaths.split(",");
            Map<String, String> aPs = (Map<String, String>) JSON.parse(aSqlPs);
            String aGetSql = ReadSQLUtil.ReadSqlFromFile(resPath + aTableList[0]);
            String aExecuteSql = ReadSQLUtil.ReadSqlFromFile(resPath + aTableList[1]);
            int count = tiDB.getResult(aGetSql, aPs, j.getErrorList());
            boolean flag = false;
            if (count > 0) {
                flag = tiDB.executeSql(aExecuteSql, aPs, j.getErrorList());

                // register chain user
                Thread t = new Thread(() -> enrollUser(aPs));
                t.start();
            } else {
                j.setState(-1);
            }
            if (flag) {
                j.setState(1);
            }
        } catch (Exception er) {
            j.setMsg(er.getMessage());
            log.info(er.getMessage());
        }
        res = JSON.toJSONString(j);
        return res;
    }

    @RequestMapping(value = "acGetOpenId")
    public String acGetOpenId(@RequestParam("code") String code) {
        String res = "";
        Json j = new Json();
        try {
            JSONObject jsonObject = wxService.getOpenId(code);
            String openId = String.valueOf(jsonObject.get("openid"));
            if (!openId.equals("null")) {
                // 返回状态为1
                j.setState(1);
                j.setDatas(openId);
//                String session_key = String.valueOf(jsonObject.get("session_key"));
                log.info("openId: " + openId);
//                log.info("session_key: " + session_key);

                // insert openId
                Thread t = new Thread(() -> addUserByOpenId(openId));
                t.start();
            } else {
                String errcode = String.valueOf(jsonObject.get("errcode"));
                String errmsg = String.valueOf(jsonObject.get("errmsg"));
                j.setMsg(errcode + ", " + errmsg);
                log.info("msg: " + j.getMsg());
            }

        } catch (Exception er) {
            j.setMsg(er.getMessage());
            log.info(er.getMessage());
        }
        res = JSON.toJSONString(j);
        return res;
    }

    @RequestMapping(value = "acAddChain")
    public String acAddChain(@RequestParam("openId") String openId, @RequestParam("tag") String tag, @RequestParam("data") String data) {
        String res = "";
        Json j = new Json();
        try {
            if (!openId.equals("null")) {
                tiDB = TiDB.getTiDB(dbUtil);
                String aGetSql = SQLTemplate.selectTokenByOpenId(openId);
                String result = tiDB.getResultByParams(aGetSql, null, 2, null);
                boolean flag = false;
                String time = DateUtil.formatTime(new Date());
                if (result.length() > 0) {
                    String[] split = result.split(",");
                    String token = split[0];
                    String orgName = split[1].toLowerCase();
                    List<String> property = Arrays.asList(openId, tag, time, data);
                    Thread t = new Thread(() -> addChainByArgs(openId, token, orgName, property));
                    t.start();
                    String insertChainSql = SQLTemplate.insertChainByOpenId(openId, tag, time, data);
                    flag = tiDB.executeSql(insertChainSql, null);
                    if (flag) {
                        j.setState(1);
                    }
                    log.info("acAddChain: " + openId + " " + flag);
                } else {
                    log.info("acAddChain: " + openId + " " + flag);
                }

            } else {
                log.info("msg: " + j.getMsg());
            }
        } catch (Exception er) {
            j.setMsg(er.getMessage());
            log.info(er.getMessage());
        }
        res = JSON.toJSONString(j);
        return res;
    }

    private void addChainByArgs(String openId, String token, String orgName, List<String> property) {
//        Args args = new Args(property);
        String peer0 = "peer0." + orgName + ".example.com";
        String peer1 = "peer1." + orgName + ".example.com";
        List<String> peer = Arrays.asList(peer0, peer1);
//        Peer peers = new Peer(peer);
        ChainArgs chainArgs = new ChainArgs(peer, "invoke", property);
        String trxId = wxService.addChainByOpenId(token, chainArgs);
        if (trxId.length() == 64) {
            // update the chain flag
            String updateSql = SQLTemplate.updateChainByOpenId(openId, trxId);
            boolean flag = tiDB.executeSql(updateSql, null);
            log.info("addChainByArgs: " + openId + "," + flag);
        } else {
            // flag = 0
            log.info("addChainByArgs: " + openId + "," + 0);
        }
    }

    private void addUserByOpenId(String openId) {
        tiDB = TiDB.getTiDB(dbUtil);
        String aGetSql = SQLTemplate.selectOpenId(openId);
        int count = tiDB.getResult(aGetSql, null, null);
        boolean flag = false;
        if (count == 0) {
            String insertTokenSql = SQLTemplate.insertTokenByOpenId(openId);
            flag = tiDB.executeSql(insertTokenSql, null);
            log.info("addUserByOpenId: " + openId + " " + flag);
        } else {
            log.info("addUserByOpenId: " + openId + " " + flag);
        }
    }

    /**
     * @ClassName: ApiController
     * @Title:
     * @Description: register chain user
     * @param:
     * @author: taoyouxian
     * @date: 18:30 2018/8/16
     */
    public void enrollUser(Map<String, String> aPs) {
        tiDB = TiDB.getTiDB(dbUtil);
        String openId = aPs.get("openId");
        Random random = new Random();
        int randNum = random.nextInt(2) + 1;
        String orgName = "Org" + randNum;
        Enroll enroll = new Enroll(openId, orgName);
        JSONObject tokenObject = wxService.getTokenByOpenId(enroll);
        String token = tokenObject.get("token").toString();
        // openId, token, orgName
        String updateTokenSql = SQLTemplate.updateTokenByOpenId(openId, token, orgName);
        boolean flag = tiDB.executeSql(updateTokenSql, null);
        log.info("updateTokenByOpenId: " + openId + "," + token + "," + orgName + " " + flag);
    }
}
