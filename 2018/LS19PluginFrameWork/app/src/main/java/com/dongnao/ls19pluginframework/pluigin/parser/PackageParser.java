package com.dongnao.ls19pluginframework.pluigin.parser;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.InstrumentationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.Signature;

import java.io.File;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Administrator on 2018/4/13.
 */

public abstract class PackageParser {
//    外包
    protected Object mPackageParser;
    protected Context mContext;

    public PackageParser(Context mContext) {
        this.mContext = mContext;
    }

    public abstract void parsePackage(File packageFile, int flags) throws Exception;

    public abstract ActivityInfo generateActivityInfo(Object activity, int flags) throws Exception;

    public abstract ServiceInfo generateServiceInfo(Object service, int flags) throws Exception;

    public abstract ProviderInfo generateProviderInfo(Object provider, int flags) throws Exception;

    public abstract InstrumentationInfo generateInstrumentationInfo(Object instrumentation, int flags) throws Exception;

    public abstract ApplicationInfo generateApplicationInfo(int flags) throws Exception;

    public abstract PermissionGroupInfo generatePermissionGroupInfo(Object permissionGroup, int flags) throws Exception;

    public abstract PermissionInfo generatePermissionInfo(Object permission, int flags) throws Exception;

    public abstract PackageInfo generatePackageInfo(int gids[], int flags, long firstInstallTime, long lastUpdateTime, HashSet<String> grantedPermissions) throws Exception;



    public abstract List getActivities() throws Exception;

    public abstract List getServices() throws Exception;

    public abstract List getProviders() throws Exception;

    public abstract List getPermissions() throws Exception;

    public abstract List getPermissionGroups() throws Exception;

    public abstract List getRequestedPermissions() throws Exception;

    public abstract List getReceivers() throws Exception;

    public abstract List getInstrumentations() throws Exception;


    public abstract String getPackageName() throws Exception;

    public abstract String readNameFromComponent(Object data) throws Exception;

    public abstract List<IntentFilter> readIntentFilterFromComponent(Object data) throws Exception;

    public abstract void writeSignature(Signature[] signatures) throws Exception;

    public abstract void collectCertificates(int flags) throws Exception;

}
