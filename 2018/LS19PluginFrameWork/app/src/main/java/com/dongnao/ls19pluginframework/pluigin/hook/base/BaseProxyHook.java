package com.dongnao.ls19pluginframework.pluigin.hook.base;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by baby on 2018/4/16.
 */

//这个是个InvocationHandler
public  abstract class BaseProxyHook extends  BaseHook implements InvocationHandler {
    // @Override
    // public abstract  void onInit(ClassLoader classLoader);

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
//        分发总站     startActivity（宿主 OneActivity）
        if (!isEnable()) {
            return method.invoke(realObj, method, args);
        }
//        //策略模式  2  classHandle：IActivityManagerClassHandle
        BaseMethodHandle baseMethodHandle = classHandle.getHookMethodHandler(method.getName());
        if (baseMethodHandle != null) {
            baseMethodHandle.doHookInnner(realObj, method, args);
        }

        return null;
    }
}
