package com.lijuncai.web.mvc;

import java.lang.annotation.*;

/**
 * @description: @RequestParam注解,用于标注方法的参数
 * 作用目标是参数
 * @author: lijuncai
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface RequestParam {
    String value();//用于保存query string中的key
}