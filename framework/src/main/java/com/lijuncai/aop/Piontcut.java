package com.lijuncai.aop;

import java.lang.annotation.*;

/**
 * @description @Piontcut,���ڱ�ʶ�����
 * ����Ŀ���Ƿ���
 * @author: lijuncai
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Piontcut {
}
