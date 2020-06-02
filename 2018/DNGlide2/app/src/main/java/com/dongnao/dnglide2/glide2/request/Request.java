package com.dongnao.dnglide2.glide2.request;

import com.dongnao.dnglide2.glide2.Target;

/**
 * Created by Administrator on 2018/5/9.
 */

public class Request {

    private final Object model;
    private final Target target;

    public Request(Object model, Target target) {
        this.model = model;
        this.target = target;
    }
}
