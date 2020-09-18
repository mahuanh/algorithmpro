package com.xmdd.algorithmpro.myutils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xmdd.algorithmpro.base.MyApplication;
import com.xmdd.algorithmpro.R;


/**
 * 自定义toast
 * https://www.jianshu.com/p/4a37c61697dd
 */
public class MyToast {
    private static Toast mToast;

    public static void showToast( String toastMsg) {
        try {
            if (TextUtils.isEmpty(toastMsg)) {
                return;
            }
            if (mToast != null) {
                mToast.cancel();
                mToast = null;
            }
            mToast = new Ftoast(MyApplication.getInstence(), toastMsg, Toast.LENGTH_SHORT);//自定义
            mToast.show();
        } catch (Exception e) {
            e.printStackTrace();
            //可能在子线程调了这个方法
        }
    }

    //使用自定义toast
    private static class Ftoast extends Toast {

        public Ftoast(Context context, String msg, int duration) {
            super(context);
            LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflate != null;
            @SuppressLint("InflateParams") View root = inflate.inflate(R.layout.my_toast, null);//加载自定义的XML布局
            TextView txtContent = (TextView) root.findViewById(R.id.txtToast);
            txtContent.setText(msg);

            setDuration(duration);
            setView(root); //这是setView。就是你的自定义View
            //必须设置Gravity.FILL_HORIZONTAL 这个选项，布局文件的宽高才会正常显示
            setGravity(Gravity.CENTER | Gravity.FILL_HORIZONTAL, 0, 0); //这是，放着顶部，然后水平放满屏幕
        }

    }
}

