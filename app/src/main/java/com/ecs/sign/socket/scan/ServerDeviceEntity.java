package com.ecs.sign.socket.scan;

/**
 * 作者：RedKeyset on 2019/11/20 11:04
 * 邮箱：redkeyset@aliyun.com
 */
public class ServerDeviceEntity {
    private String deviceIp;
    private String deviceName;
    private boolean isCheck;

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
