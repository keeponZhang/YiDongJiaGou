package com.dongnao.alvin.taopiaopiao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.dongnao.alvin.pluginstand.PayInterfaceBroadcast;

/**
 * Created by baby on 2018/3/30.
 */

public class MyReceiver  extends BroadcastReceiver  implements PayInterfaceBroadcast{


    @Override
    public void attach(Context context) {
        Toast.makeText(context, "-----绑定上下文成功---->", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "-----插件收到广播--->", Toast.LENGTH_SHORT).show();
        Toast.makeText(context, "-----插件收到广播1--->", Toast.LENGTH_SHORT).show();
        Toast.makeText(context, "-----插件收到广播2--->", Toast.LENGTH_SHORT).show();
        Toast.makeText(context, "-----插件收到广播3--->", Toast.LENGTH_SHORT).show();
    }
}
