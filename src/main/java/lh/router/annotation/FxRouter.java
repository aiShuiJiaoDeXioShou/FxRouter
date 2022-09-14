package lh.router.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注意:!!此注解只有在@FxRoute下才能生效 <br/>
 * 将一个Pane类型的组件转化为专属的路由组件
 * 使用方法:
 *
 * @FxRouter
 * private RouterView router;
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FxRouter {
}
