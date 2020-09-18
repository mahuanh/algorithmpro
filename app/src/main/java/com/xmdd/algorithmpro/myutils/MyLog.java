
package com.xmdd.algorithmpro.myutils;

import android.text.TextUtils;
import android.util.Log;

public class MyLog {

    private static boolean mEnabled = true;
    private static int mLogLevel = Log.VERBOSE;
    private static String PREFIX = " xmdd - === ";

    public static void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    public static boolean isEnabled() {
        return mEnabled;
    }

    public static void setLogLevel(int level) {
        mLogLevel = level;
    }

    public static int v(String tag, String msg) {
        if (mEnabled && mLogLevel <= Log.VERBOSE && !TextUtils.isEmpty(msg)) {
            return Log.v(PREFIX + tag, msg);
        }
        return 0;
    }

    public static int d(String tag, String msg) {
        if (mEnabled && mLogLevel <= Log.DEBUG && !TextUtils.isEmpty(msg)) {
            return Log.d(PREFIX + tag, msg);
        }
        return 0;
    }

    public static int i(String tag, String msg) {
        if (mEnabled && mLogLevel <= Log.INFO && !TextUtils.isEmpty(msg)) {
            return Log.i(PREFIX + tag, msg);
        }
        return 0;
    }

    public static int info(String msg) {
        if (mEnabled && mLogLevel <= Log.INFO && !TextUtils.isEmpty(msg)) {
            return Log.i(PREFIX + "TAG", msg);
        }
        return 0;
    }

    public static int w(String tag, String msg) {
        if (mEnabled && mLogLevel <= Log.WARN && !TextUtils.isEmpty(msg)) {
            return Log.w(PREFIX + tag, msg);
        }
        return 0;
    }

    public static int w(String tag, String msg, Throwable t) {
        if (mEnabled && mLogLevel <= Log.WARN && !TextUtils.isEmpty(msg)) {
            return Log.w(PREFIX + tag, msg, t);
        }
        return 0;
    }

    public static int e(String tag, String msg) {
        if (mEnabled && mLogLevel <= Log.ERROR && !TextUtils.isEmpty(msg)) {
            return Log.e(PREFIX + tag, msg);
        }
        return 0;
    }

    public static int e(String tag, String msg, Throwable t) {
        if (mEnabled && mLogLevel <= Log.ERROR && !TextUtils.isEmpty(msg)) {
            return Log.e(PREFIX + tag, msg, t);
        }
        return 0;
    }


    /**
     * 打印一个json字符串带换行的方法
     *
     * @param jsonStr
     * @return
     */
    public static String format(String jsonStr) {
        int level = 0;
        StringBuffer jsonForMatStr = new StringBuffer();
        for (int i = 0; i < jsonStr.length(); i++) {
            char c = jsonStr.charAt(i);
            if (level > 0 && '\n' == jsonForMatStr.charAt(jsonForMatStr.length() - 1)) {
                jsonForMatStr.append(getLevelStr(level));
            }
            switch (c) {
                case '{':
                case '[':
                    jsonForMatStr.append(c + "\n");
                    level++;
                    break;
                case ',':
                    jsonForMatStr.append(c + "\n");
                    break;
                case '}':
                case ']':
                    jsonForMatStr.append("\n");
                    level--;
                    jsonForMatStr.append(getLevelStr(level));
                    jsonForMatStr.append(c);
                    break;
                default:
                    jsonForMatStr.append(c);
                    break;
            }
        }
        String[] strArr = jsonForMatStr.toString().split("[\n]");
        for (int i = 0; i < strArr.length; i++) {
            MyLog.i("TAG", "" + strArr[i]);
        }

        return jsonForMatStr.toString();

    }

    private static String getLevelStr(int level) {
        StringBuffer levelStr = new StringBuffer();
        for (int levelI = 0; levelI < level; levelI++) {
            levelStr.append("\t");
        }
        return levelStr.toString();
    }

}
