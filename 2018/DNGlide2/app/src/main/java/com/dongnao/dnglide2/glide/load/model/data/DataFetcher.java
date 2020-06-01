package com.dongnao.dnglide2.glide.load.model.data;

/**
 * @author Lance
 * @date 2018/4/21
 * 数据获取器
 */
public interface DataFetcher<T> {


    interface DataFetcherCallback<T> {


        void onFetcherReady(T data);


        void onLoadFailed(Exception e);
    }

    void loadData(DataFetcherCallback<? super T> callback);

    void cancel();

    Class<T> getDataClass();

}

