package com.example.administrator.apt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

@Route(path = "aaaaa")
public class MainActivity extends AppCompatActivity {

    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void test(View view) {
        Log.e("TAG", "MainActivity test:" );
    }
}
