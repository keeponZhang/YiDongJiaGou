package com.dongnao.dnglide2.glide.load.codec;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lance
 * @date 2018/4/21
 */

public class ResourceDecoderRegistry {

    private final List<Entry<?>> entries = new ArrayList<>();

    public <Data> List<ResourceDecoder<Data>> getDecoders(Class<Data> dataClass) {
        List<ResourceDecoder<Data>> docoders = new ArrayList<>();
        for (Entry<?> entry : entries) {
            Log.e("TAG", "ResourceDecoderRegistry getDecoders dataClass:" +dataClass);
            if (entry.handles(dataClass)) {
                docoders.add((ResourceDecoder<Data>) entry.decoder);
            }
        }
        return docoders;
    }

    private static class Entry<T> {
        private final Class<T> dataClass;
        final ResourceDecoder<T> decoder;

        public Entry(Class<T> dataClass, ResourceDecoder<T> decoder) {
            this.dataClass = dataClass;
            this.decoder = decoder;
        }
        //之类entry有个handles（参数传class），后面的ResourceDecoder也有个handles方法
        public boolean handles(Class<?> dataClass) {
            return this.dataClass.isAssignableFrom(dataClass);
        }
    }

    public <T> void add(Class<T> dataClass, ResourceDecoder<T> decoder) {
        entries.add(new Entry<>(dataClass, decoder));
    }
}
