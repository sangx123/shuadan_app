package com.xinfu.qianxiaozhuang.activity.publish

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.RequestCodeConfig
import com.xinfu.qianxiaozhuang.activity.BaseActivity
import com.xinfu.qianxiaozhuang.api.Api
import com.xinfu.qianxiaozhuang.api.BaseResult
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_publish_task.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.error
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.io.File
import android.widget.Toast
import javax.xml.datatype.DatatypeConstants.SECONDS
import android.R.attr.button
import com.jakewharton.rxbinding2.view.RxView
import com.xinfu.qianxiaozhuang.utils.StringUtils
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit


/**
 * 发布任务
 */
class PublishTaskActivity : BaseActivity() {
    var content=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish_task)
        initUI()
    }

    private fun initUI() {
        mContent.setOnClickListener {
            var intent= Intent(this@PublishTaskActivity,PublishTaskContextEditActivity::class.java)
            intent.putExtra(PublishTaskContextEditActivity.param_data_content,content)
            startActivityForResult(intent,RequestCodeConfig.request100)
        }

        mBtn.setOnClickListener {
            error { content }
        }
        RxView.clicks(mBtn)
                .throttleFirst(3, TimeUnit.SECONDS)//在一秒内只取第一次点击
                .subscribe { uploadImageList() }
    }

    //上传多张图片
    private fun uploadImageList(){
        error { content }
        return
        val contentList = StringUtils.cutStringByLineTag(content)
        for(item in contentList){
            if(item.contains("img")){
                contentList.add(StringUtils.getImgSrc(item))
            }
        }
        showApiProgress()

        var map = HashMap<String, RequestBody>()
        map["content"] = RequestBody.create(MediaType.parse("multipart/form-data"), content)

        for(item in contentList){
            var file= File(item)
            map.put("imageList\";filename=\""+file.name, MultipartBody.create(MediaType.parse("multipart/form-data"),file))
        }

        Api.getApiService().uploadImageList(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<String>> {
                    override fun onComplete() {
                        hideApiProgress()
                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(t: BaseResult<String>) {
//                            t.result?.let {
//                                toast("图片上传成功！")
//                            }

                        toast("保存成功!")
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            RequestCodeConfig.request100->{
                if(resultCode==RequestCodeConfig.result200){
                    if(data!=null&&!data.getStringExtra(PublishTaskContextEditActivity.param_data_content).isNullOrBlank()){
                        content=data.getStringExtra(PublishTaskContextEditActivity.param_data_content)
                    }
                }
            }
        }
    }


}
