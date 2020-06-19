package com.dongnao.alvin.taopiaopiao;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class TaoPiaoPiaoMainActivity extends  BaseActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("TAG", "MainActivity onCreate 加载啦啦:" );
        findViewById(R.id.img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if(that!=null){
                        Toast.makeText(that,"有代理-------->"+that.getClass().getSimpleName(),
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(that,SceondActivity.class));
                        startService(new Intent(that, OneService.class));
                    }else{
                        Toast.makeText(TaoPiaoPiaoMainActivity.this,"无代理-------->",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(TaoPiaoPiaoMainActivity.this,SceondActivity.class));
                        startService(new Intent(TaoPiaoPiaoMainActivity.this, OneService.class));
                    }
               IntentFilter intentFilter = new IntentFilter();
               intentFilter.addAction("com.dongnao.alvin.taopiaopiao.MainActivity");
               registerReceiver(new MyReceiver(), intentFilter);
            }
        });
        findViewById(R.id.sendBroad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent();
               intent.setAction("com.dongnao.alvin.taopiaopiao.MainActivity");
               sendBroadcast(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
