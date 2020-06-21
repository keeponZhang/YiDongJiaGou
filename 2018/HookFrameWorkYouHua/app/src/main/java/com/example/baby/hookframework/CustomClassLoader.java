package com.example.baby.hookframework;

import dalvik.system.DexClassLoader;

/**
 * Created by Administrator on 2018/4/11.
 */

public class CustomClassLoader extends DexClassLoader {
    public CustomClassLoader(String dexPath, String optimizedDirectory, String libraryPath, ClassLoader parent) {
        super(dexPath, optimizedDirectory, libraryPath, parent);
    }
}
