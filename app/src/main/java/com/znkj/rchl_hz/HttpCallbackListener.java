package com.znkj.rchl_hz;

/**
 * Created by lenovo-pc on 2018-01-29.
 */

public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);

}
