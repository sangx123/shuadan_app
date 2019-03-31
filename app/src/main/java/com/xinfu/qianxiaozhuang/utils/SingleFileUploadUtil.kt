package com.xinfu.qianxiaozhuang.utils
import com.google.gson.Gson
import com.qiniu.android.http.ResponseInfo
import com.qiniu.android.storage.UpCancellationSignal
import com.qiniu.android.storage.UpCompletionHandler
import com.qiniu.android.storage.UpProgressHandler
import com.xinfu.qianxiaozhuang.Config
import com.xinfu.qianxiaozhuang.api.Api
import com.xinfu.qianxiaozhuang.api.BaseResult
import com.xinfu.qianxiaozhuang.api.model.QiNiuResponseBean
import com.xinfu.qianxiaozhuang.api.model.QiniuModel
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * 07/02/2018  2:46 PM
 * Created by Zhang.
 */
class SingleFileUploadUtil(val localPath: String, val uploadListener: SingleFileUploadListener) : UpCompletionHandler, UpProgressHandler, UpCancellationSignal {

    val TAG = "SingleFileUploadUtil"
    var tokenFailCount = 0
    var mToake:String? = null;
    val mGson = Gson();

    private var uploadUtil: QiNiuUploadUtil = QiNiuUploadUtil.getInstance()

    private var mFileSize: Long = 0;

    private var mCancel = false


    fun cancel(){
        mCancel = true
    }

    fun start() {
        val file = File(localPath)
        if (!file.exists()) {
            uploadListener.onUploadFailed(localPath)
        } else {
            mFileSize = file.length()
        }

        getTokenAndUpload()
        /*
        thread(start = true){
            uploadListener.onStartGetToken(localPath)
            getToken()
            if(mToake == null){
                return@thread
            }
            uploadListener.onStartUpload(localPath)
            upload()
        }
        */



    }

    fun getTokenAndUpload(){
        LogUtil.e(TAG,"getTokenAndUpload");
        Api.getApiService().getQiNiuToken().subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(object : Observer<BaseResult<QiniuModel>> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                        uploadListener.onStartGetToken(localPath)
                    }

                    override fun onNext(t: BaseResult<QiniuModel>) {
                        t?.let {
                            it.data?.let {
                                mToake = it.token
                                if(mToake?.isNotBlank()?:false){
                                    uploadListener.onStartUpload(localPath)
                                    upload()
                                }else{
                                    LogUtil.e(TAG,"onNext get token is empty");
                                    //blank
                                }
                            }
                        }



                    }

                    override fun onError(e: Throwable) {
                        LogUtil.e(TAG,"update onError-->"+e.message);

                        if(tokenFailCount ++ <= Config.MAX_FAILURE_COUNT){
                            TimeUnit.SECONDS.sleep(tokenFailCount.toLong())
                            getTokenAndUpload()
                        }else{
                            uploadListener.onUploadFailed(localPath)
                        }
                    }

                })
    }



    fun upload(){
        LogUtil.e(TAG,"upload");
        if(mCancel){
            uploadListener.onUploadCancel(localPath)
            return
        }
        uploadUtil.upload(localPath,mToake,this,this,this)
    }

    override fun isCancelled(): Boolean {
        LogUtil.e(TAG,"isCancelled");
        return mCancel
    }

    override fun progress(key: String?, percent: Double) {
        LogUtil.e(TAG,"progress");
        if(mCancel){
            return
        }
        uploadListener.onUploadProgress(localPath,(percent*100).toInt(),(mFileSize*percent).toLong(),mFileSize)
    }

    override fun complete(key: String?, info: ResponseInfo?, response: JSONObject?) {
        LogUtil.e(TAG,"complete");

        if(mCancel){
            uploadListener.onUploadCancel(localPath)
            return
        }

        if (info!=null && info.isOK() ) {
            try {
                LogUtil.e(TAG, "complete: --->" + response.toString())
                val b = mGson.fromJson(response.toString(), QiNiuResponseBean::class.java!!)
                uploadListener.onUploadSucceed(localPath,b)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            LogUtil.e(TAG, "complete: failed---->>>   " + info)
            uploadListener.onUploadFailed(localPath)
        }
    }


}