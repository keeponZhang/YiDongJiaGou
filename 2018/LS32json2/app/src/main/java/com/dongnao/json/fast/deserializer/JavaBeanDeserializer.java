package com.dongnao.json.fast.deserializer;

import com.dongnao.json.fast.FieldInfo;
import com.dongnao.json.fast.JSON;
import com.dongnao.json.fast.JsonConfig;
import com.dongnao.json.fast.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @author Lance
 * @date 2018/5/10
 */

public class JavaBeanDeserializer implements ObjectDeserializer {

    /**
     * 所有的非公开属性
     */
    private final List<FieldInfo> setter;
    private final Class beanType;


    public JavaBeanDeserializer(Class clazz) {
        this.beanType = clazz;
        Map<String, Field> fieldCacheMap = Utils.parserAllFieldToCache(clazz);
        setter = Utils.computeSetters(clazz, fieldCacheMap);
    }


    @Override
    public <T> T deserializer(JsonConfig config, String json, Object object) {
        JSONObject jsonObject;
        if (null == object) {
            try {
                jsonObject = new JSONObject(json);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        } else {
            jsonObject = (JSONObject) object;
        }
        //必须有默认无参构造函数
        T t;
        try {
            t = (T) beanType.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        for (FieldInfo fieldInfo : setter) {
            if (!jsonObject.has(fieldInfo.name)) {
                continue;
            }
            try {
                Object value = jsonObject.get(fieldInfo.name);
                //value类型是一个json对象
                if (value instanceof JSONObject) {
                    //获得对应反序列化器
                    ObjectDeserializer deserializer = config
                            .getDeserializer(fieldInfo.type);
                    Object obj = deserializer.deserializer(config, null, value);
                    fieldInfo.set(t, obj);
                } else if (value instanceof JSONArray) {
                    //集合
                    //获得属性的Type 直接getType获得class 拿不到泛型(泛型擦除)
                    ObjectDeserializer deserializer = config
                            .getDeserializer(fieldInfo.field
                                    .getGenericType());
                    Object item = deserializer.deserializer(config, null, value);
                    fieldInfo.set(t, item);
                } else {
                    if (value != JSONObject.NULL) {
                        fieldInfo.set(t, value);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return t;
    }

}
