package com.example.baby.binderframwork;

import android.os.RemoteException;

/**
 * Created by baby on 2018/4/4.
 */

public class MyAppIml extends MyApp.Stub {
    private String name = "小明";
    @Override
    public String getName() throws RemoteException {
        return name;
    }

    @Override
    public String setName(String name) throws RemoteException {
        this.name = name;
        return "";
    }


}
