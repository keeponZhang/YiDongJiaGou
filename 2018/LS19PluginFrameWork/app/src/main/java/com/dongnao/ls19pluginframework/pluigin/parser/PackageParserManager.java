package com.dongnao.ls19pluginframework.pluigin.parser;

import android.content.Context;
import android.os.Build;

/**
 * Created by Administrator on 2018/4/13.
 */

public class PackageParserManager {
    private static final PackageParserManager ourInstance = new PackageParserManager();

    public static PackageParserManager getInstance() {
        return ourInstance;
    }

    private PackageParserManager() {
    }

    public PackageParser getPluginParser(Context context) throws Exception {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            //sdk大于22的  走这里
            return new PackageParser22(context);
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new PackageParser21(context);//API 21
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH) {
            return new PackageParser20(context);//API 17,18,19,20
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN) {
            return new PackageParser16(context); //API 16
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH && Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            return new PackageParser15(context); //API 14,15
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB &&Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            return new PackageParser15(context); //API 9，10
        } else {
            return new PackageParser15(context); //API 9，10
        }
    }
}
