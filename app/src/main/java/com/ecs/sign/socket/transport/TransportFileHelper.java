package com.ecs.sign.socket.transport;

import com.ecs.baseporject.bean.FileTransferInfo;
import com.ecs.sign.base.common.Constant;
import com.ecs.sign.base.common.util.FileUtils;
import com.ecs.sign.base.common.util.LogUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zw
 * @time 2020/1/15
 * @description
 */
public class TransportFileHelper {

    public void startTransport(String ip,File file,SocketFileClientCallBack clientCallBack) {

        Observable.create(new ObservableOnSubscribe<Double>() {
            @Override
            public void subscribe(ObservableEmitter<Double> emitter) throws Exception {
                transport(ip,file,emitter);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Double>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Double aDouble) {
                        clientCallBack.onProgress(aDouble);
                    }

                    @Override
                    public void onError(Throwable e) {
                        clientCallBack.onFailed();
                    }

                    @Override
                    public void onComplete() {
                        clientCallBack.onSucceed();
                    }
                });


    }

    private void transport(String ip, File file, ObservableEmitter<Double> emitter) {

        if (file.exists()) {
            FileTransferInfo fileTransfer = new FileTransferInfo();
            fileTransfer.setFilePath(file.getAbsolutePath());
            fileTransfer.setFileLength(file.length());
            fileTransfer.setMd5(FileUtils.getFileMD5(file));

            LogUtils.e( "计算结束，文件的MD5码值是：" + fileTransfer.getMd5());

             Socket client;
             FileInputStream fis;
             ObjectOutputStream objectOutputStream;
             OutputStream outputStream;

            try {

                client  =new Socket(ip, Constant.SERVER_PORT);

                //BufferedInputStream bi=new BufferedInputStream(new InputStreamReader(new FileInputStream(file),"GBK"));
                outputStream = client.getOutputStream();
                objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(fileTransfer);
                fis = new FileInputStream(new File(fileTransfer.getFilePath()));

                byte buf[] = new byte[4096];
                int len;
                //文件大小
                long fileSize = fileTransfer.getFileLength();
                //当前的传输进度
                double progress;
                //总的已传输字节数
                long total = 0;
                //缓存-当次更新进度时的时间
                long tempTime = System.currentTimeMillis();
                //缓存-当次更新进度时已传输的总字节数
                long tempTotal = 0;
                //传输速率（Kb/s）
                double speed;
                //预估的剩余完成时间（秒）
                double remainingTime;
                // 开始传输文件
                System.out.println("======== 开始传输文件 ========");

                while ((len = fis.read(buf)) != -1) {
                    outputStream.write(buf, 0, len);
                    total += len;
                    long time = System.currentTimeMillis() - tempTime;
                    //每一秒更新一次传输速率和传输进度
                    if (time > 500) {
                        //当前的传输进度
                        progress = total * 100 / fileSize;
                        LogUtils.e( "---------------------------");
                        LogUtils.e( "传输进度: " + progress);
                        LogUtils.e( "时间变化：" + time / 1000.0);
                        LogUtils.e( "字节变化：" + (total - tempTotal));
                        //计算传输速率，字节转Kb，毫秒转秒
                        speed = ((total - tempTotal) / 1024.0 / (time / 1000.0));
                        //预估的剩余完成时间
                        remainingTime = (fileSize - total) / 1024.0 / speed;
                        LogUtils.e( "传输速率：" + speed);
                        LogUtils.e( "预估的剩余完成时间：" + remainingTime);
                        //缓存-当次更新进度时已传输的总字节数
                        tempTotal = total;
                        //缓存-当次更新进度时的时间
                        tempTime = System.currentTimeMillis();
//                        callBack.onProgress(progress);
                        emitter.onNext(progress);
                    }
                }
                emitter.onComplete();
                fis.close();
                outputStream.close();
                objectOutputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        }else {
            LogUtils.e("文件不存在");
        }
    }
}
