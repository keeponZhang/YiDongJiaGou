package com.dongnao.dnglide2.glide2.cache;

import com.dongnao.dnglide2.glide2.cache.recycle.Resource;

/**
 * Created by Administrator on 2018/5/4.
 */

public interface MemoryCache {

    interface ResourceRemoveListener{
        void onResourceRemoved(Resource resource);
    }

    Resource put(Key key, Resource resource);

    void setResourceRemoveListener(ResourceRemoveListener listener);

    Resource remove2(Key key);

}
