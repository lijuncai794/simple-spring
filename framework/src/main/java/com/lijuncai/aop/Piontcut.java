package com.lijuncai.aop;

import java.lang.annotation.*;

/**
 * @description @Piontcut,用于标识切入点
 * 作用目标是方法
 * @author: lijuncai
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Piontcut {
}
