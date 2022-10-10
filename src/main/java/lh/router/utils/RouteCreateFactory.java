package lh.router.utils;

import lh.router.build.RouteAnnotationBuild;

public abstract class RouteCreateFactory {

    private static RouteAnnotationBuild routerAnnotation;

    /**
     * 默认使用注解
     *
     * @return 返回注解构造对象
     */
    public static RouteAnnotationBuild build(String path) {
        if (routerAnnotation == null) routerAnnotation = new RouteAnnotationBuild(path);
        return routerAnnotation;
    }

}
