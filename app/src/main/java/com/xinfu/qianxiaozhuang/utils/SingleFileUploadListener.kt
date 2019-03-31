package com.xinfu.qianxiaozhuang.utils

import com.xinfu.qianxiaozhuang.api.model.QiNiuResponseBean

/**
 * 07/02/2018  4:32 PM
 * Created by Zhang.
 */
interface SingleFileUploadListener {
     fun onStartGetToken(path:String)
    fun onStartUpload(path:String);
    fun onUploadProgress(path:String,percentage:Int, bytesInProgress:Long, totalBytes:Long);
    fun onUploadSucceed(path:String,resp: QiNiuResponseBean);
    fun onUploadFailed(path:String);
    fun onUploadCancel(path:String);
}