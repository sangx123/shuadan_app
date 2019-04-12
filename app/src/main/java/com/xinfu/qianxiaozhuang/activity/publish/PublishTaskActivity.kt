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
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.xinfu.qianxiaozhuang.activity.MainActivity
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.view.OptionsPickerView
import android.graphics.Color
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener
import kotlinx.android.synthetic.main.layout_key_value_item.view.*
import java.util.ArrayList


/**
 * 发布任务
 */
class PublishTaskActivity : BaseActivity() {
    var content = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish_task)
        initUI()
    }

    private fun initUI() {
        mContent.setOnClickListener {
            var intent = Intent(this@PublishTaskActivity, PublishTaskContextEditActivity::class.java)
            intent.putExtra(PublishTaskContextEditActivity.param_data_content, content)
            startActivityForResult(intent, RequestCodeConfig.request100)
        }

        mBtn.setOnClickListener {
            //error { content }
        }
        RxView.clicks(mBtn)
                .throttleFirst(3, TimeUnit.SECONDS)//在一秒内只取第一次点击
                .subscribe {
                    if(checkParam())
                    uploadImageList(mTitle.edit_content.text.toString(),mBenjin.text.toString(),mJiangli.text.toString(),mPeopleNum.text.toString())

                }
         val list = ArrayList<String>()
        list.add("淘宝")
        list.add("京东")
        list.add("拼多多")
        list.add("微信")
        list.add("其他")
        var pvOptions = OptionsPickerBuilder(this, OnOptionsSelectListener { options1, options2, options3, v ->
            mTaskType.setResult(list[options1])
        }).build<String>()
        pvOptions.setPicker(list)
        mTaskType.setOnClickListener {
            //条件选择器
            pvOptions.show()
        }
    }

    private fun checkParam(): Boolean {
        if (mTitle.edit_content.text.toString().isNullOrBlank()) {
            toast("请输入任务标题")
            return false
        }
        if (content.isNullOrBlank()) {
            toast("请输入任务内容")
            return false
        }
        if(mTaskType.mResult.isNullOrBlank()){
            toast("请选择任务类型")
            return false
        }

        if(mBenjin.text.toString().isNullOrBlank()){
            toast("请输入任务本金")
            return false
        }
        if(mJiangli.text.toString().isNullOrBlank()){
            toast("请输入任务奖励")
            return false
        }else{
            if(mJiangli.text.toString().toFloat()==0f){
                toast("任务奖励必须大于0")
                return false
            }
        }
        if(mPeopleNum.text.toString().isNullOrBlank()){
            toast("请输入任务人数")
            return false
        }else{
            if(mPeopleNum.text.toString().toFloat()==0f){
                toast("任务人数必须大于0")
                return false
            }
        }
        return true
    }

    //上传多张图片
    private fun uploadImageList(title: String, benJing: String, jiangLi: String, peopleNum: String) {
        val contentList = StringUtils.cutStringByLineTag(content)
        var list=ArrayList<String>()
        for (item in contentList) {
            if (item.contains("img")) {
                list.add(StringUtils.getImgSrc(item))
            }
        }
        showApiProgress()

        var map = HashMap<String, RequestBody>()
        map["content"] = RequestBody.create(MediaType.parse("multipart/form-data"), content)
        map["title"] = RequestBody.create(MediaType.parse("multipart/form-data"), title)
        map["benJing"] = RequestBody.create(MediaType.parse("multipart/form-data"), benJing)
        map["jiangLi"] = RequestBody.create(MediaType.parse("multipart/form-data"), jiangLi)
        map["peopleNum"] = RequestBody.create(MediaType.parse("multipart/form-data"), peopleNum)
        map["type"]= RequestBody.create(MediaType.parse("multipart/form-data"),mTaskType.mResult?:"")
        for (item in list) {
            var file = File(item)
            map.put("imageList\";filename=\"" + file.name, MultipartBody.create(MediaType.parse("multipart/form-data"), file))
        }

        Api.getApiService().createTask(map)
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
        when (requestCode) {
            RequestCodeConfig.request100 -> {
                if (resultCode == RequestCodeConfig.result200) {
                    if (data != null && !data.getStringExtra(PublishTaskContextEditActivity.param_data_content).isNullOrBlank()) {
                        content = data.getStringExtra(PublishTaskContextEditActivity.param_data_content)
                    }
                }
            }
        }
    }


}
