package com.dongnao.json.fast2;

import com.dongnao.json.fast2.serializer.ObjectSerializer;

/**
 * Created by Administrator on 2018/5/14.
 */

public class JSON {

    public static String toJSONString(Object object) {
        ObjectSerializer serializer = JsonConfig.getGlobalInstance().getSerializer(object.getClass());
        StringBuilder sb = new StringBuilder();
        serializer.serializer(JsonConfig.getGlobalInstance(), sb, object);
        return sb.toString();
    }
}
