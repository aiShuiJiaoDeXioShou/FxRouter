package lh.router.fn;

import lh.router.Router;

public interface RouteInterceptors {
    boolean interceptor(Router router);
}
