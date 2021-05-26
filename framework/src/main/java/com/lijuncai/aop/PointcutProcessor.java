package com.lijuncai.aop;

import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.ShadowMatch;

import java.lang.reflect.Method;

/**
 * @description: Pointcut���ʽ������
 * @author: lijuncai
 **/
public class PointcutProcessor {

    //Pointcut��������ֱ�Ӹ�����ֵ��Aspectj�����б��ʽ���Ա�֧�ֶ��ڶ���ʽ�Ľ���
    private PointcutParser pointcutParser = PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingContextClassloaderForResolution(
            PointcutParser.getAllSupportedPointcutPrimitives()
    );
    //���ʽ������
    private PointcutExpression pointcutExpression;

    public PointcutProcessor(String expression) {
        this.pointcutExpression = pointcutParser.parsePointcutExpression(expression);
    }

    /**
     * �жϴ����Class�Ƿ���Aspect��Ŀ�������(��ɸ)
     *
     * @param targetClass Class<?> Ŀ����
     * @return boolean �Ƿ�ƥ��
     */
    public boolean roughMatches(Class<?> targetClass) {
        return pointcutExpression.couldMatchJoinPointsInType(targetClass);
    }

    /**
     * �жϴ����Method�����Ƿ���Aspect��Ŀ�������(��ɸ)
     *
     * @param method Method Ŀ�귽��
     * @return boolean �Ƿ�ƥ��
     */
    public boolean accurateMatches(Method method) {
        ShadowMatch shadowMatch = pointcutExpression.matchesMethodExecution(method);
        if (shadowMatch.alwaysMatches()) {
            return true;
        } else {
            return false;
        }
    }
}
