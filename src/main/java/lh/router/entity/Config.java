package lh.router.entity;

import lh.router.Router;
import lh.router.fn.InvokeFunction;
import lh.router.fn.RouteInterceptors;

import java.util.Objects;

public class Config {

    public InvokeFunction Catch;
    private Router router;
    /**
     * 路由拦截器
     */
    private RouteInterceptors[] interceptors;

    public Config(Router router) {
        this.router = router;
    }

    /**
     * 如果没有该路由的处理方法
     *
     * @param aCatch
     * @return
     */
    public Config patchCatch(InvokeFunction aCatch) {
        this.Catch = aCatch;
        return this;
    }

    public Config addInterceptors(RouteInterceptors... interceptors) {
        this.interceptors = interceptors;
        return this;
    }

    public boolean invokeInterceptors() {
        if (Objects.isNull(this.interceptors)) return true;
        for (RouteInterceptors interceptor : this.interceptors) {
            boolean b = interceptor.interceptor(router);
            if (b) {
                continue;
            }
            return false;
        }
        return true;
    }
}
