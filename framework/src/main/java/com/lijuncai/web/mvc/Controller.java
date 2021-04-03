package com.lijuncai.web.mvc;

import java.lang.annotation.*;

/**
 * @description: @Controller注解,用于标识控制类
 * 作用目标是类
 * @author: lijuncai
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Controller {
}
