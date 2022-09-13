package lh.utils;

import java.net.URL;

/**
 * 获取根源路径的工具类
 */
public class ClassRootUtil {

    public static String BasePath(String path) {
        URL resource = ClassRootUtil.class.getClassLoader().getResource(path);
        assert resource != null;
        return resource.getPath();
    }

}
