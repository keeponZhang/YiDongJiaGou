package com.dongnao.ls19pluginframework.pluigin.hook.handleImpl;

import android.content.Context;
import android.content.pm.PackageInfo;

import com.dongnao.ls19pluginframework.pluigin.hook.base.BaseClassHandle;
import com.dongnao.ls19pluginframework.pluigin.hook.base.BaseMethodHandle;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/18.
 */

public class IPackageManagerClassHandle  extends BaseClassHandle {
    @Override
    protected void init(Map<String, BaseMethodHandle> hookMethodHandles) {
        hookMethodHandles.put("getPackageInfo", new GetPackageInfoMethodHandle(mHostContext));
    }

//
private static class GetPackageInfoMethodHandle extends BaseMethodHandle {
    public GetPackageInfoMethodHandle(Context mHostContext) {
        super(mHostContext);
    }

    @Override
    protected boolean beforeInvoke(Object receiver, Method method, Object[] args) {
//        返回 invoke
        PackageInfo packageInfo = new PackageInfo();
        setUserMyResult(packageInfo);
//
        return true;
    }
}
}
