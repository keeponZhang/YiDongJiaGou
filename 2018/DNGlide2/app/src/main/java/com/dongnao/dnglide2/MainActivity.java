package com.dongnao.dnglide2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.speech.RecognitionService;
import android.support.v7.app.AppCompatActivity;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;

import com.dongnao.dnglide2.glide.Glide;
import com.dongnao.dnglide2.glide.request.RequestOptions;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ImageView mIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIv = findViewById(R.id.iv);
        ImageView iv1 = findViewById(R.id.iv1);
        ImageView iv2 = findViewById(R.id.iv2);


//        new LruCache<String,Bitmap>(10){
//            @Override
//            protected int sizeOf(String key, Bitmap value) {
//                return super.sizeOf(key, value);
//            }
//
//            @Override
//            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
//                oldValue.recycle();
//            }
//        };





        // Glide.with(this).load("/sdcard/main.jpg")
        //         .into(iv1);
        // Glide.with(this).load(new File("/sdcard/okhttp.png")).into(iv2);

    }

    public void toNext(View view) {
        startActivity(new Intent(this, SecondActivity.class));
    }

    public void load(View view) {
        Glide.with(this).load("https://ss1.bdstatic" +
                ".com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2669567003," +
                "3609261574&fm=27&gp=0.jpg22222222asads")
                .apply(new RequestOptions().error(R.drawable.ic_launcher_background).placeholder
                        (R.mipmap.ic_launcher).override(500, 500))
                .into(mIv);
    }
}
