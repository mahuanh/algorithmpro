package com.xmdd.algorithmpro.base;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MyBeseActivity extends AppCompatActivity {
    private long oldClickTime = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public Activity getActivity() {
        return this;
    }

    @Override
    public void startActivity(Intent intent, @Nullable Bundle options) {
        //一秒内重复点击 return
        if (System.currentTimeMillis() - oldClickTime < 1 * 1000) {
            return;
        }
        oldClickTime = System.currentTimeMillis();
        super.startActivity(intent, options);
    }
}
