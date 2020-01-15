package com.ecs.baseporject.bean;

import java.io.Serializable;

/**
 * 作者：RedKeyset on 2019/11/19 10:19
 * 邮箱：redkeyset@aliyun.com
 */
public class FileTransferInfo implements Serializable {
    //文件路径
    private String filePath;
    //文件大小
    private long fileLength;
    //MD5码
    private String md5;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
