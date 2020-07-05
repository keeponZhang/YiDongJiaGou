package com.dongnao.json.fast.serializer;


import com.dongnao.json.fast.JsonConfig;
import com.dongnao.json.fast.Utils;

import java.util.List;

/**
 * @author Lance
 * @date 2018/5/8
 */

public class ListSerializer implements ObjectSerializer {

    public static final ListSerializer instance = new ListSerializer();


    @Override
    public void serializer(JsonConfig config, StringBuilder out, Object object) {
        List<?> list = (List<?>) object;
        if (list.isEmpty()) {
            out.append("[]");
            return;
        }
        out.append("[");
        for (int i = 0; i < list.size(); i++) {
            Object item = list.get(i);
            if (i != 0) {
                out.append(',');
            }
            if (item == null) {
                out.append("null");
            } else {
                Class<?> clazz = item.getClass();
                if (Utils.isBox(clazz)) {
                    out.append(item);
                } else if (Utils.isString(clazz)) {
                    out.append('"');
                    out.append(item);
                    out.append('"');
                } else {
                    //找到对应序列化器
                    ObjectSerializer objectSerializer = config.getSerializer(clazz);
                    objectSerializer.serializer(config, out, item);
                }
            }
        }
        out.append("]");
    }
}
