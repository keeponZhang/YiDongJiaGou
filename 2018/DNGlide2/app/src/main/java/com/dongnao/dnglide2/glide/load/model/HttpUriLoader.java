package com.dongnao.dnglide2.glide.load.model;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.dongnao.dnglide2.glide.load.ObjectKey;
import com.dongnao.dnglide2.glide.load.model.data.HttpUriFetcher;

import java.io.InputStream;

/**
 * @author Lance
 * @date 2018/4/21
 */

public class HttpUriLoader implements ModelLoader<Uri, InputStream> {

    @Override
    public LoadData<InputStream> buildLoadData(Uri uri) {
        return new LoadData<>(new ObjectKey(uri), new HttpUriFetcher(uri));
    }

    @Override
    public boolean handles(Uri uri) {
        String scheme = uri.getScheme();
        return scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https");
    }

    public static class Factory implements ModelLoaderFactory<Uri, InputStream> {

        @NonNull
        @Override
        public ModelLoader<Uri, InputStream> build(ModelLoaderRegistry modelLoaderRegistry) {
            return new HttpUriLoader();
        }

    }
}
