package lh.utils;


import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReUtil;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lh.router.RouterView;
import lh.router.annotation.FXRoute;
import lh.router.annotation.FxRouter;
import lh.router.entity.Route;
import lh.router.utils.RouterConfigUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 管理javafx的路由跳转
 * 该类默认支持注解
 * 这里推崇使用单文件实现javafx路由，好吧这个类只是某个版本的备份文件
 */
@Deprecated
public class AbandonRouter {

    /**
     * 创建根画布
     *
     * @param basePackagePath
     */
    private final static RouterView root = new RouterView();
    /**
     * 解析好的所有路由（还未实例化）
     */
    private final List<Route> fxRoutes = new ArrayList<>();

    /**
     * 存放实例化对象，和路由的路径
     */
    @Deprecated
    private final HashMap<String, Node> routerMap = new HashMap<>();

    private final HashMap<String, Route> routers = new HashMap<>();
    /**
     * 扫描指定包下所有与@FXRouter注解相关的路由
     */
    private String basePackagePath;
    /**
     * 当前的路由地址, 初始化地址为/
     *
     * @return
     */
    private String nowPath = "/";

    /**
     * 上一级路由地址
     */
    private String prePath = "/";

    /**
     * 支持通过config自定义路由
     */
    public AbandonRouter(RouterConfigUtil config) {
        // TODO
    }

    public AbandonRouter(String basePackagePath, Stage stage) {
        this.basePackagePath = basePackagePath;
        // 初始化所有的路由
        initAnnotationRouter();
        // 初始化一块干净的画布
        stage.setScene(new Scene(root));
        if (Objects.isNull(isRoute("/")))
            throw new RuntimeException("必须要指定一个主路由！！！\n指定一个组件的路由地址为’/‘");
        // 默认路由路径为/为主路由
        toRoute("/");
    }

    /**
     * 获取前路由
     *
     * @return 返回前路由
     */
    public Route getPreRouter() {
        return routers.get(prePath);
    }

    /**
     * 获取现在的路由
     *
     * @return 返回现在的路由
     */
    public Route getNowRoute() {
        return routers.get(nowPath);
    }

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
        this.prePath = nowPath;
        this.nowPath = path;
        Route route = routers.get(path);
        if (Objects.isNull(route)) throw new RuntimeException("没有该对应路径目标路由");

        // 判断是否符合根路由条件,如果符合根路由条件则直接调用root切换页面
        // 获取前路由里面是否含有@Router
        List<Pane> panes = routers.get(prePath).getRouter();
        // 如果有就进行下一步,没有直接跳过
        if (panes != null)
            if (panes.size() > 0 && !path.equals("/") && routers.get(prePath).getChildList().contains(path)) {
                toChildrenRouter(routers.get(prePath), path);
                return;
            }

        this.setNowRootNode(route.getNode());
    }

    /**
     * 设置Root的当前节点
     */
    private void setNowRootNode(Node node) {
        switchRouterNode(root, node);
    }

    /**
     * 切换所选中的pane类的节点
     *
     * @param root 选中的pane类
     * @param node 选中的节点
     */
    private void switchRouterNode(Pane root, Node node) {
        if (root.getChildren().size() == 0) {
            root.getChildren().add(node);
            return;
        }
        root.getChildren().set(0, node);
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


    /**
     * 通过解析的注解实例化该View
     */
    private void initAnnotationRouter() {
        // 解析所有加上@FxRoute注解的类或者方法
        this.parseFxRouter()
                .serialization();
    }

    /**
     * 序列化路由
     */
    private AbandonRouter serialization() {
        // 装配
        fxRoutes.forEach(fr -> routers.put(fr.getPath(), fr));
        routers.forEach((key, route) -> {
            List<String> toChildren = this.getRouteToChildren(key);
            route.setChildList(toChildren);
        });
        return this;
    }


    /**
     * 解析指定包下的FxRouter注解
     *
     * @return 返回一个当前类的class，和注解本身
     */
    private AbandonRouter parseFxRouter() {
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
                    .setPaths(lh.router.utils.RouterUtils.paths(route.getPath()));

            // 添加装配的信息
            fxRoutes.add(route);
        }
        return this;
    }


    /**
     * 对路由路径实例化顺序进行排序
     */
    private AbandonRouter sortRoutePath() {

        // 判断该路由是一级路由还是二级路由
        // 一路由优先放置
        fxRoutes.sort((o1, o2) -> {

            if (lh.router.utils.RouterUtils.isSimpleRoutePath(o1)) {
                return 1;
            } else {
                int ex = lh.router.utils.RouterUtils.sumPathSymbol(o1) - lh.router.utils.RouterUtils.sumPathSymbol(o2);
                return ex > 0 ? 1 : -1;
            }

        });
        return this;
    }

    /**
     * 解析FxRouteBean 将它放入到指定的map集合里面
     */
    @Deprecated
    private AbandonRouter paresFxRouteBeanToMap() {
        for (Route route : fxRoutes) {

            String path = route.getFxRoute().value();
            Class<?> aClass = route.getAClass();
            // 实例化该aClass
            try {
                Object o = aClass.getDeclaredConstructor().newInstance();
                routerMap.put(path, (Node) o);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        return this;
    }

    /**
     * 获取所有的路由路径，并且返回一个字符串类型的数组 <br/>
     * 此方法不能在标注@FxRoute中使用 <br/>
     * 不能在组件中使用 <br/>
     */
    public Set<String> getRoutePaths() {
        return routers.keySet();
    }

    /**
     * 判断是否具有该路由,如果没有返回false
     */
    public Boolean isRoute(String path) {
        return routers.containsKey(path);
    }

    /**
     * 跳转到上一个路由路径
     */
    public void pre() {
        toRoute(Objects.requireNonNullElse(prePath, "/"));
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

    @NotNull
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

    private void thrower(Class<?> clazz, FXRoute fxRoute) {
        if (!lh.router.utils.RouterUtils.isLegitimate(fxRoute)) {
            throw new RuntimeException("符号解析错误:" +
                    "在解析:" + clazz.getName() + "的时候发生错误" +
                    "\n判断该路由是否为合法路由 " +
                    "\n一个路由必须由/开头，否则为非法路由 " +
                    "\n最后一个字符不能为/,否则为非法路由 " +
                    "\n如果一个/后面还有一个/符号，则说明该路由非法,/后面不能以汉字或者符号作为命名对象\n");
        }
    }

    /**
     * 判断当前路由是否为子路由,返回父代路由 </br>
     * 如果不是子路由,返回值为空
     */
    private Route isNowChild(String path) {
        Set<String> routePaths = getRoutePaths();
        List<String> father = new ArrayList<>();
        for (String routePath : routePaths) {
            if (path.contains(routePath)) {
                father.add(routePath);
            }
        }

        if (father.size() == 0) {
            return null;
        }

        father.sort((o1, o2) -> o1.length() > o2.length() ? -1 : 0);
        return routers.get(father.get(0));
    }


    /**
     * 获取一个路由当前所有的子路由
     */
    private List<String> getRouteToChildren(String path) {
        Set<String> routePaths = getRoutePaths();
        return routePaths.stream().filter(s -> ReUtil.isMatch("^" + path + ".*", s) && !s.equals(path))
                .collect(Collectors.toList());
    }


    /**
     * 判断当前路由是否具有@Router标注<br/>
     * 如果具有该标注,则跳转到该标注所在的路由地址
     */
    private void toChildrenRouter(Route route, String path) {
        List<Pane> panes = route.getRouter();
        if (panes.size() > 0) {
            for (Pane pane : panes) {
                switchRouterNode(pane, routers.get(path).getNode());
            }
        }
    }

    /**
     * 跳转到利用指定字段在子路由中跳转
     *
     * @param route 该子路由的父路由
     * @param path  该子路由的路径
     */
    private void toFieldName(Route route, String path, String fieldName) {
        Class<?> aClass = route.getAClass();
        try {
            Field routerView = aClass.getDeclaredField(fieldName);
            routerView.setAccessible(true);
            Pane pane = (Pane) routerView.get(route.getNode());
            switchRouterNode(pane, routers.get(path).getNode());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
