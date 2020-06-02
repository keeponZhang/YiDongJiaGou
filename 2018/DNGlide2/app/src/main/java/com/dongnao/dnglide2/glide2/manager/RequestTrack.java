package com.dongnao.dnglide2.glide2.manager;

import com.dongnao.dnglide2.glide2.request.Request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Created by Administrator on 2018/5/9.
 */

public class RequestTrack {

    //
    private Set<Request> requests = Collections.newSetFromMap(new WeakHashMap<Request, Boolean>());

    /**
     * 如果停止了请求，则Request不在执行,Request只有弱引用 可能会被回收
     * 如果继续了请求
     * 防止停止掉的请求被回收
     */
    private List<Request> pendingRequests = new ArrayList<>();

    public void runReuqest(Request request) {

    }

    public void pauseRequests() {

    }

    public void resumeRequests() {

    }

    public void clearRequests() {

    }
}
