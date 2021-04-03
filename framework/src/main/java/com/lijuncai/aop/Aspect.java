package com.lijuncai.aop;

import java.lang.annotation.*;

/**
 * @description: @Aspect,用于标识需要实现AOP的类
 * 作用目标是类
 * @author: lijuncai
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Aspect {
}
