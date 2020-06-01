package com.dongnao.dnglide2.glide.cache;


import java.security.MessageDigest;

/**
 * @author Lance
 * @date 2018/4/20
 */

public interface Key {

    void updateDiskCacheKey(MessageDigest messageDigest);

    byte[] getKeyBytes();

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();
}
