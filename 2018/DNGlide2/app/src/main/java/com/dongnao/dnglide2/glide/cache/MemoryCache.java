package com.dongnao.dnglide2.glide.cache;

import com.dongnao.dnglide2.glide.recycle.Resource;


public interface MemoryCache {

    interface ResourceRemovedListener {
        void onResourceRemoved(Resource resource);
    }


    Resource remove2(Key key);


    Resource put(Key key, Resource resource);


    void setResourceRemovedListener(ResourceRemovedListener listener);

    void clearMemory();

    void trimMemory(int level);
}
