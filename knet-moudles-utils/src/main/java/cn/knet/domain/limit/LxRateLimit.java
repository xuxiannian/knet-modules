package cn.knet.domain.limit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LxRateLimit {

    //资源名称
    String name();

    //限制每秒访问次数，默认为3次
    double max() default 3;


    long time() default 60000;


}
