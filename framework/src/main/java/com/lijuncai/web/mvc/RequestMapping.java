package com.lijuncai.web.mvc;

import java.lang.annotation.*;

/**
 * @description: @RequestMappingע��,����ָ��ӳ���uri
 * ����Ŀ���Ƿ���
 * @author: lijuncai
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestMapping {
    String value();//���ڱ���ӳ���uri
}
