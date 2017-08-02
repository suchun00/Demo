package com.sc.utils;

import com.google.gson.Gson;

/**
 * Created by suchun on 2017/7/21.
 */
public class GsonUtil {
    public static <T> T parseJsonWithGson(String jsonData, Class<T> type){
        Gson gson = new Gson();

        T result = gson.fromJson(jsonData,type);
        return result;
    }

}
