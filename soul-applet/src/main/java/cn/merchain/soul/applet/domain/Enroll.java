package cn.merchain.soul.applet.domain;

/**
 * @version V1.0
 * @Package: cn.merchain.soul.applet.domain
 * @ClassName: Enroll
 * @Description:
 * @author: taoyouxian
 * @date: Create in 2018-08-16 17:26
 **/
public class Enroll {
    private String username;
    private String orgName;

    public Enroll(String username, String orgName) {
        this.username = username;
        this.orgName = orgName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    @Override
    public String toString() {
        return "Enroll{" +
                "username='" + username + '\'' +
                ", orgName='" + orgName + '\'' +
                '}';
    }
}
