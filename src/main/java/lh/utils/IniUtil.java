package lh.utils;

import java.io.File;
import java.util.HashMap;

public class IniUtil {

    private String basePath;

    public HashMap<String,Object> pareIni(String basePath)  {

        HashMap<String, Object> config = new HashMap<>();
        // 通过basePath创造文件读取对象
        File file = new File(basePath);
        if (!file.isFile()||FileUtil.isFileType(basePath,"yt")) {
            throw new RuntimeException("这不是可以读取的文件！");
        }
        // 制作一门编程语言，词法分析，语法分析

        return config;
    }

}
