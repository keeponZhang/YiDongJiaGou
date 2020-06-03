package com.dongnao.dnglide2.glide2;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.dongnao.dnglide2.glide2.cache.ArrayPool;
import com.dongnao.dnglide2.glide2.cache.MemoryCache;
import com.dongnao.dnglide2.glide2.cache.recycle.BitmapPool;

/**
 * Created by Administrator on 2018/5/4.
 */

public class Glide {

    private final MemoryCache memoryCache;
    private final BitmapPool bitmapPool;
    private final ArrayPool arrayPool;
    //
    private final RequestManagerRetriever requestManagerRetriever;

    private static Glide glide;

    protected Glide(Context context, GlideBuilder builder) {
        requestManagerRetriever = new RequestManagerRetriever();
        memoryCache = builder.memoryCache;
        bitmapPool = builder.bitmapPool;
        arrayPool = builder.arrayPool;
    }

    /**
     * 默认实现
     *
     * @param context
     * @return
     */
    private static Glide get(Context context) {
        if (null == glide) {
            synchronized (Glide.class) {
                if (null == glide) {
                    init(context, new GlideBuilder());
                }
            }
        }
        return glide;
    }


    /**
     * 使用者可以定制自己的 GlideBuilder
     *
     * @param context
     * @param builder
     */
    public static void init(Context context, GlideBuilder builder) {
        Context applicationContext = context.getApplicationContext();
        Glide glide = builder.build(applicationContext);
        Glide.glide = glide;
    }

    public static RequestManager with(FragmentActivity activity) {
        //原版requestManagerRetriever是RequestManagerRetriever的一个静态成员变量
        return Glide.get(activity).requestManagerRetriever.get(activity);
    }


}
