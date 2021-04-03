package com.lijuncai.beans;

import java.lang.annotation.*;

/**
 * @description: @AutoWired注解,用于标识需要依赖注入的属性
 * 作用目标是类的属性
 * @author: lijuncai
 **/

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoWired {
}
