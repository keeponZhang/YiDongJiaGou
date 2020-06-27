package com.dongnao.ls19pluginframework.pluigin.parser;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.InstrumentationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;

import com.dongnao.ls19pluginframework.pluigin.utils.reflect.MethodUtils;

import java.lang.reflect.Method;
import java.util.HashSet;

/**
 * Created by Administrator on 2018/4/13.
 */

public class PackageParser16 extends PackageParser20 {
    public PackageParser16(Context mContext) throws Exception {
        super(mContext);
        mStopped = false;
        mEnabledState = 0;
    }
    private boolean mStopped;
    private int mEnabledState;
    @Override
    public ActivityInfo generateActivityInfo(Object activity, int flags) throws Exception {
        /*public static final ActivityInfo generateActivityInfo(Activity a, int flags, boolean stopped, int enabledState, int userId)  */
        Method method = MethodUtils.getAccessibleMethod(sPackageParserClass, "generateActivityInfo", sActivityClass, int.class, boolean.class, int.class, int.class);
        return (ActivityInfo) method.invoke(null, activity, flags, mStopped, mEnabledState, mUserId);
    }


    @Override
    public ServiceInfo generateServiceInfo(Object service, int flags) throws Exception {
        /*public static final ServiceInfo generateServiceInfo(Service s, int flags, boolean stopped, int enabledState, int userId)*/
        Method method = MethodUtils.getAccessibleMethod(sPackageParserClass, "generateServiceInfo", sServiceClass, int.class, boolean.class, int.class, int.class);
        return (ServiceInfo) method.invoke(null, service, flags, mStopped, mEnabledState, mUserId);
    }


    @Override
    public ProviderInfo generateProviderInfo(Object provider, int flags) throws Exception {
        /*     public static final ProviderInfo generateProviderInfo(Provider p, int flags, boolean stopped,
            int enabledState, int userId)  */
        Method method = MethodUtils.getAccessibleMethod(sPackageParserClass, "generateProviderInfo", sProviderClass, int.class, boolean.class, int.class, int.class);
        return (ProviderInfo) method.invoke(null, provider, flags, mStopped, mEnabledState, mUserId);
    }

    @Override
    public InstrumentationInfo generateInstrumentationInfo(
            Object instrumentation, int flags) throws Exception {
        /*    public static final InstrumentationInfo generateInstrumentationInfo(
            Instrumentation i, int flags)*/
        return super.generateInstrumentationInfo(instrumentation, flags);
    }

    @Override
    public ApplicationInfo generateApplicationInfo(int flags) throws Exception {
        /*   public static ApplicationInfo generateApplicationInfo(Package p, int flags,
            boolean stopped, int enabledState, int userId) */
        Method method = MethodUtils.getAccessibleMethod(sPackageParserClass, "generateApplicationInfo", mPackage.getClass(), int.class, boolean.class, int.class, int.class);
        return (ApplicationInfo) method.invoke(null, mPackage, flags, mStopped, mEnabledState, mUserId);
    }

    @Override
    public PermissionGroupInfo generatePermissionGroupInfo(
            Object permissionGroup, int flags) throws Exception {
        /*     public static final PermissionGroupInfo generatePermissionGroupInfo(
            PermissionGroup pg, int flags) */
        return super.generatePermissionGroupInfo(permissionGroup, flags);

    }

    @Override
    public PermissionInfo generatePermissionInfo(
            Object permission, int flags) throws Exception {
        /*  public static final PermissionInfo generatePermissionInfo(
            Permission p, int flags) */
        return super.generatePermissionInfo(permission, flags);
    }

    @Override
    public PackageInfo generatePackageInfo(
            int gids[], int flags, long firstInstallTime, long lastUpdateTime,
            HashSet<String> grantedPermissions) throws Exception {
        /*     public static PackageInfo generatePackageInfo(PackageParser.Package p,
            int gids[], int flags, long firstInstallTime, long lastUpdateTime,
            HashSet<String> grantedPermissions, boolean stopped, int enabledState, int userId)*/
        Method method = MethodUtils.getAccessibleMethod(sPackageParserClass, "generatePackageInfo",
                mPackage.getClass(),
                int[].class, int.class, long.class, long.class, HashSet.class, boolean.class, int.class, int.class);
        return (PackageInfo) method.invoke(null, mPackage, gids, flags, firstInstallTime, lastUpdateTime, grantedPermissions, mStopped, mEnabledState, mUserId);
    }
}
