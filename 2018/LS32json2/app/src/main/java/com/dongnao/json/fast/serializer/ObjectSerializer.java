package com.dongnao.json.fast.serializer;

import com.dongnao.json.fast.JsonConfig;

/**
 * @author Lance
 * @date 2018/5/7
 */

public interface ObjectSerializer {

    void serializer(JsonConfig config, StringBuilder out, Object object);
}
