package cn.merchain.soul.common.metadata;

import cn.merchain.soul.common.utils.db.DBUtil;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Connection;

/**
 * @version V1.0
 * @Package: cn.merchain.soul.common.metadata
 * @ClassName: TestMetadata
 * @Description:
 * @author: taoyouxian
 * @date: Create in 2018-08-09 21:02
 **/
@SpringBootTest(classes = DBUtil.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class TestMetadata {

    private final Logger log = Logger.getLogger(TestMetadata.class);

    @Autowired
    protected DBUtil dbUtil;

    @Test
    public void testGetConnection() {
        Connection conn = dbUtil.getConnection();
        log.info("Connection: " + conn);
        System.out.println("Connection: " + conn);
    }

}
