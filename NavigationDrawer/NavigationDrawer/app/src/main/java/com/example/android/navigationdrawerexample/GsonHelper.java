package com.example.android.navigationdrawerexample;

/**
 * Created by TranDaiNhan on 5/14/2017.
 */

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by duyth on 5/5/2017.
 */

public class GsonHelper {

    static Gson gson;

    public static Gson getGson(){
        if (gson==null)
            gson = new Gson();
        return gson;
    }

    public static  <T> T fromJson(String json, Type typeOfT){
        return getGson().fromJson(json,typeOfT);
    }

    public static String toJson(Object src){
        return getGson().toJson(src);
    }
}
