package com.dongnao.ls19pluginframework.pluigin.hook.base;

import java.lang.reflect.InvocationHandler;

/**
 * Created by baby on 2018/4/16.
 */

public abstract class BaseHook  {
//    hook类 ----动态代理  invoke        接口  serListner
//  还原的系统对象
    protected Object realObj;
//分发类
    protected  BaseClassHandle classHandle;

    //    开启关闭
    public boolean isEnable = true;


    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public void setRealObj(Object realObj) {
        this.realObj = realObj;
        classHandle=createHookHandle();
    }
//策略模式  1
    protected abstract BaseClassHandle createHookHandle();

    //一般是这里hook，所以需要classLoader
    public abstract void onInstall(ClassLoader classLoader);
}
