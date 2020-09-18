package com.xmdd.algorithmpro.base;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.StrictMode;

import com.xmdd.algorithmpro.myutils.MyDensity;

public class MyApplication extends Application {
    private static MyApplication myApplication;

    public static Context getInstence() {
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        MyDensity.setDensity(this, 375f);

        initFileProvider();


    }

    private void initFileProvider() {
        // 解决7.0以上版本的FileProvider问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }


    public static PackageInfo getAppPackageInfo(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo appInfo = null;
        try {
            appInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {

        }
        return appInfo;
    }
}
