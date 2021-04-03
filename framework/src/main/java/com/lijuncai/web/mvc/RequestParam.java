package com.lijuncai.web.mvc;

import java.lang.annotation.*;

/**
 * @description: @RequestParamע��,���ڱ�ע�����Ĳ���
 * ����Ŀ���ǲ���
 * @author: lijuncai
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface RequestParam {
    String value();//���ڱ���query string�е�key
}