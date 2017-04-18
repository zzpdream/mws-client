package com.ydd.conference.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by ranfi on 12/9/14.
 */
public class JsonMapper {

    private final static Gson gson;

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("yyyy-MM-dd HH:mm:ss");
        builder.excludeFieldsWithoutExposeAnnotation();
        gson = builder.create();
    }

    private JsonMapper() {

    }

    /**
     * Object可以是POJO，也可以是Collection或数组。 如果对象为Null, 返回"null". 如果集合为空集合, 返回"[]".
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }


    public static String toJson(Object obj, Type type) {
        return gson.toJson(obj, type);
    }

    /**
     * 反序列化POJO或简单Collection如List<String>.
     * <p/>
     * 如果JSON字符串为Null或"null"字符串, 返回Null. 如果JSON字符串为"[]", 返回空集合.
     * <p/>
     * 如需反序列化复杂Collection如List<MyBean>, 请使用fromJson(String, JavaType)
     */
    public static <T> T fromJson(String jsonValue, Class<T> clazz) {
        return gson.fromJson(jsonValue, clazz);
    }

    public static <T> T fromJson(String jsonValue, Type type) {
        return gson.fromJson(jsonValue, type);
    }

    public static <T> List<T> fromJson(String jsonValue) {
        return gson.fromJson(jsonValue, new TypeToken<List<T>>() {
        }.getType());
    }
}
