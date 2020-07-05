package com.dongnao.json.fast.deserializer;

import com.dongnao.json.fast.JsonConfig;

/**
 * @author Lance
 * @date 2018/5/13
 */

public interface ObjectDeserializer {

    <T> T deserializer(JsonConfig config,String json, Object object);

}
