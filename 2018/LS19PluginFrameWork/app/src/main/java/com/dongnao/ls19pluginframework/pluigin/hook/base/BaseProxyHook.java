package com.dongnao.ls19pluginframework.pluigin.hook.base;

import android.content.Context;
import android.util.Log;

import com.dongnao.ls19pluginframework.pluigin.HookFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by baby on 2018/4/16.
 */

//这个是个InvocationHandler
public  abstract class BaseProxyHook extends  BaseHook implements InvocationHandler {
    // @Override
    // public abstract  void onInit(ClassLoader classLoader);
    protected Context mContext;
    public  BaseProxyHook(Context context){
        mContext= context;
    }
    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
//        分发总站     startActivity（宿主 OneActivity）
        if (!isEnable()) {
            return method.invoke(realObj, args);
        }
//        //策略模式  2  classHandle：IActivityManagerClassHandle
        BaseMethodHandle baseMethodHandle = classHandle.getHookMethodHandler(method.getName());
        // Log.d("TAG", "BaseProxyHook invoke baseMethodHandle:"+baseMethodHandle);
        Log.d("TAG",
                "-----------BaseProxyHook invoke:"+method.getName() +" baseMethodHandle="+baseMethodHandle+" processName="+
                        HookFactory.getProcessName(mContext));
        if (baseMethodHandle != null) {
            return baseMethodHandle.doHookInnner(realObj, method, args);
        }

        return method.invoke(realObj,  args);
    }
}
