package lh.router.util;

import cn.hutool.core.util.ReUtil;
import lh.router.annotation.FXRoute;
import lh.router.entity.FxRouteBean;

import java.util.ArrayList;

/**
 * 路由组件的工具类
 */
public class RouterUtil {

    /**
     * 判断是否为单层路由
     */
    public static Boolean isSimpleRoutePath(FxRouteBean path) {

        return ReUtil.count("/",path.getFxRoute().value()) == 1;
    }


    /**
     * 判断该路由是否为合法路由
     * 一个路由必须由/开头，否则为非法路由
     * 最后一个字符不能为/,否则为非法路由
     * 如果一个/后面还有一个/符号，则说明该路由非法,/后面不能以汉字或者符号作为命名对象
     */
    public static Boolean isLegitimate(FXRoute fxRoute) {
        String path = fxRoute.value();
        // 不能够包含//-->这个符号,或者\\这个符号
        return !path.contains("//") && !path.contains("\\") && ReUtil.isMatch("^/([\\w,-,/])*$", path);
    }

    /**
     * 判断一共有多少个 / 符号
     */
    public static Integer sumPathSymbol(FxRouteBean path) {
        return ReUtil.count("/",path.getFxRoute().value());
    }

    /**
     * 根据传入的字符串,解析顶层路由和子路由
     */
    public static ArrayList<String> paths() {

        return null;
    }


}
