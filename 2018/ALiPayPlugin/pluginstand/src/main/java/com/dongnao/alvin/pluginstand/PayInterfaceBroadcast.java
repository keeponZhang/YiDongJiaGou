package com.dongnao.alvin.pluginstand;

import android.content.Context;
import android.content.Intent;

/**
 * Created by baby on 2018/3/30.
 */

public interface PayInterfaceBroadcast {

    public void attach(Context context);

    public void onReceive(Context context, Intent intent);
}
