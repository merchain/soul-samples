package cn.merchain.soul.applet.domain;

import java.util.List;

/**
 * @version V1.0
 * @Package: cn.merchain.soul.applet.domain
 * @ClassName: Peer
 * @Description:
 * @author: taoyouxian
 * @date: Create in 2018-08-17 20:03
 **/
public class Peer {
    private List<String> peer;

    public Peer() {
    }

    public Peer(List<String> peer) {
        this.peer = peer;
    }

    public List<String> getPeer() {
        return peer;
    }

    public void setPeer(List<String> peer) {
        this.peer = peer;
    }
}
