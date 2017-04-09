package cx.study.auction.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import cx.study.auction.BuildConfig;

/**
 *
 * Created by chengxiao on 2017/4/8.
 */

public class SharedPreferencesUtil {
    private static final String NAME = BuildConfig.APPLICATION_ID;
    public static void put(Context context,String key,String value){
        SharedPreferences spf = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        spf.edit().putString(key,value).apply();
    }
    public static void put(Context context,String key,boolean value){
        SharedPreferences spf = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        spf.edit().putBoolean(key,value).apply();
    }
    public static void put(Context context,String key,float value){
        SharedPreferences spf = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        spf.edit().putFloat(key,value).apply();
    }
    public static void put(Context context,String key,int value){
        SharedPreferences spf = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        spf.edit().putInt(key,value).apply();
    }
    public static void put(Context context,String key,long value){
        SharedPreferences spf = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        spf.edit().putLong(key,value).apply();
    }
}
