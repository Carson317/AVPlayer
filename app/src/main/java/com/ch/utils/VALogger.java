package com.ch.utils;

import android.util.Log;

/**
 * Created by heng.cao on 8/12/16.
 */
public class VALogger {
    private static String TAG = "VALogger-CH";
    public VALogger(){

    }
    public static void Info(String strMsg){
        Log.i(TAG, getFunctionName() + strMsg);
    }

    public static void Error(String strMsg){
        Log.e(TAG,Log.getStackTraceString(new Exception()).toString() + strMsg);
    }

    public static void Warning(String strMsg){
        Log.w(TAG, getFunctionName() + strMsg);
    }
    public static void Debug(String strMsg){
        Log.d(TAG, getFunctionName() + strMsg);
    }

    private static String getFunctionName() {
        StackTraceElement[] var0 = Thread.currentThread().getStackTrace();
        return var0[4].getClassName() + ":" + var0[4].getMethodName() + ":";
    }
}
