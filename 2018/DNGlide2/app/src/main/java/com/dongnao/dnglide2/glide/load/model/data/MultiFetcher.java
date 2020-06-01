package com.dongnao.dnglide2.glide.load.model.data;

import android.support.annotation.Nullable;

import java.util.List;

/**
 * @author Lance
 * @date 2018/4/21
 */

public class MultiFetcher<Data> implements DataFetcher<Data>, DataFetcher
        .DataFetcherCallback<Data> {
    //代理多个fetcher
    private final List<DataFetcher<Data>> fetchers;
    private int currentIndex;
    private DataFetcherCallback<? super Data> callback;

    public MultiFetcher(List<DataFetcher<Data>> fetchers) {
        this.fetchers = fetchers;
        currentIndex = 0;
    }


    @Override
    public void loadData(DataFetcherCallback<? super Data> callback) {
        this.callback = callback;
        fetchers.get(currentIndex).loadData(this);
    }


    @Override
    public void cancel() {
        for (DataFetcher<Data> fetcher : fetchers) {
            fetcher.cancel();
        }
    }

    @Override
    public Class<Data> getDataClass() {
        return fetchers.get(0).getDataClass();
    }

    /**
     * 执行下一个fetcher或者失败
     */
    private void startNextOrFail() {
        if (currentIndex < fetchers.size() - 1) {
            //使用下一个fetcher
            currentIndex++;
            loadData(callback);
        } else {
            callback.onLoadFailed(new RuntimeException("Fetch failed"));
        }
    }

    @Override
    public void onFetcherReady(@Nullable Data data) {
        if (data != null) {
            callback.onFetcherReady(data);
        } else {
            startNextOrFail();
        }
    }

    @Override
    public void onLoadFailed(Exception e) {
        startNextOrFail();
    }
}
