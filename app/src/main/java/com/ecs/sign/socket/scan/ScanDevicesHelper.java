package com.ecs.sign.socket.scan;



import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;

import com.ecs.sign.base.common.Constant;
import com.ecs.sign.base.common.util.LogUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * @author zw
 * @time 2020/1/9
 * @description
 */
public class ScanDevicesHelper {

    private boolean isRunning = true;

    private static ScanDevicesHelper instance;
    private WifiManager.MulticastLock lock;

    private OnServerDeviceCallBackListener serverDeviceCallBack;
    private CompositeDisposable compositeDisposable;

    private Context context;

    private ScanDevicesHelper(Context context) {
        this.context = context;
        WifiManager manager = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        if (manager != null) {
            this.lock = manager.createMulticastLock("UDP_WIFI");
        }
    }

    public  static  ScanDevicesHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (ScanDevicesHelper.class) {
                if (instance == null) {
                    instance = new ScanDevicesHelper(context);
                }
            }
        }
        return instance;
    }


    private void  disposeEvent(){
        if (compositeDisposable!=null){
            compositeDisposable.clear();
        }
    }


    public interface OnServerDeviceCallBackListener {
        void onResult(ServerDeviceEntity serverDeviceEntity);
    }

    public void setListeningCallBack(OnServerDeviceCallBackListener callBack) {
        this.serverDeviceCallBack = callBack;
    }


    public void stopListening() {
        isRunning = false;
        if (lock.isHeld()){
            lock.release();
        }
        disposeEvent();
    }
    

    public void startListening() {
        isRunning =true;
        Disposable listenDisposable = Observable.create(new ObservableOnSubscribe<ServerDeviceEntity>() {
            @Override
            public void subscribe(ObservableEmitter<ServerDeviceEntity> emitter) throws Exception {
                startListen(emitter);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<ServerDeviceEntity>() {
                    @Override
                    public void accept(ServerDeviceEntity entity) throws Exception {
                        if (serverDeviceCallBack != null) {
                            LogUtils.e("*********serverDeviceCallBack********************   ====："+entity.getDeviceIp());
                            serverDeviceCallBack.onResult(entity);
                        }
                    }
                });

        addEventSubscribe(listenDisposable);
    }


    public void scanServerDevices() {
        LogUtils.e("============scanServerDevices=======");

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                 new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        LogUtils.e("============scanServerDevices= subscribe======");
//                        sendMsg();
//                    }
//                }).start();
//            }
//        }, 1000);


        Disposable scanDisposable = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                LogUtils.e("============scanServerDevices= subscribe======");
                sendMsg();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
        addEventSubscribe(scanDisposable);
    }



    /**
     * 接受返回设备列表IP 信息等。
     */
    private void startListen(ObservableEmitter<ServerDeviceEntity> emitter) {
        // UDP服务器监听的端口
        // 接收的字节大小，客户端发送的数据不能超过这个大小
        LogUtils.e("================startListen====================");
        byte[] message = new byte[500];
        try {
            DatagramSocket datagramSocket = new DatagramSocket(null);
            datagramSocket.setReuseAddress(true);
            datagramSocket.bind(new InetSocketAddress(Constant.SOCKET_PORT));

            // 建立Socket连接
            datagramSocket.setBroadcast(true);
            DatagramPacket datagramPacket = new DatagramPacket(message,
                    message.length);
            try {
                while (isRunning) {
                    LogUtils.e("UDP lock isHeld", "判断：" + lock.isHeld());
                    if (!lock.isHeld()) {
                        // 准备接收数据
                        LogUtils.e( "准备接受");
                        this.lock.acquire();
                        datagramSocket.receive(datagramPacket);
                        String strMsg = new String(datagramPacket.getData()).trim();
                        String strIp = datagramPacket.getAddress().getHostAddress();
                        if (strMsg.contains(Constant.SOCKET_SERVER)) {
                            ServerDeviceEntity serverDeviceEntity = new ServerDeviceEntity();
                            serverDeviceEntity.setCheck(false);
                            serverDeviceEntity.setDeviceIp(strIp);
                            serverDeviceEntity.setDeviceName(strMsg);
                            emitter.onNext(serverDeviceEntity);
//                            if (serverDeviceCallBack != null) {
//                                serverDeviceCallBack.onResult(serverDeviceEntity);
//                            }
                        }
                        if (lock.isHeld()) {
                            this.lock.release();
                        }
                    }
                }
                datagramSocket.close();
            } catch (IOException e) {//IOException
                e.printStackTrace();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private void sendMsg(){
        LogUtils.e("================sendMsg====================");
        String message = Constant.SOCKET_CLIENT;
        DatagramSocket s = null;
        try {
            s = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        InetAddress local = null;
        try {
            local = InetAddress.getByName("255.255.255.255");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        int msg_length = message.length();
        byte[] messageByte = message.getBytes();
        DatagramPacket p = new DatagramPacket(messageByte, msg_length, local,
                Constant.SOCKET_PORT);
        try {
            s.send(p);
            s.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 将订阅事件 event 加入到 disposable的容器中
     * @param disposable
     */
    private void addEventSubscribe(Disposable disposable){
        LogUtils.e("===addEventSubscribe==");
        if (compositeDisposable == null ){
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

}
