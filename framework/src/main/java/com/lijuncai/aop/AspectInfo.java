package com.lijuncai.aop;

/**
 * @description: 切面信息包装类
 * @author: lijuncai
 **/
public class AspectInfo {
    private DefaultAspect aspectObject;
    private PointcutProcessor pointcutProcessor;

    public AspectInfo(DefaultAspect aspectObject, PointcutProcessor pointcutProcessor) {
        this.aspectObject = aspectObject;
        this.pointcutProcessor = pointcutProcessor;
    }

    public DefaultAspect getAspectObject() {
        return aspectObject;
    }

    public void setAspectObject(DefaultAspect aspectObject) {
        this.aspectObject = aspectObject;
    }

    public PointcutProcessor getPointcutProcessor() {
        return pointcutProcessor;
    }

    public void setPointcutProcessor(PointcutProcessor pointcutProcessor) {
        this.pointcutProcessor = pointcutProcessor;
    }
}
