package lh.router.entity;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import lh.router.annotation.FXRoute;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ToString
public class Route {


    private FXRoute fxRoute;


    private Class<?> aClass;

    /**
     * 页面的路径
     */
    private String path;

    /**
     * 实例化对象
     */
    private Node node;

    /**
     * 实例化的类名
     */
    private String className;

    /**
     * 如果是父类则触发router属性
     */
    private List<Pane> router;

    /**
     * 判断是不是其他路由的父类
     */
    private Boolean isFather;

    /**
     * 路由分割的字符串集合
     */
    private List<String> paths;

    /**
     * 子路由数组
     */
    private List<String> childList;

}
