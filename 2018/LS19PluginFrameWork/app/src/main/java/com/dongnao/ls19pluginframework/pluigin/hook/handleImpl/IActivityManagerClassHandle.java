package com.dongnao.ls19pluginframework.pluigin.hook.handleImpl;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.RemoteException;

import com.dongnao.ls19pluginframework.pluigin.core.PluginManager;
import com.dongnao.ls19pluginframework.pluigin.hook.base.BaseClassHandle;
import com.dongnao.ls19pluginframework.pluigin.hook.base.BaseMethodHandle;

import java.util.Map;

/**
 * Created by baby on 2018/4/16.
 */
//具体分发类
//BaseClassHandle拥有BaseMethodHandle
public class IActivityManagerClassHandle extends BaseClassHandle {
    @Override
    protected void init(Map<String, BaseMethodHandle> hookMethodHandles) {
        //这里放进去的可以通过getHookMethodHandler拿出来
        hookMethodHandles.put("startActivity", new StartActivityMethodHandle(mHostContext));
    }





}
