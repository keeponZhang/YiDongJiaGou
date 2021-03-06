package com.dongnao.dnglide2.glide.load;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.dongnao.dnglide2.glide.load.codec.ResourceDecoder;

import java.io.IOException;
import java.util.List;
import java.util.TreeSet;

/**
 * @author Lance
 * @date 2018/4/21
 */
//泛型是Data的class类型
public class LoadPath<Data> {
    private final Class<Data> dataClass;
    //这里可以有多个decoders
    private final List<ResourceDecoder<Data>> decoders;

    public LoadPath(Class<Data> dataClass, List<ResourceDecoder<Data>> decoder) {
        this.dataClass = dataClass;
        this.decoders = decoder;
    }


    public Bitmap runLoad(Data data, int width, int height) {
        Bitmap result = null;
        for (ResourceDecoder<Data> decoder : decoders) {
            try {
                if (decoder.handles(data)) {
                    result = decoder.decode(data, width, height);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (result != null) {
                break;
            }
        }
        return result;
    }

}
