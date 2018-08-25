package cn.merchain.soul.server;

import cn.merchain.soul.common.utils.ReadSQLUtil;
import cn.merchain.soul.common.utils.db.DBUtil;
import cn.merchain.soul.common.utils.db.TiDB;
import cn.merchain.soul.server.domain.Json;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * @version V1.0
 * @Package: cn.merchain.soul.server
 * @ClassName: TestController
 * @Description:
 * @author: taoyouxian
 * @date: Create in 2018-08-10 13:51
 **/
@SpringBootTest(classes = DBUtil.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class TestController {

    private Logger log = Logger.getLogger(TestController.class);

    @Autowired
    protected DBUtil dbUtil;

    @Test
    public void testAcGetTable() {
        TiDB tiDB = TiDB.getTiDB(dbUtil);
        String res = "";
        Json j = new Json();
        try {
            // 获取sql路径
            String aSqlPath = "F:\\Ubuntu\\blockchain\\soul-samples\\soul-common\\sql\\applet\\user\\Token.txt";
            // 获取sql需要的参数信息
            String aSqlPs = "{}";
            // 获取sql内容
            String aSql = ReadSQLUtil.ReadSqlFromFile(aSqlPath);
            // 将参数转换成键值对
            Map<String, String> aPs = (Map<String, String>) JSON.parse(aSqlPs);
            // 获取结果集
            JSONArray aJson = tiDB.getTable(aSql, aPs, j.getErrorList());
            // 获取拼接结果
            j.setDatas(aJson);
            // 返回状态为1
            j.setState(1);
        } catch (Exception er) {
            j.setMsg(er.getMessage());
            log.info(er.getMessage());
        }
        res = JSON.toJSONString(j);
        log.info("Result: " + res);
    }

    @Test
    public void testAcExecuteSql() {
        TiDB tiDB = TiDB.getTiDB(dbUtil);
        Json j = new Json();
        try {
            // 获取sql路径
            String aSqlPath = "F:\\Ubuntu\\blockchain\\soul-samples\\soul-common\\sql\\applet\\user\\AddToken.txt";
            // 获取sql需要的参数信息
            String aSqlPs = "{\"F_Token\":\"token\",\"F_UserID\":1,\"F_Wxtoken\":\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MzM3NzQ3MDMsInVzZXJuYW1lIjoiQmFycnkiLCJvcmdOYW1lIjoiT3JnMiIsImlhdCI6MTUzMzczODcwM30.DLrFx9frS4m_HQBbJeU81tQME46XSJM30ELIG6ETNrc\"}";
            // 获取sql内容
            String aSql = ReadSQLUtil.ReadSqlFromFile(aSqlPath);
            // 将参数转换成键值对
            Map<String, String> aPs = (Map<String, String>) JSON.parse(aSqlPs);
            // 获取结果集
            boolean flag = tiDB.executeSql(aSql, aPs, j.getErrorList());
            if (flag) {
                // 返回状态为1
                j.setState(1);
            }
        } catch (Exception er) {
            j.setMsg(er.getMessage());
            log.info(er.getMessage());
        }
    }

    @Test
    public void testAcGetId() {
        TiDB tiDB = TiDB.getTiDB(dbUtil);
        int count = 0;
        try {
            String aSql = "Select F_ID from T_Token where openId = '1'";
            count = tiDB.getResult(aSql, null, null);
        } catch (Exception er) {
            log.info(er.getMessage());
        }
        log.info("Result: " + count);
    }

    // acAddChain, user PostMan to send request.

    @Test
    public void testGetPath() {
        File path = null;
        try {
            path = new File(ResourceUtils.getURL("classpath:").getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String gitPath = path.getParentFile().getParentFile().getParent() + File.separator + "logistics" + File.separator + "uploads" + File.separator;
        log.info("Result: " + gitPath);
    }
}
