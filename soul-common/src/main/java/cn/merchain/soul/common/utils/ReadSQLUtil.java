package cn.merchain.soul.common.utils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;

public class ReadSQLUtil {

    private static Logger log = Logger.getLogger(ReadSQLUtil.class);

    /* 读取文件内容 */
    public static String ReadSqlFromFile(File fileName) {
        StringBuffer buffer = new StringBuffer();
        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            // 获取磁盘的文件
            // File file = new File(fileName);
            // 开始读取磁盘的文件
            fileInputStream = new FileInputStream(fileName);
            // 创建一个字节流
            inputStreamReader = new InputStreamReader(fileInputStream, "utf-8");
            // 创建一个字节的缓冲流
            bufferedReader = new BufferedReader(inputStreamReader);
            String string = null;
            while ((string = bufferedReader.readLine()) != null) {
                buffer.append(string + "\n");
            }
        } catch (FileNotFoundException e) {
            log.error("Sql file not Found!\n");
            e.printStackTrace();
        } catch (IOException e) {
            log.error("Read sql file IOException!");
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
                inputStreamReader.close();
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buffer.toString();
    }

    /* 重载函数 */
    public static String ReadSqlFromFile(String fileName) {
        return ReadSqlFromFile(new File(fileName));
    }

}
