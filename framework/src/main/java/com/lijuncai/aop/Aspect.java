package com.lijuncai.aop;

import java.lang.annotation.*;

/**
 * @description: @Aspect,���ڱ�ʶ��Ҫʵ��AOP����
 * ����Ŀ������
 * @author: lijuncai
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Aspect {
}
