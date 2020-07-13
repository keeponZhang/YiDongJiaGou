package com.morgoo.droidplugin.hook.handle;

import android.content.Context;
import android.os.Build;
import android.os.RemoteException;
import android.util.Log;

import com.morgoo.droidplugin.hook.HookedMethodHandler;
import com.morgoo.droidplugin.pm.PluginManager;

import java.lang.reflect.Method;

class ReplaceCallingPackageHookedMethodHandler extends HookedMethodHandler {

    public ReplaceCallingPackageHookedMethodHandler(Context hostContext) {
        super(hostContext);
    }

    @Override
    protected boolean beforeInvoke(Object receiver, Method method, Object[] args) throws Throwable {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            if (args != null && args.length > 0) {
                for (int index = 0; index < args.length; index++) {
                    if (args[index] != null && (args[index] instanceof String)) {
                        String str = ((String) args[index]);

                        /**
                         * 如果发现是插件程序的报名的话，会统一替换成宿主
                         * 为什么？原来当我们的app调用一些系统api的时候
                         * 都会到AppOpsService那边仅此你鉴权
                         * AppOpsService会判断当前的ui和包名是不是匹配，
                         * 如果不匹配会跑一个"Bad call"的SercurityException
                         * 我们启动插件的时候，uid是宿主apk
                         * 包名是插件apk，显然是不匹配的
                         * 因此经过这层转换之后，我们就可以“欺骗”系统
                         * 让其以为是宿主apk调过来的
                         * 当然，这样做也是有副作用的，宿主apk必须把所有插件apk所需要的权限全部申请上，
                         * 因为系统只会检查宿主apk
                         */
                        boolean packagePlugin = isPackagePlugin(str);
                        Log.w("TAG", "ReplaceCallingPackageHookedMethodHandler beforeInvoke " +
                                "method "+method.getName() +
                                "   参数str:"+str+"   hostPackageName():"+mHostContext.getPackageName()+" packagePlugin:"+packagePlugin);
                        if (isPackagePlugin(str)) {
                            args[index] = mHostContext.getPackageName();
                        }
                    }
                }
            }
        }
        return super.beforeInvoke(receiver, method, args);
    }

    private static boolean isPackagePlugin(String packageName) throws RemoteException {
        return PluginManager.getInstance().isPluginPackage(packageName);
    }
}