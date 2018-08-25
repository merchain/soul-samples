package cn.merchain.soul.common.utils;

import java.io.*;

public class FileUtil {
    static private FileUtil instance = null;

    private FileUtil() {
    }

    public static FileUtil Instance() {
        if (instance == null) {
            instance = new FileUtil();
        }
        return instance;
    }

    public BufferedReader getReader(String path) throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        return reader;
    }

    public File[] getFiles(String dirPath) {
        File dir = new File(dirPath);
        return dir.listFiles();
    }

    /**
     * @param file
     * @return String
     * @Title: readFile
     * @Description: 文件的读和写
     */
    public static String readFile(File file) {
        if (!file.exists()) {
            return "";
        }
        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            // 获取磁盘的文件
            // File file = new File(fileName);
            // 开始读取磁盘的文件
            fileInputStream = new FileInputStream(file);
            // 创建一个字节流
            inputStreamReader = new InputStreamReader(fileInputStream);
            // 创建一个字节的缓冲流
            bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer buffer = new StringBuffer();
            String string = null;
            while ((string = bufferedReader.readLine()) != null) {
                buffer.append(string + "\n");
            }
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                bufferedReader.close();
                inputStreamReader.close();
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String readFile(String fileName) {
        return readFile(new File(fileName));
    }

    public static void writeFile(String content, String filename)
            throws IOException {
        // 要写入的文件
        File file = new File(filename);
        // 写入流对象
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(file);
            printWriter.print(content);
            printWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (printWriter != null) {
                try {
                    printWriter.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }


    public static void writeFile(String content, String filename, boolean flag)
            throws IOException {
        File file = new File(filename);
        FileWriter fw = new FileWriter(file, flag);
        // 写入流对象
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(fw);
            printWriter.print(content);
            printWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (printWriter != null) {
                try {
                    printWriter.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    public static void appendFile(String content, String filename)
            throws IOException {
        boolean flag = false;
        // 要写入的文件
        File file = new File(filename);
        if (file.exists()) {
            flag = true;
        }
        FileWriter fw = new FileWriter(file, true);
        // 写入流对象
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(fw);
            if (flag) {
                printWriter.print("\r\n");
            }
            printWriter.print(content);
            printWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (printWriter != null) {
                try {
                    printWriter.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    public BufferedWriter getWriter(String path) throws IOException {
        return new BufferedWriter(new FileWriter(path));
    }

}
