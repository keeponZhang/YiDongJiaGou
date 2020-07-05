package com.dongnao.json.fast2;

import com.dongnao.json.fast2.serializer.JavaBeanSerializer;
import com.dongnao.json.fast2.serializer.ListSerializer;
import com.dongnao.json.fast2.serializer.ObjectSerializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/14.
 */
public class JsonConfig {

    private static JsonConfig globalInstance = new JsonConfig();
    //序列化器缓存
    private Map<Class, ObjectSerializer> serializers = new HashMap();

    public static JsonConfig getGlobalInstance() {
        return globalInstance;
    }


    public ObjectSerializer getSerializer(Class<?> clazz) {
        ObjectSerializer objectSerializer = serializers.get(clazz);
        //
        if (null != objectSerializer) {
            return objectSerializer;
        }
        //
        if (List.class.isAssignableFrom(clazz)) {
            objectSerializer = ListSerializer.instance;
        } else if (Map.class.isAssignableFrom(clazz)) {
            //....
            throw new RuntimeException("Map序列化未实现");
            //数组
        } else if (clazz.isArray()) {
            throw new RuntimeException("数组序列化未实现");
        } else {
            objectSerializer = new JavaBeanSerializer(clazz);
        }
        serializers.put(clazz, objectSerializer);
        return objectSerializer;
    }

}
