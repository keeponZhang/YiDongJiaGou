package com.dongnao.ls19pluginframework.pluigin.core;

import dalvik.system.DexClassLoader;

/**
 * Created by Administrator on 2018/4/18.
 */

public class CustomClassLoader extends DexClassLoader {
    public CustomClassLoader(String dexPath, String optimizedDirectory, String libraryPath, ClassLoader parent) {
        super(dexPath, optimizedDirectory, libraryPath, parent);
    }
}
