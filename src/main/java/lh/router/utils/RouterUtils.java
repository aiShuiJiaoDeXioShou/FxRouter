package lh.router.utils;

import cn.hutool.core.util.ReUtil;
import lh.router.annotation.FXRoute;
import lh.router.entity.Route;
import lh.router.fn.InvokeFunction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 路由组件的工具类
 */
public class RouterUtils {

    /**
     * 判断是否为单层路由
     */
    public static Boolean isSimpleRoutePath(Route path) {

        return ReUtil.count("/", path.getFxRoute().value()) == 1;
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
    public static Integer sumPathSymbol(Route path) {
        return ReUtil.count("/",path.getFxRoute().value());
    }

    /**
     * 根据传入的字符串,解析顶层路由和子路由
     */
    public static List<String> paths(String path) {
        // 根据/符号解析路由
        String[] routeArr = path.split("/");
        return Arrays.stream(routeArr)
                .filter(s -> !s.isEmpty())
                .map(s -> "/" + s)
                .collect(Collectors.toList());
    }

    /**
     * 去掉最开始的路径
     */
    public static String delStartPath(String path) {
        return path.substring(path.indexOf("/", 1));
    }


    /**
     * 对路由路径实例化顺序进行排序
     */
    public static List<Route> sortRoutePath(List<Route> routeList) {

        // 判断该路由是一级路由还是二级路由
        // 一路由优先放置
        routeList.sort((r1, r2) -> {

            if (RouterUtils.isSimpleRoutePath(r1)) {
                return 1;
            } else {
                int ex = RouterUtils.sumPathSymbol(r1) - RouterUtils.sumPathSymbol(r2);
                return ex > 0 ? 1 : -1;
            }

        });

        return routeList;
    }

    /**
     * 通过反射调用指定名称的方法
     */
    public static void patchMethod(String name, Object entity) {
        Class<?> aClass = entity.getClass();
        try {
            Method declaredMethod = aClass.getMethod(name);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(entity);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            // 如果接收到错误了说明，该组件没有init函数
        }
    }

    /**
     * 获取指定类，指定注解的方法，并在方法前实现切面 <br/>
     *
     * @param annotation 指定注解
     * @param obj        该类的实例化对象
     * @param args       该方法的传参
     */
    public static void section(
            Object obj,
            Class annotation,
            Object[] args,
            InvokeFunction... invoke) {

        Method[] methods = obj.getClass().getMethods();
        Arrays.stream(methods)
                .peek(method -> method.setAccessible(true))
                .filter(method -> method.getAnnotation(annotation) != null)
                .toList()
                .stream()
                .peek(method -> {
                    try {
                        // 在这些函数前执行，一些特定的函数
                        for (InvokeFunction function : invoke) {
                            function.Invoke();
                        }
                        method.invoke(obj, args);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });


    }

}
