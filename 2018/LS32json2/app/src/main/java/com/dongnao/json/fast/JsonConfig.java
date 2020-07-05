package com.dongnao.json.fast;

import com.dongnao.json.fast.deserializer.JavaBeanDeserializer;
import com.dongnao.json.fast.deserializer.ListDeserializer;
import com.dongnao.json.fast.deserializer.ObjectDeserializer;
import com.dongnao.json.fast.serializer.JavaBeanSerializer;
import com.dongnao.json.fast.serializer.ListSerializer;
import com.dongnao.json.fast.serializer.ObjectSerializer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Lance
 * @date 2018/5/14
 */

public class JsonConfig {


    public final static JsonConfig globalInstance = new JsonConfig();

    public final static JsonConfig getGlobalInstance() {
        return globalInstance;
    }


    /**
     * 序列化
     */
    private Map<Class, ObjectSerializer> serializers = new HashMap();

    public ObjectSerializer getSerializer(Class<?> clazz) {
        ObjectSerializer objectSerializer = serializers.get(clazz);
        if (null != objectSerializer) {
            return objectSerializer;
        }
        if (List.class.isAssignableFrom(clazz)) {
            objectSerializer = ListSerializer.instance;
        } else if (Map.class.isAssignableFrom(clazz)) {
            throw new RuntimeException("暂不支持Map序列化");
        } else if (clazz.isArray()) {
            throw new RuntimeException("暂不支持数组序列化");
        } else {
            objectSerializer = new JavaBeanSerializer(clazz);
        }
        serializers.put(clazz, objectSerializer);
        return objectSerializer;
    }


    /**
     * 反序列化
     */
    private Map<Type, ObjectDeserializer> deserializers = new HashMap<>();

    public ObjectDeserializer getDeserializer(Type type) {
        ObjectDeserializer deserializer = deserializers.get(type);
        if (null == deserializer) {
            if (type instanceof Class) {
                deserializer = new JavaBeanDeserializer((Class) type);
            } else if (type instanceof ParameterizedType) {
                deserializer = new ListDeserializer((ParameterizedType) type);
            }
            deserializers.put(type, deserializer);
        }
        return deserializer;
    }

}
