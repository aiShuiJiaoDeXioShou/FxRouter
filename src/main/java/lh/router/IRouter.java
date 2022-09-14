package lh.router;

import lh.router.entity.Route;

import java.util.List;
import java.util.Set;

public interface IRouter {

    /**
     * 获取所有的路由路径，并且返回一个字符串类型的数组 此方法不能在标注@FxRoute中使用 不能在组件中使用
     */
    Set<String> getRoutePaths();

    /**
     * 获取现在的路由
     *
     * @return 返回现在的路由
     */
    Route getNowRoute();

    /**
     * 获取上一个路由路径
     */
    String getPrePath();

    String getNowPath();

    /**
     * 获取前路由
     *
     * @return 返回前路由
     */
    Route getPreRouter();

    /**
     * 判断是否具有该路由,如果没有返回false
     */
    Boolean isRoute(String path);

    /**
     * 跳转到上一个路由路径
     */
    void pre();

    /**
     * 跳转到目标路由
     *
     * @param path 路由地址
     */
    void toRoute(String path);

    /**
     * 获取路由的历史记录
     */
    List<String> history();

    /**
     * 获取上一个路由说传递的参数
     */
    <T> T acceptPackage();

    /**
     * 将指定数据传递给下一个路由，并跳转到指定位置
     */
    <T> void sendPackage(T data);

}
