package com.dongnao.dnglide2.glide.cache;

/**
 * @author Lance
 * @date 2018/4/22
 */

public interface ArrayPool {

    byte[] get(int len);

    void put(byte[] data);


    void clearMemory();

    void trimMemory(int level);

    int getMaxSize();
}
