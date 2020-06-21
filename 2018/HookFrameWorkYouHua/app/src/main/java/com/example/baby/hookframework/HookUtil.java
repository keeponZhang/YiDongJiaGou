package com.example.baby.hookframework;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.sax.Element;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * Created by baby on 2018/4/2.
 */

public class HookUtil {

    private Context context;

    public  void hookHookMh(Context context  ) {
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
            Field callbackField = Handler.class.getDeclaredField("mCallback");

            callbackField.setAccessible(true);

            callbackField.set(mH,new ActivityMH(mH));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public  void hookStartActivity(Context context) {
//        还原 gDefault 成员变量  反射  调用一次
        this.context = context;
        try {
            Class<?> ActivityManagerNativecls=Class.forName("android.app.ActivityManagerNative");
            Field gDefault = ActivityManagerNativecls.getDeclaredField("gDefault");
            gDefault.setAccessible(true);
//            因为是静态变量  所以获取的到的是系统值  hook   伪hook
            Object defaltValue=gDefault.get(null);
            //mInstance对象
            Class<?> SingletonClass=Class.forName("android.util.Singleton");

            Field mInstance = SingletonClass.getDeclaredField("mInstance");
//        还原 IactivityManager对象  系统对象
            mInstance.setAccessible(true);
            Object iActivityManagerObject=mInstance.get(defaltValue);
            Class<?> IActivityManagerIntercept = Class.forName("android.app.IActivityManager");
            startActivty startActivtyMethod = new startActivty(iActivityManagerObject);
//            第二参数  是即将返回的对象 需要实现那些接口
            Object oldIactivityManager = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader()
                    , new Class[]{IActivityManagerIntercept, View.OnClickListener.class}
                    , startActivtyMethod);
//            将系统的iActivityManager  替换成    自己通过动态代理实现的对象   oldIactivityManager对象  实现了 IActivityManager这个接口的所有方法
            mInstance.set(defaltValue, oldIactivityManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    class ActivityMH implements  Handler.Callback{
        private  Handler mH;

        public ActivityMH(Handler mH) {
            this.mH = mH;
        }

        @Override
        public boolean handleMessage(Message msg) {
//LAUNCH_ACTIVITY ==100 即将要加载一个activity了
            if (msg.what == 100) {
//加工 --完  一定丢给系统  secondActivity  -hook->proxyActivity---hook->    secondeActivtiy   2
                handleLuachActivity(msg);
            }
//做了真正的跳转
            mH.handleMessage(msg);
            return  true;
        }

        private void handleLuachActivity(Message msg) {
//            还原   ActivityClientRecord obj
            Object obj = msg.obj;
            try {
                Field intentField=obj.getClass().getDeclaredField("intent");
                intentField.setAccessible(true);
                //  ProxyActivity   2
                Intent realyIntent = (Intent) intentField.get(obj);
//                sconedActivity  1
                Intent oldIntent = realyIntent.getParcelableExtra("oldIntent");
                if (oldIntent != null) {
                    MyApplication.isPlugin = true;
//                    集中式登录
                    SharedPreferences share = context.getSharedPreferences("david",
                            Context.MODE_PRIVATE);
                    if (share.getBoolean("login",false)) {

//                      登录  还原  把原有的意图    放到realyIntent   SecondeActivity
                        realyIntent.setComponent(oldIntent.getComponent());
                    }else {
                        MyApplication.isPlugin = false;
                        ComponentName componentName = new ComponentName(context,LoginActivity.class);
                        realyIntent.putExtra("extraIntent", oldIntent.getComponent()
                                .getClassName());
                        realyIntent.setComponent(componentName);
                    }
                    realyIntent.setComponent(oldIntent.getComponent());

                    Field activityInfoField= obj.getClass().getDeclaredField("activityInfo");
                    activityInfoField.setAccessible(true);
                    ActivityInfo activityInfo= (ActivityInfo) activityInfoField.get(obj);
//              插件的class  packageName--->loadeApk   系统   第一次 IPackageManager ----》activitry  -——》包名   ---》
//                    不够 IPackageManage.getPackageInfo()
                    activityInfo.applicationInfo.packageName = oldIntent.getPackage() == null ? oldIntent.getComponent().getPackageName()
                            : oldIntent.getPackage();
                    hookPackgeManager();
                }else {
                    MyApplication.isPlugin = false;
                }
//              msg.obj      真实类型   ActivityClientRecord

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    private void hookPackgeManager() {
//          hook   方法  IPackageManager.getPackgeInfo

        // 这一步是因为 initializeJavaContextClassLoader 这个方法内部无意中检查了这个包是否在系统安装
        // 如果没有安装, 直接抛出异常, 这里需要临时Hook掉 PMS, 绕过这个检查.

        Class<?> activityThreadClass = null;
        try {
            activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
            currentActivityThreadMethod.setAccessible(true);
            Object currentActivityThread = currentActivityThreadMethod.invoke(null);

            // 获取ActivityThread里面原始的 sPackageManager
            Field sPackageManagerField = activityThreadClass.getDeclaredField("sPackageManager");
            sPackageManagerField.setAccessible(true);
            Object sPackageManager = sPackageManagerField.get(currentActivityThread);

            Log.i("david", " handleMessage之前发生啦   ");
            // 准备好代理对象, 用来替换原始的对象
            Class<?> iPackageManagerInterface = Class.forName("android.content.pm.IPackageManager");
            Object proxy = Proxy.newProxyInstance(iPackageManagerInterface.getClassLoader()
                    , new Class[]{iPackageManagerInterface},new IPackageManagerHandler(sPackageManager) );
            sPackageManagerField.set(currentActivityThread,proxy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class startActivty implements InvocationHandler {
        private  Object iActivityManagerObject;

        public startActivty(Object iActivityManagerObject) {
            this.iActivityManagerObject = iActivityManagerObject;
        }
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            Log.i("INFO","invoke    "+method.getName());
            if ("startActivity".equals(method.getName())) {
                Log.i("INFO","-----------------startActivity--------------------------");
//瞒天过海
//                寻找传进来的intent
                Intent intent = null;
                int index=0;
                for (int i=0;i<args.length;i++) {
//                    intent
                    Object arg = args[i];
                    if (arg instanceof Intent) {
                        intent = (Intent) args[i];
                        index = i;
                    }
                }
//目的  ---载入acgtivity  将它还原
                Intent newIntent = new Intent();
                ComponentName componentName = new ComponentName(context, ProxyActivity.class);
                newIntent.setComponent(componentName);
//                真实的意图 被我隐藏到了  键值对
                newIntent.putExtra("oldIntent", intent);
                args[index] = newIntent;
            }

            return method.invoke(iActivityManagerObject, args);
        }
    }

//    通过  修改  mPackages   ArrayMap<String, WeakReference<LoadedApk>>
//    LoadedApk ---->插件
//    2   实例化 LoadedApk
//    3   put


    public void putLoadedApk(String path) {
        try {

            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
//            先还原activityThread对象
            Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
            currentActivityThreadMethod.setAccessible(true);
            Object currentActivityThread = currentActivityThreadMethod.invoke(null);

//    再来还原  mPackages  对象
            // 获取到 mPackages 这个静态成员变量, 这里缓存了apk包的信息
            Field mPackagesField = activityThreadClass.getDeclaredField("mPackages");
            mPackagesField.setAccessible(true);
            Map mPackages = (Map) mPackagesField.get(currentActivityThread);

//z找到  getPackageInfoNoCheck   method 方法
            Class<?> compatibilityInfoClass = Class.forName("android.content.res.CompatibilityInfo");
            Method getPackageInfoNoCheckMethod = activityThreadClass.getDeclaredMethod(
                    "getPackageInfoNoCheck", ApplicationInfo.class, compatibilityInfoClass);

//        得到 CompatibilityInfo  里面的  静态成员变量       DEFAULT_COMPATIBILITY_INFO  类型  CompatibilityInfo
            Field defaultCompatibilityInfoField = compatibilityInfoClass.getDeclaredField("DEFAULT_COMPATIBILITY_INFO");
            Object defaultCompatibilityInfo = defaultCompatibilityInfoField.get(null);

            ApplicationInfo applicationInfo = parseReceivers(context, path);

//            一个问题  传参 ApplicationInfo ai 一定是与插件相关    ApplicationInfo----》插件apk文件
//LoadedApk getPackageInfoNoCheck(ApplicationInfo ai, CompatibilityInfo compatInfo)
            Object loadedApk=getPackageInfoNoCheckMethod.invoke(currentActivityThread, applicationInfo, defaultCompatibilityInfo);


            //data/data/宿主/plugin/插件包名/odex
            //applicationInfo.packageNames是插件包名
            String odexPath = Utils.getPluginOptDexDir(applicationInfo.packageName).getPath();
            String libDir = Utils.getPluginLibDir(applicationInfo.packageName).getPath();

            ClassLoader classLoader = new CustomClassLoader(path,odexPath,libDir,context.getClassLoader());
            Field mClassLoaderField = loadedApk.getClass().getDeclaredField("mClassLoader");
            mClassLoaderField.setAccessible(true);
            mClassLoaderField.set(loadedApk,classLoader);
            WeakReference weakReference = new WeakReference(loadedApk);

//     最终目的  是要替换ClassLoader  不是替换LoaderApk
            //这里放进去的是插件的包名
            mPackages.put(applicationInfo.packageName,weakReference);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private ApplicationInfo   parseReceivers(Context context, String path) {

//        Package对象
//        PackageParser pp = new PackageParser();

//        PackageParser.Package  pkg = pp.parsePackage(scanFile, parseFlags);


        try {

            Class   packageParserClass = Class.forName("android.content.pm.PackageParser");
            Method parsePackageMethod = packageParserClass.getDeclaredMethod("parsePackage", File.class, int.class);
            Object packageParser = packageParserClass.newInstance();
            Object packageObj=  parsePackageMethod.invoke(packageParser, new File(path), PackageManager.GET_ACTIVITIES);

//



            Class<?> componentClass = Class.forName("android.content.pm.PackageParser$Component");
            Field intentsField = componentClass.getDeclaredField("intents");


            // 调用generateActivityInfo 方法, 把PackageParser.Activity 转换成
            Class<?> packageParser$ActivityClass = Class.forName("android.content.pm.PackageParser$Activity");
//            generateActivityInfo方法
            Class<?> packageUserStateClass = Class.forName("android.content.pm.PackageUserState");
            Object defaltUserState= packageUserStateClass.newInstance();
            Method generateReceiverInfo = packageParserClass.getDeclaredMethod("generateActivityInfo",
                    packageParser$ActivityClass, int.class, packageUserStateClass, int.class);
            Class<?> userHandler = Class.forName("android.os.UserHandle");
            Method getCallingUserIdMethod = userHandler.getDeclaredMethod("getCallingUserId");
            int userId = (int) getCallingUserIdMethod.invoke(null);
//目的     generateApplicationInfo  方法  生成  ApplicationInfo
            // 需要调用 android.content.pm.PackageParser#generateActivityInfo(android.content.pm.ActivityInfo, int, android.content.pm.PackageUserState, int)
            //      generateApplicationInfo
            Method generateApplicationInfoMethod = packageParserClass.getDeclaredMethod("generateApplicationInfo",
                    packageObj.getClass(),
                    int.class,
                    packageUserStateClass);
            ApplicationInfo applicationInfo= (ApplicationInfo) generateApplicationInfoMethod.invoke(packageParser, packageObj, 0, defaltUserState);

            applicationInfo.sourceDir = path;
            applicationInfo.publicSourceDir = path;
            return applicationInfo;
            //generateActivityInfo
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }




//插件的apk的  dex  -->   Element

    public void injectPluginClass() {
        String cachePath = context.getCacheDir().getAbsolutePath();
        String apkPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/plugin.apk";
        DexClassLoader dexClassLoader = new DexClassLoader(apkPath, cachePath, cachePath, context.getClassLoader());

      //     第一步   找到    插件的Elements数组  dexPathlist  ----？dexElement

        try {
            Class myDexClazzLoader=Class.forName("dalvik.system.BaseDexClassLoader");
            Field  myPathListFiled=myDexClazzLoader.getDeclaredField("pathList");
            myPathListFiled.setAccessible(true);
            Object myPathListObject =myPathListFiled.get(dexClassLoader);


            Class  myPathClazz=myPathListObject.getClass();
            Field  myElementsField = myPathClazz.getDeclaredField("dexElements");
            myElementsField.setAccessible(true);
//          自己插件的  dexElements[]
            Object myElements=myElementsField.get(myPathListObject);

            //     第二步   找到    系统的Elements数组    dexElements
            PathClassLoader pathClassLoader= (PathClassLoader) context.getClassLoader();
            Class baseDexClazzLoader = Class.forName("dalvik.system.BaseDexClassLoader");
            Field pathListFiled = baseDexClazzLoader.getDeclaredField("pathList");
            pathListFiled.setAccessible(true);
            Object pathListObject = pathListFiled.get(pathClassLoader);

            Class systemPathClazz = pathListObject.getClass();
            Field systemElementsField = systemPathClazz.getDeclaredField("dexElements");
            systemElementsField.setAccessible(true);
            //系统的  dexElements[]
            Object systemElements = systemElementsField.get(pathListObject);
            //     第三步  上面的dexElements  数组  合并成新的  dexElements     然后通过反射重新注入系统的Field （dexElements ）变量中

//       新的     Element[] 对象
//            dalvik.system.Element

            int systemLength = Array.getLength(systemElements);
            int myLength = Array.getLength(myElements);
//            找到 Element  的Class类型   数组    每一个成员的类型
            Class<?> sigleElementClazz = systemElements.getClass().getComponentType();
            int newSysteLength = myLength + systemLength;
            Object newElementsArray=Array.newInstance(sigleElementClazz, newSysteLength);
//融合
            for (int i = 0; i < newSysteLength; i++) {
//                先融合 插件的Elements
                if (i < myLength) {
                    Array.set(newElementsArray, i, Array.get(myElements, i));
                }else {
                    Array.set(newElementsArray,i,Array.get(systemElements,i-myLength));
                }
            }
            Field  elementsField=pathListObject.getClass().getDeclaredField("dexElements");;
            elementsField.setAccessible(true);
//            将新生成的EleMents数组对象重新放到系统中去
            elementsField.set( pathListObject,newElementsArray);


        } catch (Exception e) {
            e.printStackTrace();
        }







    }


}
