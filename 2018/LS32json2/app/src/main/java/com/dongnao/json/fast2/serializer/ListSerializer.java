package com.dongnao.json.fast2.serializer;

import com.dongnao.json.fast2.JsonConfig;
import com.dongnao.json.fast2.Utils;

import java.util.List;

/**
 * Created by Administrator on 2018/5/14.
 * 负责list集合序列化器
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
            if (i != 0) {
                out.append(',');
            }
            Object item = list.get(i);
            if (null == item) {
                out.append("null");
            } else {
                Class<?> clazz = item.getClass();
                if (Utils.isBox(clazz)) {
                    out.append(item);
                } else if (Utils.isString(clazz)) {
                    out.append("\"");
                    out.append(item);
                    out.append("\"");
                } else {
                    //
                    ObjectSerializer serializer = config.getSerializer(clazz);
                    serializer.serializer(config, out, item);
                }
            }
        }
        out.append("]");

    }
}
