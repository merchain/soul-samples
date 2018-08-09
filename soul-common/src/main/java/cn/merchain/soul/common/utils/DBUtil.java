package cn.merchain.soul.common.utils;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//@Component
public class DBUtil {
    private static DBUtil INSTANCE = new DBUtil();

    public static DBUtil Instance() {
        return INSTANCE;
    }

    private Connection connection = null;
    private Log log = LogFactory.Instance().getLog();

    @Value("${metadata.db.url}")
    private String url;
    @Value("${metadata.db.user}")
    private String user;
    @Value("${metadata.db.password}")
    private String pass;
    @Value("${metadata.db.driver}")
    private String driver;

//    private DBUtil() {
//        try {
//
//            Class.forName(driver);
//            this.connection = DriverManager.getConnection(url, user, pass);
//        } catch (Exception e) {
//            log.error("Connection error: " + e.getMessage());
//        }
//    }

    public Connection getConnection() {
        try {
            if (this.connection == null || this.connection.isValid(1000) == false) {
                Class.forName(driver);
                this.connection = DriverManager.getConnection(url, user, pass);
            }
            return this.connection;
        } catch (SQLException e) {
            log.error("Connection error: " + e.getMessage());
            return null;
        } catch (ClassNotFoundException e) {
            log.error("Connection error: " + e.getMessage());
        }
        return this.connection;
    }

    public void close() {
        try {
            if (this.connection != null) {
                this.connection.close();
            }
        } catch (SQLException e) {
            log.error("Close error: " + e.getMessage());
        }
    }
}
