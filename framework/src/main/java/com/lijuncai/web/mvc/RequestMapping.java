package com.lijuncai.web.mvc;

import java.lang.annotation.*;

/**
 * @description: @RequestMapping注解,用于指定映射的uri
 * 作用目标是方法
 * @author: lijuncai
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestMapping {
    String value();//用于保存映射的uri
}
