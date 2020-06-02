package com.dongnao.dnglide2.glide2;

import android.app.ActivityManager;
import android.content.Context;
import android.util.DisplayMetrics;

import com.dongnao.dnglide2.glide2.cache.ArrayPool;
import com.dongnao.dnglide2.glide2.cache.LruMemoryCache;
import com.dongnao.dnglide2.glide2.cache.MemoryCache;
import com.dongnao.dnglide2.glide2.cache.recycle.BitmapPool;
import com.dongnao.dnglide2.glide2.cache.recycle.DiskCache;
import com.dongnao.dnglide2.glide2.cache.recycle.DiskLruCacheWrapper;
import com.dongnao.dnglide2.glide2.cache.recycle.LruBitmapPool;

/**
 * Created by Administrator on 2018/5/9.
 */

public class GlideBuilder {

    MemoryCache memoryCache;
    DiskCache diskCache;
    BitmapPool bitmapPool;
    //进行数组的缓存
    ArrayPool arrayPool;


    public void setMemoryCache(MemoryCache memoryCache) {
        this.memoryCache = memoryCache;
    }

    public void setDiskCache(DiskCache diskCache) {
        this.diskCache = diskCache;
    }

    public void setBitmapPool(BitmapPool bitmapPool) {
        this.bitmapPool = bitmapPool;
    }

    private static int getMaxSize(ActivityManager activityManager) {
        //使用最大可用内存的0.4作为缓存使用  64M
        final int memoryClassBytes = activityManager.getMemoryClass() * 1024 * 1024;
        return Math.round(memoryClassBytes * 0.4f);
    }

    public Glide build(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context
                .ACTIVITY_SERVICE);
        //Glide缓存最大可用内存大小
        int maxSize = getMaxSize(activityManager);

        //减去数组缓存后的可用内存大小
        int availableSize = maxSize - arrayPool.getMaxSize();

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int widthPixels = displayMetrics.widthPixels;
        int heightPixels = displayMetrics.heightPixels;
        // 获得一个屏幕大小的argb所占的内存大小
        int screenSize = widthPixels * heightPixels * 4;

        //bitmap复用占 4份
        float bitmapPoolSize = screenSize * 4.0f;
        //内存缓存占 2份
        float memoryCacheSize = screenSize * 2.0f;

        if (bitmapPoolSize + memoryCacheSize <= availableSize) {
            bitmapPoolSize = Math.round(bitmapPoolSize);
            memoryCacheSize = Math.round(memoryCacheSize);
        } else {
            //把总内存分成 6分
            float part = availableSize / 6.0f;
            bitmapPoolSize = Math.round(part * 4);
            memoryCacheSize = Math.round(part * 2);
        }
        if (null == bitmapPool) {
            bitmapPool = new LruBitmapPool((int) bitmapPoolSize);
        }
        if (null == memoryCache) {
            memoryCache = new LruMemoryCache((int) memoryCacheSize);
        }
//TODO       memoryCache.setResourceRemoveListener();
        if (null == diskCache) {
            diskCache = new DiskLruCacheWrapper(context);
        }
        return new Glide(context, this);
    }

}
