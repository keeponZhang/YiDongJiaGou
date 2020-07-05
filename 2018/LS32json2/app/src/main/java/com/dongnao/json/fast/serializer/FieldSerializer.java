package com.dongnao.json.fast.serializer;


import com.dongnao.json.fast.FieldInfo;
import com.dongnao.json.fast.JsonConfig;
import com.dongnao.json.fast.Utils;

/**
 * @author Lance
 * @date 2018/5/8
 */

public class FieldSerializer {

    private String key;
    private FieldInfo fieldInfo;
    private boolean isPrimitive;

    public FieldSerializer(FieldInfo fieldInfo) {
        this.fieldInfo = fieldInfo;
        this.key = '"' + fieldInfo.name + "\":";
        Class type = fieldInfo.type;
        //是否是基本数据类型或者其包装类
        isPrimitive = Utils.isBox(type) || type.isPrimitive();
    }

    public String serializer(JsonConfig config, Object object) {
        Object o = fieldInfo.get(object);
        //没值就不写了
        if (null == o) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if (isPrimitive) {
            sb.append(key);
            sb.append(o.toString());
        } else if (Utils.isString(fieldInfo.type)) {
            sb.append(key);
            sb.append('"');
            sb.append(o.toString());
            sb.append('"');
        } else {
            //找到对应的序列化器
            ObjectSerializer objectSerializer = config.getSerializer(fieldInfo.type);
            sb.append(key);
            objectSerializer.serializer(config, sb, o);
        }
        return sb.toString();
    }
}
