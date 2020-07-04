package com.dongnao.ls19pluginframework.pluigin.am;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.dongnao.ls19pluginframework.pluigin.pm.IPluiginManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/18.
 */

public class ActivityManageService {

    private Context context;
    IPluiginManager pluiginManager;
    //key=processName value=ProcessItem
//
    private StaticProcessList mStaticProcessList = new StaticProcessList();
    private RunningProcessList mRunningProcessList = new RunningProcessList();
    private String stubProcessName1;

    public ActivityManageService(Context context) {
        this.context = context;
//        mStaticProcessList代表AndroidMenifest声明
        mStaticProcessList.onCreate(context);
//          mRunningProcessList动态
        mRunningProcessList.setContext(context);

    }

    public void onCreate(IPluiginManager pluiginManager) {
        this.pluiginManager = pluiginManager;
    }
    public ActivityInfo selectStubActivityInfo(int callingPid, int callingUid, ActivityInfo targetInfo)  {
//        targetInfo  插件 ActivityInfo  跳转的Activity
//        android:name=".pluigin.activity.ActivityMode$P02$Standard00"
//        targetInfo 插件ActivtiyInfo
//        :plugin01
        String stubPlugin =mRunningProcessList.getStubProcessByTarget(targetInfo);
//        进程起来了
        if (stubPlugin != null) {
//            静态进程列表 的:plugin03  10个activity
            List<ActivityInfo> stunInfos=mStaticProcessList.getActivityInfoForProcessName(stubPlugin);
            for (ActivityInfo activityInfo : stunInfos) {
//                规则  启动模式一定是一样  stander ---
                if (activityInfo.launchMode == targetInfo.launchMode) {
//                   找到了activityInfo
                    if (activityInfo.launchMode == ActivityInfo.LAUNCH_MULTIPLE) {
                        mRunningProcessList.setTargetProcessName(activityInfo, targetInfo);
                        return activityInfo;
                    } else if (!mRunningProcessList.isStubInfoUsed(activityInfo, targetInfo, stubPlugin)) {
                        mRunningProcessList.setTargetProcessName(activityInfo, targetInfo);
                        return activityInfo;
                    }
//                    activity已经被别人了用       不需      stander  需要1
                }
            }
            return null;
        }
//        进程没起来

        //宿主一共预注册了几个进程
        List<String> stubProcssNames = mStaticProcessList.getProcessNames();
        Log.e("TAG", "ActivityManageService selectStubActivityInfo 一共预注册了:"+ stubProcssNames.size());
        for (String stubProcessName : stubProcssNames) {
            Log.e("TAG", "ActivityManageService selectStubActivityInfo stubProcessName:" +stubProcessName);
            //拿到该进程注册的所有的ActivityInfo
            List<ActivityInfo> stubInfos = mStaticProcessList.getActivityInfoForProcessName(stubProcessName);
            if (!mRunningProcessList.isProcessRunning(stubProcessName)) {
                Log.w("TAG", "ActivityManageService selectStubActivityInfo 进程没有运行:" );
        //进程没有运行 daivd 先一步
                for (ActivityInfo stubInfo : stubInfos) {
                    if (stubInfo.launchMode == targetInfo.launchMode) {
                        if (stubInfo.launchMode == ActivityInfo.LAUNCH_MULTIPLE) {
                            mRunningProcessList.setTargetProcessName(stubInfo, targetInfo);
                            return stubInfo;
                        } else if (!mRunningProcessList.isStubInfoUsed(stubInfo, targetInfo, stubProcessName)) {
                            mRunningProcessList.setTargetProcessName(stubInfo, targetInfo);
//                    stubInfo  自带对应的 process
                            return stubInfo;
                        }
                    }
                }

            }else {
////进程运行  lance 先一步
                if (mRunningProcessList.isProcessRunning(stubProcessName)) {
//              1      lance没有进房间 还在厕所
//               2     lance  没钱被赶出来了      插件  进程被手动清空了
                    if (mRunningProcessList.isPkgEmpty(stubProcessName)) {
                        for (ActivityInfo stubInfo : stubInfos) {
                            if (stubInfo.launchMode == targetInfo.launchMode) {
                                if (stubInfo.launchMode == ActivityInfo.LAUNCH_MULTIPLE) {
                                    mRunningProcessList.setTargetProcessName(stubInfo, targetInfo);
                                    return stubInfo;
                                } else if (!mRunningProcessList.isStubInfoUsed(stubInfo, targetInfo, stubProcessName1)) {
                                    mRunningProcessList.setTargetProcessName(stubInfo, targetInfo);
                                    return stubInfo;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public void onReportMyProcessName(int callingPid, int callingUid, String stubProcessName, String targetProcessName, String targetPkg) {
        mRunningProcessList.setProcessName(callingPid, stubProcessName, targetProcessName, targetPkg);
    }
}
