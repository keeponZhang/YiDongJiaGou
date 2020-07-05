package com.dongnao.json.fast2.serializer;

import com.dongnao.json.fast2.JsonConfig;

/**
 * Created by Administrator on 2018/5/14.
 */

public interface ObjectSerializer {

    public void serializer(JsonConfig config, StringBuilder out, Object object);
}
