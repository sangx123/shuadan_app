package com.xinfu.qianxiaozhuang.activity.publish

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.jakewharton.rxbinding2.view.RxView
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.orhanobut.hawk.Hawk
import com.sendtion.xrichtext.RichTextEditor
import com.tbruyelle.rxpermissions2.Permission
import com.tbruyelle.rxpermissions2.RxPermissions
import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.RequestCodeConfig
import com.xinfu.qianxiaozhuang.SpConfig
import com.xinfu.qianxiaozhuang.activity.BaseActivity
import com.xinfu.qianxiaozhuang.api.Api
import com.xinfu.qianxiaozhuang.api.BaseResult
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
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.*
import java.io.File

class PublishTaskContextEditActivity : BaseActivity() {
    private var myContent=""
    lateinit var selectList: MutableList<LocalMedia>
    companion object {
        var param_data_content="param_data_content"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish_task_context_edit)
        if(!intent.getStringExtra(param_data_content).isNullOrBlank()){
            myContent=intent.getStringExtra(param_data_content)
        }
        initUI()
    }

    private fun initUI() {
        mTitleBar.setRightOnClickListener(View.OnClickListener {
            var intent =Intent();
            intent.putExtra("param_data_content",getEditData())
            setResult(RequestCodeConfig.result200,intent)
            finish()
        })
        RxView.clicks(mAddImage)
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
                            toast("权限被禁止！")
                        } else {
                            // Denied permission with ask never again
                            // Need to go to the settings
                            toast("权限被禁止，请到设置界面开启权限！")
                        }
                    }

                    override fun onError(e: Throwable) {
                        toast("onError")
                    }

                })




        et_new_content.post {
            et_new_content.clearAllLayout()
            showDataSync(myContent)
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
    }

    /**
     * 负责处理编辑数据提交等事宜，请自行实现
     */
    private fun getEditData(): String {
        val editList = et_new_content.buildEditData()
        val content = StringBuilder()
        for (itemData in editList) {
            if (itemData.inputStr != null) {
                content.append("<line>"+itemData.inputStr+"</line>")
            } else if (itemData.imagePath != null) {
                content.append("<line>"+"<img src=\"").append(itemData.imagePath).append("\"/>"+"</line>")
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
        closeSoftKeyInput()//关闭软键盘
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
                            //uploadImage(it[0].path)
                            //uploadImageList(it)
                            //默认只能1张张图片上传
                            insertImagesSync(it[0].path)
                        }
                    }

                }
            }
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
                        if (et_new_content != null) {
                            //在图片全部插入完毕后，再插入一个EditText，防止最后一张图片后无法插入文字
                            if(myContent.isNullOrBlank())
                            et_new_content.addEditTextAtIndex(et_new_content.lastIndex, "")
                        }
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onSubscribe(d: Disposable) {
                      mDisposables.add(d)
                    }

                    override fun onNext(text: String) {
                        if (et_new_content != null) {
                            if (text.contains("<img") && text.contains("src=")) {
                                //imagePath可能是本地路径，也可能是网络地址
                                val imagePath = StringUtils.getImgSrc(text)
                                //插入空的EditText，以便在图片前后插入文字
                                //et_new_content.addEditTextAtIndex(et_new_content.lastIndex, "")
                                et_new_content.addImageViewAtIndex(et_new_content.lastIndex, imagePath)
                            } else {
                                et_new_content.addEditTextAtIndex(et_new_content.lastIndex, text)
                            }
                        }
                    }
                })
    }
}


//    //上传多张图片
//    private fun uploadImageList(list:List<LocalMedia>){
//        LogUtil.e("sangxiang", "uploadImage");
//        showApiProgress()
//        var map = HashMap<String, RequestBody>()
//        map["content"] = RequestBody.create(MediaType.parse("multipart/form-data"), getEditData())
//
//        for(item in list){
//            var file=File(item.path)
//            map.put("imageList\";filename=\""+file.name,MultipartBody.create(MediaType.parse("multipart/form-data"),file))
//        }
//
//        Api.getApiService().uploadImageList(map)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(object : Observer<BaseResult<String>> {
//                    override fun onComplete() {
//                        hideApiProgress()
//                    }
//
//                    override fun onSubscribe(d: Disposable) {
//                        mDisposables.add(d)
//                    }
//
//                    override fun onNext(t: BaseResult<String>) {
////                            t.result?.let {
////                                toast("图片上传成功！")
////                            }
//
//                        insertImagesSync("http://"+t.data!!)
//                    }
//
//                    override fun onError(e: Throwable) {
//                        e.printStackTrace()
//                    }
//
//                })
//    }
//    //上传单张图片
//    private fun uploadImage(imagePath: String) {
//        LogUtil.e("sangxiang", "uploadImage");
//        showApiProgress()
//        getImageContent(imagePath)?.let {
//                        Api.getImageApiService().uploadImage(it)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(object : Observer<BaseResult<String>> {
//                        override fun onComplete() {
//                            hideApiProgress()
//                        }
//
//                        override fun onSubscribe(d: Disposable) {
//                            mDisposables.add(d)
//                        }
//
//                        override fun onNext(t: BaseResult<String>) {
////                            t.result?.let {
////                                toast("图片上传成功！")
////                            }
//
//                            insertImagesSync("http://"+t.data!!)
//                        }
//
//                        override fun onError(e: Throwable) {
//                            e.printStackTrace()
//                        }
//
//                    })
//        }
//                //?:toast("图片无效,请重新选择图片")
//
//    }

//    private fun getImageContent(mPhotoPath: String = ""): MultipartBody.Part? {
//        if (mPhotoPath.isNullOrBlank()) {
//            return null
//        }
//        val MIMETYPE_IMAGE_JPEG = "image/jpeg"
//        val file = File(mPhotoPath)
//        return MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse(MIMETYPE_IMAGE_JPEG), file))
//    }
//七牛图片上传
//    private val uploadListener = object : SingleFileUploadListener {
//        override fun onStartGetToken(path: String) {
//            LogUtil.e("sangxiang", "onStartGetToken ${path}");
//        }
//
//        override fun onStartUpload(path: String) {
//            //LogUtil.e(TAG, "onStartUpload $path");
//        }
//
//        val progressMap = hashMapOf<String, Int>()
//        override fun onUploadProgress(path: String, percentage: Int, bytesInProgress: Long, totalBytes: Long) {
//            //LogUtil.e("sangxiang", "$path uploading: $percentage %")
////            for (taskItem in mData) {
////                taskItem.taskSubmit.executeImgArr.forEach {
////                    if (it.localPath == path) {
////                        it.progress = percentage
////                        val lastProgress = progressMap.get(path)
////                        if (lastProgress == null || lastProgress != percentage) {
////                            progressMap.put(path, percentage)
////                            mViewPositionMap.forEach {
////                                LogUtil.e(TAG, "view map iterator ${it.key}   it.value.third?.taskItemID --> ${it.value.third?.taskItemID}  |  ${taskItem.taskItemID}")
////                                if (it.value.third?.taskItemID == taskItem.taskItemID) {
////                                    LogUtil.e(TAG, "notifiy data Item Changed:${it.key}")
////                                    notifyItemChanged(it.key)
////                                }
////                            }
////                        }
////                    }
////                }
////            }
//        }
//
//        override fun onUploadSucceed(path: String, resp: QiNiuResponseBean) {
//
//            MainHandler.post({
//                hideApiProgress()
//                this@PublishTaskContextEditActivity.toast("图片上传成功")
//
//                insertImagesSync(path)
////                for (item in mAdapter.images) {
////                    if (item.path == resp.key) {
////                        item.uploadStatus = UploadStatus.SUCCESS.status
////                        item.url = resp.url
////                    }
////                }
////                mAdapter.notifyDataSetChanged()
//            }, 0)
////            for (taskItem in mData) {
////                taskItem.taskSubmit.executeImgArr.forEach {
////                    LogUtil.e(TAG, "onUploadSucceed: ${it.localPath} : ${resp.key} url-->${resp.url}");
////                    if (it.localPath == resp.key) {
//////                         it.imgUrl = resp.url
////                        LogUtil.e(TAG, "onUploadSucceed:set image url!!!----!!!");
////                        it.setImageUrl(resp.url)
////                        it.uploadStatus = UploadStatus.SUCCESS
////                        MainHandler.post({ notifyDataSetChanged() }, 0)
////                    }
////                    //save to db
////                }
////            }
////            LogUtil.e(TAG, mData.joinToString { it.taskSubmit.toString() + "---" })
//        }
//
//        override fun onUploadFailed(path: String) {
//            MainHandler.post({
//                hideApiProgress()
//                this@PublishTaskContextEditActivity.toast("图片上传失败,请重新上传！")
////                for (i in mAdapter.images.indices) {
////                    var item =mAdapter.images[i]
////                    if (item.path == path) {
////                        item.uploadStatus = UploadStatus.FAIL.status
////                        mAdapter.images.removeAt(i)
////                    }
////                }
////                mAdapter.notifyDataSetChanged()
//            }, 0)
////                }
////            }
//        }
//
//        override fun onUploadCancel(path: String) {
//            hideApiProgress()
//        }
//    }
