package cn.merchain.soul.applet.domain;

import java.util.List;

/**
 * @version V1.0
 * @Package: cn.merchain.soul.applet.domain
 * @ClassName: Args
 * @Description:
 * @author: taoyouxian
 * @date: Create in 2018-08-17 20:02
 **/
public class Args {
    private List<String> property;

    public Args() {
    }

    public Args(List<String> property) {
        this.property = property;
    }

    public List<String> getProperty() {
        return property;
    }

    public void setProperty(List<String> property) {
        this.property = property;
    }

    @Override
    public String toString() {
        return "Args{" +
                "property=" + property +
                '}';
    }
}
