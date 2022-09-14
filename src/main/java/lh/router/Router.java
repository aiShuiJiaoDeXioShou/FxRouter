package lh.router;


import cn.hutool.core.util.ReUtil;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lh.router.entity.Route;
import lh.router.utils.RouterConfigUtil;
import lh.router.utils.RouterCreateFactory;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 管理javafx的路由跳转
 * 该类默认支持注解
 */
public class Router implements IRouter {

    /**
     * 序列化之后的路由组
     */
    private final HashMap<String, Route> myRouters = new HashMap<>();

    /**
     * 创建根画布
     *
     * @param basePackagePath
     */
    private final static RouterView root = new RouterView();

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
    public Router(RouterConfigUtil config) {
        // TODO
    }

    public Router(String basePackagePath, Stage stage) {

        // 初始化所有的路由,使用路由构造工厂
        List<Route> routeList = RouterCreateFactory
                .build(basePackagePath)
                .create()
                .getRoutes();

        // 序列化原始路由
        this.serialization(routeList, myRouters);

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
        return myRouters.get(prePath);
    }

    /**
     * 获取现在的路由
     * @return 返回现在的路由
     */
    public Route getNowRoute() {
        return myRouters.get(nowPath);
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
        Route route = myRouters.get(path);
        if (Objects.isNull(route)) throw new RuntimeException("没有该对应路径目标路由");

        // 判断是否符合根路由条件,如果符合根路由条件则直接调用root切换页面
        // 获取前路由里面是否含有@Router
        List<Pane> panes = myRouters.get(prePath).getRouter();
        // 如果有就进行下一步,没有直接跳过
        if (panes != null)
            if (panes.size() > 0 && !path.equals("/") && myRouters.get(prePath).getChildList().contains(path)) {
                toChildrenRouter(myRouters.get(prePath), path);
                return;
            }

        this.setNowRootNode(route.getNode());
    }

    @Override
    public List<String> history() {
        // TODO
        return null;
    }

    @Override
    public <T> T acceptPackage() {
        // TODO
        return null;
    }

    @Override
    public <T> void sendPackage(T data) {
        // TODO
    }

    /**
     * 获取所有的路由路径，并且返回一个字符串类型的数组 <br/>
     * 此方法不能在标注@FxRoute中使用 <br/>
     * 不能在组件中使用 <br/>
     */
    public Set<String> getRoutePaths() {
        return myRouters.keySet();
    }

    /**
     * 判断是否具有该路由,如果没有返回false
     */
    public Boolean isRoute(String path) {
        return myRouters.containsKey(path);
    }

    /**
     * 跳转到上一个路由路径
     */
    public void pre() {
        toRoute(Objects.requireNonNullElse(prePath, "/"));
    }


    /**
     * 设置Root的当前节点
     */
    private void setNowRootNode(Node node) {
        switchRouterNode(root, node);
    }

    /**
     * 切换所选中的pane类的节点
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
     * 序列化路由
     */
    private Router serialization(List<Route> routeList, Map<String, Route> mapRouters) {
        // 装配
        routeList.forEach(fr -> mapRouters.put(fr.getPath(), fr));
        mapRouters.forEach((key, route) -> {
            List<String> toChildren = this.getRouteToChildren(key);
            route.setChildList(toChildren);
        });
        return this;
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
        return myRouters.get(father.get(0));
    }

    /**
     * 获取一个路由当前所有的子路由
     */
    private List<String> getRouteToChildren(String path) {
        Set<String> routePaths = getRoutePaths();
        return routePaths.stream().filter(s ->ReUtil.isMatch("^"+path+".*",s)&&!s.equals(path))
                .collect(Collectors.toList());
    }


    /**
     * 判断当前路由是否具有@Router标注<br/>
     * 如果具有该标注,则跳转到该标注所在的路由地址
     */
    private void toChildrenRouter(Route route,String path) {
        List<Pane> panes = route.getRouter();
        if (panes.size() > 0) {
            for (Pane pane : panes) {
                switchRouterNode(pane, myRouters.get(path).getNode());
            }
        }
    }

    /**
     * 跳转到利用指定字段在子路由中跳转
     * @param route 该子路由的父路由
     * @param path 该子路由的路径
     */
    private void toFieldName(Route route,String path, String fieldName) {
        Class<?> aClass = route.getAClass();
        try {
            Field routerView = aClass.getDeclaredField(fieldName);
            routerView.setAccessible(true);
            Pane pane = (Pane) routerView.get(route.getNode());
            switchRouterNode(pane, myRouters.get(path).getNode());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
