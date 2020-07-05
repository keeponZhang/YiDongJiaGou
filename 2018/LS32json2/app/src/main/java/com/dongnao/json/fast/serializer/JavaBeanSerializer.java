package com.dongnao.json.fast.serializer;


import com.dongnao.json.fast.JsonConfig;
import com.dongnao.json.fast.Utils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @author Lance
 * @date 2018/5/7
 */

public class JavaBeanSerializer implements ObjectSerializer {

    private final List<FieldSerializer> getters;


    public JavaBeanSerializer(Class<?> clazz) {
        //获得对应类所有属性
        Map<String, Field> fieldCacheMap = Utils.parserAllFieldToCache(clazz);
        //获得需要序列化的成员序列化器集合
        getters = Utils.computeGetters(clazz, fieldCacheMap);
    }


    @Override
    public void serializer(JsonConfig config, StringBuilder out, Object object) {
        out.append("{");
        int i = 0;
        for (FieldSerializer getter : getters) {
            if (i != 0) {
                out.append(",");
            }
            String entry = getter.serializer(config, object);
            out.append(entry);
            if (!entry.isEmpty()) {
                i = 1;
            } else {
                i = 0;
            }
        }
        out.append("}");
    }
}
