package cn.merchain.soul.common.utils.db;

import cn.merchain.soul.common.utils.LogFactory;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
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

    public ResultSet Select(String sql) {
        Connection conn = getConnection();
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            rs = statement.executeQuery(sql);
        } catch (Exception e) {
            log.error("Select from sql server error! errmsg:{}", e);
        }
        return rs;
    }

    public boolean Execute(String sql) {
        Connection conn = getConnection();
        Statement statement = null;
        boolean flag = false;
        try {
            statement = conn.createStatement();
            statement.execute(sql);
            flag = true;
        } catch (Exception e) {
            log.error("Execute sql error! errmsg:{}", e);
        } finally {
            close(statement, conn);
        }
        return flag;
    }

    public void close(Statement statement, Connection conn) {
        try {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            log.error("Close error: " + e.getMessage());
        }
    }
}
