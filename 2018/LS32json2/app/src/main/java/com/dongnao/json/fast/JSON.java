package com.dongnao.json.fast;

import com.dongnao.json.fast.deserializer.ObjectDeserializer;
import com.dongnao.json.fast.serializer.ObjectSerializer;

import java.lang.reflect.Type;

/**
 * @author Lance
 * @date 2018/5/7
 */

public class JSON {


    /**
     * 序列化
     *
     * @param object
     * @return
     */
    public static String toJSONString(Object object) {
        return toJSONString(object, JsonConfig.getGlobalInstance());

    }

    public static String toJSONString(Object object, JsonConfig config) {
        if (null == object) {
            return "null";
        }
        StringBuilder out = new StringBuilder();
        Class<?> clazz = object.getClass();
        ObjectSerializer serializer = config.getSerializer(clazz);
        serializer.serializer(config, out, object);
        return out.toString();
    }

    /**
     * 反序列化
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T parse(String json, Type type) {
        return parse(json, type, JsonConfig.getGlobalInstance());
    }

    public static <T> T parse(String json, Class<T> clazz) {
        return parse(json, (Type) clazz, JsonConfig.getGlobalInstance());
    }


    public static <T> T parse(String json, Class<T> clazz, JsonConfig config) {
        return parse(json, (Type) clazz, config);
    }


    public static <T> T parse(String json, Type type, JsonConfig config) {
        if (null == json || json.isEmpty()) {
            return null;
        }
        ObjectDeserializer deserializer = config.getDeserializer(type);
        return deserializer.deserializer(config, json, null);
    }


}
