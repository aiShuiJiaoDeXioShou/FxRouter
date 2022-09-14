package lh.router.build;

import cn.hutool.core.util.ClassUtil;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import lh.router.annotation.FXRoute;
import lh.router.annotation.FxRouter;
import lh.router.entity.Route;
import lh.router.utils.RouterUtils;
import lh.utils.CurrentLineInfo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 此类为Router组的构造抽象类，不可被实例化
 * 用于使用注解构造Router
 */
public class RouterAnnotationBuild {


    private static List<Route> routes = new ArrayList<>();
    /**
     * 扫描指定包下所有与@FXRouter注解相关的路由
     */
    private String basePackagePath = CurrentLineInfo.getClassName();

    public RouterAnnotationBuild(String basePackagePath) {
        this.basePackagePath = basePackagePath;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    /**
     * 遍历所有标注的注解，解析到数组中
     *
     * @return List<Roue>
     */
    public RouterAnnotationBuild create() {
        // 包含了该包下以及该子包下所有的类
        Set<Class<?>> classSet = ClassUtil.scanPackage(basePackagePath);
        for (Class<?> clazz : classSet) {
            // 判断是否具有指定的注解
            FXRoute fxRoute = clazz.getAnnotation(FXRoute.class);

            // 如果没有则跳过这一步
            if (Objects.isNull(fxRoute)) continue;

            // 判断该路径是否合法,如果非法则抛出异常
            thrower(clazz, fxRoute);

            // 实例化页面对象,并装配到包装类里面
            Route route = new Route();
            Node note = newPageObject(clazz);

            // 扫描这个clazz里面所有的属性
            List<Field> fxRouter = getFxRouter(clazz);

            // 遍历属性查看是否有FxRouter
            List<Pane> fxRouterArr = findAttrFxRouterValue(note, fxRouter);

            // 将结果装配到包装类里面
            route.setFxRoute(fxRoute)
                    .setPath(fxRoute.value())
                    .setAClass(clazz)
                    .setClassName(clazz.getName())
                    // 设置跳转到的节点
                    .setNode(note)
                    .setRouter(fxRouterArr)
                    .setPaths(RouterUtils.paths(route.getPath()));

            // 添加装配的信息
            routes.add(route);
        }

        return this;
    }


    private void thrower(Class<?> clazz, FXRoute fxRoute) {
        if (!RouterUtils.isLegitimate(fxRoute)) {
            throw new RuntimeException("符号解析错误:" +
                    "在解析:" + clazz.getName() + "的时候发生错误" +
                    "\n判断该路由是否为合法路由 " +
                    "\n一个路由必须由/开头，否则为非法路由 " +
                    "\n最后一个字符不能为/,否则为非法路由 " +
                    "\n如果一个/后面还有一个/符号，则说明该路由非法,/后面不能以汉字或者符号作为命名对象\n");
        }
    }

    private Node newPageObject(Class<?> clazz) {
        Node note;

        try {
            note = (Node) clazz.getDeclaredConstructor()
                    .newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return note;
    }

    /**
     * 判断指定类的属性上是否有@FxRouter注解
     * 返回所有的@FxRouter注解
     */
    private List<Field> getFxRouter(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<Field> fxRouters = Arrays.stream(fields)
                .peek(field -> field.setAccessible(true))
                .filter(field -> field.getAnnotation(FxRouter.class) != null)
                .collect(Collectors.toList());
        return fxRouters;
    }

    private List<Pane> findAttrFxRouterValue(Node note, List<Field> fxRouter) {
        List<Pane> collect = null;
        if (fxRouter.size() != 0) {
            collect = fxRouter.stream()
                    .map(field -> {
                        try {
                            return (Pane) field.get(note);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }).collect(Collectors.toList());
        }
        return collect;
    }

}
