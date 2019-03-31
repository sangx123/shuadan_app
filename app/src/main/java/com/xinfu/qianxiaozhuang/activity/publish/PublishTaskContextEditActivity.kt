package com.xinfu.qianxiaozhuang.activity.publish

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.jakewharton.rxbinding2.view.RxView
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.sendtion.xrichtext.RichTextEditor
import com.tbruyelle.rxpermissions2.Permission
import com.tbruyelle.rxpermissions2.RxPermissions
import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.activity.BaseActivity
import com.xinfu.qianxiaozhuang.api.model.QiNiuResponseBean
import com.xinfu.qianxiaozhuang.utils.*
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_publish_task_context_edit.*
import kotlinx.android.synthetic.main.common_tool_bar.view.*
import org.jetbrains.anko.*

class PublishTaskContextEditActivity : BaseActivity() {
    private var myContent=""
    lateinit var selectList: MutableList<LocalMedia>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish_task_context_edit)
        initUI()
    }

    private fun initUI() {

        mTitleBar.setRightOnClickListener(View.OnClickListener {
            callGallery()
        })
        RxView.clicks(mTitleBar.mTvRight)
                // Ask for permissions when button is clicked
                .compose(RxPermissions(this).ensureEach(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                .subscribe(object: Observer<Permission> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(permission: Permission) {
                        if (permission.granted) {
                            callGallery()
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // Denied permission without ask never again
                            toast("Denied permission without ask never again")
                        } else {
                            // Denied permission with ask never again
                            // Need to go to the settings
                            toast("Permission denied, can't enable the camera")
                        }
                    }

                    override fun onError(e: Throwable) {
                        toast("onError")
                    }

                })


        // 图片删除事件
        et_new_content.setOnRtImageDeleteListener { imagePath ->
            if (!TextUtils.isEmpty(imagePath)) {
                val isOK = SDCardUtil.deleteFile(imagePath)
                if (isOK) {
                    toast("删除成功：$imagePath")
                }
            }
        }
        // 图片点击事件
        et_new_content.setOnRtImageClickListener(RichTextEditor.OnRtImageClickListener { imagePath ->
            myContent = getEditData()
            if (!TextUtils.isEmpty(myContent)) {
                val imageList = StringUtils.getTextFromHtml(myContent, true)
                if (!TextUtils.isEmpty(imagePath)) {
                    val currentPosition = imageList.indexOf(imagePath)
                    toast("点击图片：$currentPosition：$imagePath")
                }
            }
        })
    }

    /**
     * 负责处理编辑数据提交等事宜，请自行实现
     */
    private fun getEditData(): String {
        val editList = et_new_content.buildEditData()
        val content = StringBuilder()
        for (itemData in editList) {
            if (itemData.inputStr != null) {
                content.append(itemData.inputStr)
            } else if (itemData.imagePath != null) {
                content.append("<img src=\"").append(itemData.imagePath).append("\"/>")
            }
        }
        return content.toString()
    }

    /**
     * 关闭软键盘
     */
    private fun closeSoftKeyInput() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        //boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开
        if (imm != null && imm.isActive && currentFocus != null) {
            imm.hideSoftInputFromWindow(currentFocus.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS)
            //imm.hideSoftInputFromInputMethod();//据说无效
            //imm.hideSoftInputFromWindow(et_content.getWindowToken(), 0); //强制隐藏键盘
            //如果输入法在窗口上已经显示，则隐藏，反之则显示
            //imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 打开软键盘
     */
    private fun openSoftKeyInput() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        //boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开
        if (imm != null && !imm.isActive && et_new_content != null) {
            et_new_content.requestFocus()
            //第二个参数可设置为0
            //imm.showSoftInput(et_content, InputMethodManager.SHOW_FORCED);//强制显示
            imm.showSoftInputFromInputMethod(et_new_content.windowToken,
                    InputMethodManager.SHOW_FORCED)
        }
    }


    /**
     * 调用图库选择
     */
    fun  callGallery(){
        //调用系统图库
            PictureSelector.create(this@PublishTaskContextEditActivity)
                    .openGallery(PictureMimeType.ofImage())
                    // .selectionMedia(selectList)// 是否传入已选图片
                    .maxSelectNum(1)
                    //.compress(true)
                    .forResult(PictureConfig.CHOOSE_REQUEST)
    }

    /**
     * 调用图库回调
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data)
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
//                    for (media in selectList) {
//                        Log.i("图片-----》", media.getPath())
//                    }
                    //adapter.setList(selectList)
                    //adapter.notifyDataSetChanged()

                    selectList?.let {
                        if (it.size > 0) {
                            //先上传图片
                            uploadImage(it[0].path)
                        }
                    }

                }
            }
        }
    }

    private fun uploadImage(imagePath: String) {
        LogUtil.e("sangxiang", "uploadImage");
        val uploader = SingleFileUploadUtil(imagePath, uploadListener)
        uploader.start()
        showApiProgress()
    }
    private val uploadListener = object : SingleFileUploadListener {
        override fun onStartGetToken(path: String) {
            LogUtil.e("sangxiang", "onStartGetToken ${path}");
        }

        override fun onStartUpload(path: String) {
            //LogUtil.e(TAG, "onStartUpload $path");
        }

        val progressMap = hashMapOf<String, Int>()
        override fun onUploadProgress(path: String, percentage: Int, bytesInProgress: Long, totalBytes: Long) {
            //LogUtil.e("sangxiang", "$path uploading: $percentage %")
//            for (taskItem in mData) {
//                taskItem.taskSubmit.executeImgArr.forEach {
//                    if (it.localPath == path) {
//                        it.progress = percentage
//                        val lastProgress = progressMap.get(path)
//                        if (lastProgress == null || lastProgress != percentage) {
//                            progressMap.put(path, percentage)
//                            mViewPositionMap.forEach {
//                                LogUtil.e(TAG, "view map iterator ${it.key}   it.value.third?.taskItemID --> ${it.value.third?.taskItemID}  |  ${taskItem.taskItemID}")
//                                if (it.value.third?.taskItemID == taskItem.taskItemID) {
//                                    LogUtil.e(TAG, "notifiy data Item Changed:${it.key}")
//                                    notifyItemChanged(it.key)
//                                }
//                            }
//                        }
//                    }
//                }
//            }
        }

        override fun onUploadSucceed(path: String, resp: QiNiuResponseBean) {

            MainHandler.post({
                hideApiProgress()
                this@PublishTaskContextEditActivity.toast("图片上传成功")

                insertImagesSync(path)
//                for (item in mAdapter.images) {
//                    if (item.path == resp.key) {
//                        item.uploadStatus = UploadStatus.SUCCESS.status
//                        item.url = resp.url
//                    }
//                }
//                mAdapter.notifyDataSetChanged()
            }, 0)
//            for (taskItem in mData) {
//                taskItem.taskSubmit.executeImgArr.forEach {
//                    LogUtil.e(TAG, "onUploadSucceed: ${it.localPath} : ${resp.key} url-->${resp.url}");
//                    if (it.localPath == resp.key) {
////                         it.imgUrl = resp.url
//                        LogUtil.e(TAG, "onUploadSucceed:set image url!!!----!!!");
//                        it.setImageUrl(resp.url)
//                        it.uploadStatus = UploadStatus.SUCCESS
//                        MainHandler.post({ notifyDataSetChanged() }, 0)
//                    }
//                    //save to db
//                }
//            }
//            LogUtil.e(TAG, mData.joinToString { it.taskSubmit.toString() + "---" })
        }

        override fun onUploadFailed(path: String) {
            MainHandler.post({
                hideApiProgress()
                this@PublishTaskContextEditActivity.toast("图片上传失败,请重新上传！")
//                for (i in mAdapter.images.indices) {
//                    var item =mAdapter.images[i]
//                    if (item.path == path) {
//                        item.uploadStatus = UploadStatus.FAIL.status
//                        mAdapter.images.removeAt(i)
//                    }
//                }
//                mAdapter.notifyDataSetChanged()
            }, 0)
//                }
//            }
        }

        override fun onUploadCancel(path: String) {
            hideApiProgress()
        }
    }

    private fun insertImagesSync(path: String) {
        //insertDialog.show()
        showApiProgress()
        Observable.create(ObservableOnSubscribe<String> { emitter ->
            try {
                et_new_content.measure(0, 0)
                emitter.onNext(path)
                emitter.onComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.onError(e)
            }
        })
                //.onBackpressureBuffer()
                .subscribeOn(Schedulers.io())//生产事件在io
                .observeOn(AndroidSchedulers.mainThread())//消费事件在UI线程
                .subscribe(object : Observer<String> {
                    override fun onComplete() {
                        hideApiProgress()
                        toast("图片插入成功")
                    }

                    override fun onError(e: Throwable) {
                        hideApiProgress()
                        toast("图片插入失败:" + e.message)
                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(imagePath: String) {
                        et_new_content.insertImage(imagePath, et_new_content.measuredWidth)
                    }
                })
    }


}
