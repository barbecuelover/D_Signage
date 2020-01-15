package com.ecs.sign.socket.transport;


/**
 * 作者：RedKeyset on 2019/11/18 11:24
 * 邮箱：redkeyset@aliyun.com
 */
public interface SocketFileClientCallBack {
    void onSucceed();
    void onProgress(double position);
    void onFailed();
}
