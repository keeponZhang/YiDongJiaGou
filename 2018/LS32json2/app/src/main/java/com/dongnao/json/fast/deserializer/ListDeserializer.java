package com.dongnao.json.fast.deserializer;

import com.dongnao.json.fast.JsonConfig;
import com.dongnao.json.fast.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lance
 * @date 2018/5/13
 */

public class ListDeserializer implements ObjectDeserializer {

    private ParameterizedType type;

    public ListDeserializer(ParameterizedType type) {
        this.type = type;
    }

    @Override
    public <T> T deserializer(JsonConfig config, String json, Object object) {
        JSONArray jsonArray;
        if (null == object) {
            try {
                jsonArray = new JSONArray(json);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        } else {
            jsonArray = (JSONArray) object;
        }


        List list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                Object jsonItem = jsonArray.get(i);
                //集合中是对象或者集合
                if (jsonItem instanceof JSONObject || jsonItem instanceof JSONArray) {
                    //List<List<String>>
                    Type itemType = Utils.getItemType(type);
                    ObjectDeserializer deserializer = config.getDeserializer(itemType);
                    Object item = deserializer.deserializer(config, null, jsonItem);
                    list.add(item);
                } else {
                    //List<Integer>
                    list.add(jsonItem);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return (T) list;
    }
}
