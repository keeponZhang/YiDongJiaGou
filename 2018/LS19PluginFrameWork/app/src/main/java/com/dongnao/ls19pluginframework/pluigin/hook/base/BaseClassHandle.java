package com.dongnao.ls19pluginframework.pluigin.hook.base;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by baby on 2018/4/16.
 * 分发   hook方法名  ---》处理对应对象
 * 映射  ------分发给谁
 * method  方法级
 */

//这个方法只有一个子类IActivityManagerClassHandle
public abstract class BaseClassHandle {
//    映射关系    startActivity-----    BaseMethodHandle（具体处理startActivity-方法这个对象）
    protected Map<String, BaseMethodHandle> hookMethodHandles = new HashMap<>();
    protected Context mHostContext;

    public BaseClassHandle() {
        init(hookMethodHandles);
    }

    //会在子类控制要hook哪些方法，然后对应的BaseMethodHandle是什么
    protected abstract void init(Map<String, BaseMethodHandle> hookMethodHandles);

    //    分发方法
    public BaseMethodHandle getHookMethodHandler(String methodName) {
        if (methodName != null) {
            return hookMethodHandles.get(methodName);
        }
        return null;
    }
}
