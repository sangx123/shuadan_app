package com.xinfu.qianxiaozhuang.activity.home

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.orhanobut.hawk.Hawk
import com.sendtion.xrichtext.RichTextEditor
import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.activity.BaseActivity
import com.xinfu.qianxiaozhuang.api.Api
import com.xinfu.qianxiaozhuang.api.BaseResult
import com.xinfu.qianxiaozhuang.api.model.Task
import com.xinfu.qianxiaozhuang.api.model.params.ApplyTaskParam
import com.xinfu.qianxiaozhuang.utils.DataUtil
import com.xinfu.qianxiaozhuang.utils.SDCardUtil
import com.xinfu.qianxiaozhuang.utils.StringUtils
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_task_detail.*
import org.jetbrains.anko.*

class TaskDetailActivity : BaseActivity() {
    lateinit var task: Task
    var myContent = ""

    companion object {
        const val param_data_task = "param_data_task"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)
        initUI()
    }

    private fun initUI() {

        intent.getSerializableExtra(param_data_task)?.let {
            task = it as Task
        }

        if (task == null) {
            toast("参数传递错误！")
            finish()
            return
        }
        myContent=task.content
        tv_note_title.text = task.title
        tv_note_content.post {
            tv_note_content.clearAllLayout()
            showDataSync(myContent)
        }
        tv_note_time.text = DataUtil.getStringFromDate(task.createTime)

        uiBtnSubmit.setOnClickListener {
            getApplyTask()
        }

    }


    /**
     * 异步方式显示数据
     * @param html
     */
    private fun showDataSync(html: String) {

        Observable.create(ObservableOnSubscribe<String> { emitter -> showEditData(emitter, html) })
                //.onBackpressureBuffer()
                .subscribeOn(Schedulers.io())//生产事件在io
                .observeOn(AndroidSchedulers.mainThread())//消费事件在UI线程
                .subscribe(object : Observer<String> {
                    override fun onComplete() {

                    }

                    override fun onError(e: Throwable) {
                        toast("解析错误：图片不存在或已损坏")
                        error { "onError: " + e.message }
                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(text: String) {
                        try {
                            if (tv_note_content != null) {
                                if (text.contains("<img") && text.contains("src=")) {
                                    //imagePath可能是本地路径，也可能是网络地址
                                    val imagePath = StringUtils.getImgSrc(text)
                                    tv_note_content.addImageViewAtIndex(tv_note_content.lastIndex, imagePath)
                                } else {
                                    tv_note_content.addTextViewAtIndex(tv_note_content.lastIndex, text)
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }
                })

    }

    /**
     * 显示数据
     */
    protected fun showEditData(emitter: ObservableEmitter<String>, html: String) {
        try {
            val textList = StringUtils.cutStringByLineTag(html)
            for (i in textList.indices) {
                val text = textList[i]
                emitter.onNext(text)
            }
            emitter.onComplete()
        } catch (e: Exception) {
            e.printStackTrace()
            emitter.onError(e)
        }
    }


    /**
     *申请任务
     */
    fun getApplyTask(){
        var model= ApplyTaskParam()
        model.taskid=task.id
        Api.getApiService().getApplyTask(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<String>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(t: BaseResult<String>) {
                        t.result?.let {
                            toast(it)
                        }

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
    }

}
