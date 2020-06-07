package com.dongnao.dnglide2.glide.load.generator;

import com.dongnao.dnglide2.glide.cache.Key;

/**
 * @author Lance
 * @date 2018/4/21
 */

public interface DataGenerator {
    interface DataGeneratorCallback {
        //标记图片来源
        enum DataSource {
            REMOTE,
            CACHE
        }
        //这里有个DataSource表示来源
        void onDataReady(Key sourceKey, Object data, DataSource dataSource);

        void onDataFetcherFailed(Key sourceKey, Exception e);
    }

    boolean startNext();

    void cancel();
}
