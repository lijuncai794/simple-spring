package com.lijuncai.beans;

import java.lang.annotation.*;

/**
 * @description: @Bean注解,用于将一个类标识为Bean
 * 作用目标是类
 * @author: lijuncai
 **/

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Bean {
}