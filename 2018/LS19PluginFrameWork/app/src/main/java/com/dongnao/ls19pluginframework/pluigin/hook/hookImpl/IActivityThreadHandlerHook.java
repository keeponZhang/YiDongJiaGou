package com.dongnao.ls19pluginframework.pluigin.hook.hookImpl;

import android.content.Context;
import android.os.Handler;

import com.dongnao.ls19pluginframework.pluigin.hook.base.BaseClassHandle;
import com.dongnao.ls19pluginframework.pluigin.hook.base.BaseHook;
import com.dongnao.ls19pluginframework.pluigin.hook.base.BaseProxyHook;
import com.dongnao.ls19pluginframework.pluigin.hook.handleImpl.ActivityMH;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2018/4/18.
 */

//这里hook的是handler的callback方法，所以继承BaseHook即可
public class IActivityThreadHandlerHook extends   BaseHook {
    private Context context;

    public IActivityThreadHandlerHook(Context context) {
        this.context = context;
    }

    @Override
    protected BaseClassHandle createHookHandle() {
        return null;
    }

    @Override
    public void onInit(ClassLoader classLoader) {
        try {
            Class<?> forName = Class.forName("android.app.ActivityThread");
            Field currentActivityThreadField = forName.getDeclaredField("sCurrentActivityThread");
            currentActivityThreadField.setAccessible(true);
//            还原系统的ActivityTread   mH
            Object activityThreadObj=currentActivityThreadField.get(null);

            Field handlerField = forName.getDeclaredField("mH");
            handlerField.setAccessible(true);
//            hook点找到了
            Handler mH= (Handler) handlerField.get(activityThreadObj);
            setRealObj(mH);
            Field callbackField = Handler.class.getDeclaredField("mCallback");
            callbackField.setAccessible(true);

            callbackField.set(mH,new ActivityMH(mH));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
