package com.lijuncai.beans;

import java.lang.annotation.*;

/**
 * @description: @AutoWiredע��,���ڱ�ʶ��Ҫ����ע�������
 * ����Ŀ�����������
 * @author: lijuncai
 **/

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoWired {
}
