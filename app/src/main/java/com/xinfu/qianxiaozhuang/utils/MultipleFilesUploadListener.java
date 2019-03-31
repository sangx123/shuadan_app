package com.xinfu.qianxiaozhuang.utils;

import android.support.annotation.Keep;
import com.xinfu.qianxiaozhuang.api.model.QiNiuResponseBean;

import java.util.List;

/**
 * 07/02/2018  2:51 PM
 * Created by Zhang.
 */

@Keep
public interface MultipleFilesUploadListener {
    void onStartGetToken();

    void onStartUpload();

    void onUploadProgress(int percentage, long bytesInProgress, long totalBytes);

    void onUploadSucceed(List<QiNiuResponseBean> resp);

    void onUploadFailed();

}
