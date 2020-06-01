package com.dongnao.dnglide2.glide2.cache;

import java.security.MessageDigest;

/**
 * Created by Administrator on 2018/5/4.
 */
public interface Key {

    void updateDiskCacheKey(MessageDigest md);
}
