package cn.merchain.soul.common.utils;

/**
 * @version V1.0
 * @Package: cn.merchain.soul.common.utils
 * @ClassName: SQLTemplate
 * @Description:
 * @author: taoyouxian
 * @date: Create in 2018-08-16 18:19
 **/
public class SQLTemplate {

    public static String selectOpenId(String openId) {
        return "Select count(*) from T_Token where openId = '" + openId + "'";
    }

    public static String insertTokenByOpenId(String openId) {
        return "Insert into T_Token(openId) values('" + openId + "')";
    }

    public static String updateTokenByOpenId(String openId, String token, String orgName) {
        return String.format("Update T_Token set token = '%s', orgName = '%s'  WHERE openId = '%s';", token, orgName, openId);
    }

    public static String selectTokenByOpenId(String openId) {
        return "Select token, orgName from T_Token where openId = '" + openId + "'";
    }

    public static String insertChainByOpenId(String openId, String tag, String time, String data) {
        return String.format("Insert into T_Chain(username, tag, createTm, data) values('%s', '%s', '%s', '%s')", openId, tag, time, data);
    }

    public static String updateChainByOpenId(String openId, String trxId) {
        return String.format("Update T_Chain set TRX_ID = '" + trxId + "', flag = 1 where username = '" + openId + "'");
    }
}
