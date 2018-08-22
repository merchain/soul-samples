package cn.merchain.soul.applet.domain;

import java.util.List;

/**
 * @version V1.0
 * @Package: cn.merchain.soul.applet.domain
 * @ClassName: ChainArgs
 * @Description:
 * @author: taoyouxian
 * @date: Create in 2018-08-17 20:00
 **/
public class ChainArgs {
    private List<String> peers;
    private String fcn;
    private List<String> args;

    public ChainArgs() {
    }

    public ChainArgs(List<String> peers, String fcn, List<String> args) {
        this.peers = peers;
        this.fcn = fcn;
        this.args = args;
    }

    public List<String> getPeers() {
        return peers;
    }

    public void setPeers(List<String> peers) {
        this.peers = peers;
    }

    public String getFcn() {
        return fcn;
    }

    public void setFcn(String fcn) {
        this.fcn = fcn;
    }

    public List<String> getArgs() {
        return args;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "ChainArgs{" +
                "peers=" + peers +
                ", fcn='" + fcn + '\'' +
                ", args=" + args +
                '}';
    }
}
