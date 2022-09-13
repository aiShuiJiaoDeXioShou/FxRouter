package lh.router.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 它能在类和方法上使用,这个方法的返回值类型必须要是继承javafxView
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FXRoute {

    /**
     * 导航的路径名称
     * @return
     */
    String value();

    /**
     * 附带的参数meta
     */
    String[] meta() default {};
}
