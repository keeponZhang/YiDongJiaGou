package com.example.baby.hookframework;

import android.content.pm.PackageInfo;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2018/4/11.
 */

public class IPackageManagerHandler implements InvocationHandler
{
    private Object mBase;

    public IPackageManagerHandler(Object mBase) {
        this.mBase = mBase;
    }
//PackageInfo  ----》宿主 的包，名
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("getPackageInfo")) {
            if(args!=null&&args.length>0&&args[0].equals("com.dongnao.barry.plugin")){
                Log.e("TAG", "IPackageManagerHandler invoke:" );
                return new PackageInfo();
            }

        }
        return  method.invoke(mBase,args);
    }
}
