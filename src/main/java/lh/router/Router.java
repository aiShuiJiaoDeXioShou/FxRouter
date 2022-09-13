package lh.router;


import cn.hutool.core.util.ClassUtil;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lh.router.annotation.FXRoute;
import lh.router.entity.FxRouteBean;
import lh.router.util.RouterConfigUtil;
import lh.router.util.RouterUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * 管理javafx的路由跳转
 * 该类默认支持注解
 */
public class Router {

    /**
     * 扫描指定包下所有与@FXRouter注解相关的路由
     */
    private String basePackagePath;

    /**
     * 解析好的所有路由（还未实例化）
     */
    private List<FxRouteBean> fxRoutes = new ArrayList<>();

    /**
     * 存放实例化对象，和路由的路径
     */
    private final HashMap<String, Scene> routerMap = new HashMap<>();

    /**
     * 创建根画布
     *
     * @param basePackagePath
     */
    private Stage root;

    /**
     * 当前的路由地址, 初始化地址为/
     *
     * @return
     */
    private String nowPath;

    /**
     * 上一级路由地址
     */
    private String prePath;

    public String getPrePath() {
        return prePath;
    }

    public String getNowPath() {
        return nowPath;
    }

    /**
     * 跳转到目标路由
     *
     * @param path 路由地址
     */
    public void toRoute(String path) {
        prePath = nowPath;
        nowPath = path;
        Scene scene = routerMap.get(path);
        root.setScene(scene);
    }


    /**
     * 支持通过config自定义路由
     */
    public Router(RouterConfigUtil config) {
        // TODO
    }


    public Router(String basePackagePath, Stage byRoot) {
        this.basePackagePath = basePackagePath;
        this.root = byRoot;
        // 初始化所有的路由
        initAnnotationRouter();
        // 默认路由路径为/为主路由
        Scene scene = routerMap.get("/");
        if (Objects.isNull(scene)) throw new RuntimeException("必须要指定一个主路由！！！\n指定一个组件的路由地址为’/‘");
        nowPath = "/";
        root.setScene(scene);
    }

    /**
     * 通过解析的注解实例化该View
     */
    private void initAnnotationRouter() {
        // 解析所有加上@FxRoute注解的类或者方法
        this.parseFxRouter()
                // 对所有路由进行排序
                .sortRoutePath()
                // 将排序好的路由进行解析,并放到一个map集合当中
                .paresFxRouteBeanToMap();
    }

    /**
     * 解析指定包下的FxRouter注解
     *
     * @return 返回一个当前类的class，和注解本身
     */
    private Router parseFxRouter() {
        // 包含了该包下以及该子包下所有的类
        Set<Class<?>> classSet = ClassUtil.scanPackage(basePackagePath);
        for (Class<?> clazz : classSet) {
            // 判断是否具有指定的注解
            FXRoute fxRoute = clazz.getAnnotation(FXRoute.class);
            if (fxRoute != null) {

                // 判断该路径是否合法,如果非法则抛出异常
                if (!RouterUtil.isLegitimate(fxRoute)) {
                    throw new RuntimeException("符号解析错误:" +
                           "在解析:"+ clazz.getName()+"的时候发生错误"+
                            "\n判断该路由是否为合法路由 " +
                            "\n一个路由必须由/开头，否则为非法路由 " +
                            "\n最后一个字符不能为/,否则为非法路由 " +
                            "\n如果一个/后面还有一个/符号，则说明该路由非法,/后面不能以汉字或者符号作为命名对象\n");
                }

                FxRouteBean route = new FxRouteBean();
                route.setFxRoute(fxRoute)
                        .setAClass(clazz)
                        .setClassName(clazz.getName());
                fxRoutes.add(route);
            }
        }
        return this;
    }

    /**
     * 对路由路径实例化顺序进行排序
     */
    private Router sortRoutePath() {

        // 判断该路由是一级路由还是二级路由
        // 一路由优先放置
        fxRoutes.sort((o1, o2) -> {

            if (RouterUtil.isSimpleRoutePath(o1)) {
                return 1;
            } else {
                int ex = RouterUtil.sumPathSymbol(o1) - RouterUtil.sumPathSymbol(o2);
                return ex > 0 ? 1 : -1;
            }

        });
        fxRoutes.forEach(System.out::println);

        return this;
    }

    /**
     * 解析FxRouteBean 将它放入到指定的map集合里面
     */
    private Router paresFxRouteBeanToMap() {
        for (FxRouteBean route : fxRoutes) {

            String path = route.getFxRoute().value();
            Class<?> aClass = route.getAClass();
            // 实例化该aClass
            try {
                Object o = aClass.newInstance();
                routerMap.put(path, new Scene((Parent) o));
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return this;
    }

    /**
     * 解析路由路径,获取路由实例
     */
    private FxRouteBean parsePathToBean(@NotNull String path) {
        // TODO
        return null;
    }

    /**
     * 获取所有的路由路径，并且返回一个字符串类型的数组
     */
    public Set<String> getRoutesPath() {
        Set<String> paths = routerMap.keySet();
        return paths;
    }

    /**
     * 判断是否具有该路由,如果没有返回false
     */
    public Boolean isRoute(String path) {
        return routerMap.containsKey(path);
    }

    /**
     * 跳转到上一个路由路径
     */
    public void pre() {
        if (prePath != null){
            toRoute(prePath);
        }else {
            toRoute("/");
        }
    }

}
