package com.fanhl.util;

import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

/**
 * Gson用工具类<BR>
 * Created by honglin on 2014/9/24.
 */
public class GsonUtil {
    public static final String TAG = GsonUtil.class.getSimpleName();

    private static final Gson gson;

    static {
        gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            /**
             * 设置要过滤的属性
             */
            @Override
            public boolean shouldSkipField(FieldAttributes attr) {
                // 设置对应类中要过虑的属性
//				if (attr.getDeclaringClass() == UserInfo.class) {
//					return "password".equals(attr.getName());
//				}
                return false;
            }

            /**
             * 设置要过滤的类
             */
            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                // 这里，如果返回true就表示此类要过滤，否则就输出
                return false;
            }
        }).create();
    }

    public static final String json(Object obj) {
        return gson.toJson(obj);
    }

    public static final <T> T obj(String json, Class<T> classOfT) {
        try {
            return gson.fromJson(json, classOfT);
        } catch (JsonSyntaxException e) {
            Log.e(TAG, "解析json失败");
        }
        return null;
    }

    public static final <T> T obj(String json, Type typeOfT) {
        try {
            return gson.fromJson(json, typeOfT);
        } catch (JsonSyntaxException e) {
            Log.e(TAG, "解析json失败");
        }
        return null;
    }
}