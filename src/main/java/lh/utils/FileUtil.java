package lh.utils;

import java.io.*;

public class FileUtil {

    /**
     * 获取文件的类型
     */
    public static String getFileType(String path) {

        int lastType = path.lastIndexOf(".");
        String typeName = path.substring(lastType+1);

        return typeName;
    }

    /**
     * 判断是否是指定类型
     */
    public static boolean isFileType(String path,String type) {
        return getFileType(path).equals(type);
    }

    /**
     * 读取指定文件所有的内容
     * return 返回字符串类型的数据
     */
    public static String readToStr(String path) {
        StringBuilder fileDataStr = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String str;
            while ( (str = reader.readLine()) != null) {
                fileDataStr.append(str).append("\n");
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException("未找到该文件！！\n"+e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("读取该行文本失败\n"+e.getMessage());
        }

        return fileDataStr.toString();
    }

    /**
     * 将内容写入到指定文件当中
     */
    public static boolean writeStr(String data,String path) {

        return false;
    }
}
