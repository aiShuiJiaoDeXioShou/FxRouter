package lh.router.utils;

import lh.router.build.RouterAnnotationBuild;

public abstract class RouterCreateFactory {

    private static RouterAnnotationBuild routerAnnotation;

    /**
     * 默认使用注解
     *
     * @return 返回注解构造对象
     */
    public static RouterAnnotationBuild build(String path) {
        if (routerAnnotation == null) routerAnnotation = new RouterAnnotationBuild(path);
        return routerAnnotation;
    }

}
