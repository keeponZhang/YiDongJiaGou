// MyApp.aidl
package com.example.baby.binderframwork;

// Declare any non-default types here with import statements
//必须拥有相同的aidl文件，其实这里只有存根有用
interface MyApp {
    String getName();

    String setName(String name);
}
