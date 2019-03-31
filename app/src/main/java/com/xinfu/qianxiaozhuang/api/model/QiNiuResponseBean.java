package com.xinfu.qianxiaozhuang.api.model;

import android.support.annotation.Keep;

/**
 * 21/08/2017  4:06 PM
 * Created by Zhang.
 */

@Keep
public class QiNiuResponseBean {
    /**
     * url : http://osjmq0g3u.bkt.clouddn.com//data/user/0/com.emucoo/cache/ImagePicker/cropTemp/IMG_20170821_152544.jpg
     * key : /data/user/0/com.emucoo/cache/ImagePicker/cropTemp/IMG_20170821_152544.jpg
     * hash : FiudRzBIZSfKo36SYZqp3rCRRpEM
     * bucket : files
     * fsize : 151547
     */

    private String url;
    private String key;
    private String hash;
    private String bucket;
    private int fsize;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public int getFsize() {
        return fsize;
    }

    public void setFsize(int fsize) {
        this.fsize = fsize;
    }


}
