package com.lijuncai.aop;

import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.ShadowMatch;

import java.lang.reflect.Method;

/**
 * @description: Pointcut表达式处理类
 * @author: lijuncai
 **/
public class PointcutProcessor {

    //Pointcut解析器，直接给它赋值上Aspectj的所有表达式，以便支持对众多表达式的解析
    private PointcutParser pointcutParser = PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingContextClassloaderForResolution(
            PointcutParser.getAllSupportedPointcutPrimitives()
    );
    //表达式解析器
    private PointcutExpression pointcutExpression;

    public PointcutProcessor(String expression) {
        this.pointcutExpression = pointcutParser.parsePointcutExpression(expression);
    }

    /**
     * 判断传入的Class是否是Aspect的目标代理类(初筛)
     *
     * @param targetClass Class<?> 目标类
     * @return boolean 是否匹配
     */
    public boolean roughMatches(Class<?> targetClass) {
        return pointcutExpression.couldMatchJoinPointsInType(targetClass);
    }

    /**
     * 判断传入的Method对象是否是Aspect的目标代理方法(精筛)
     *
     * @param method Method 目标方法
     * @return boolean 是否匹配
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
