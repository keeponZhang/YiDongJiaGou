package com.dongnao.json.fast2.serializer;

import com.dongnao.json.fast2.FieldInfo;
import com.dongnao.json.fast2.JsonConfig;
import com.dongnao.json.fast2.Utils;

/**
 * Created by Administrator on 2018/5/14.
 */

public class FieldSerializer {


    private final FieldInfo fieldInfo;
    private final String key;
    private final boolean isPrimitive;

    public FieldSerializer(FieldInfo fieldInfo) {
        this.fieldInfo = fieldInfo;
        this.key = '"' + fieldInfo.name + "\":";
        //value的类型
        Class type = fieldInfo.type;
        //是否是基本数据类型或者它的包装类
        isPrimitive = Utils.isBox(type) || type.isPrimitive();
    }

    //{"age":100,"name":"testname","test":1,"list":["1","2"]}
    public String serializer(JsonConfig config, Object object) {
        Object o = fieldInfo.get(object);
        //属性没有值
        if (null == o) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if (isPrimitive) {
            sb.append(key);
            sb.append(o);
        } else if (Utils.isString(fieldInfo.type)) {
            sb.append(key);
            sb.append("\"");
            sb.append(o);
            sb.append("\"");
        } else {
            //JavaBean List
            ObjectSerializer serializer = config.getSerializer(fieldInfo.type);
            sb.append(key);
            serializer.serializer(config, sb, o);
        }
        return sb.toString();
    }
}
