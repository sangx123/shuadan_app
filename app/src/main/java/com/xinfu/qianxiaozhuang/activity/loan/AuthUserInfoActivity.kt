package com.xinfu.qianxiaozhuang.activity.loan

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.view.RxView
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.orhanobut.hawk.Hawk
import com.tbruyelle.rxpermissions2.Permission
import com.tbruyelle.rxpermissions2.RxPermissions
import com.xiang.one.network.error.RxUtils
import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.SpConfig
import com.xinfu.qianxiaozhuang.activity.BaseActivity
import com.xinfu.qianxiaozhuang.api.Api
import com.xinfu.qianxiaozhuang.api.BaseResult
import com.xinfu.qianxiaozhuang.api.InterfaceUseCase
import com.xinfu.qianxiaozhuang.api.model.IdCardDiscernModel
import com.xinfu.qianxiaozhuang.api.model.RefundUploadModel
import com.xinfu.qianxiaozhuang.api.model.params.RefundApplicationList
import com.xinfu.qianxiaozhuang.utils.GlideUtil
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_auth_user_info.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.error
import org.jetbrains.anko.toast
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import java.io.File
import java.util.ArrayList

/**
 * 认证中心----个人信息
 */
class AuthUserInfoActivity : BaseActivity() {
    companion object {
        val REQUEST_CAMERA1=3000;
        val REQUEST_CAMERA2=3001;
        val REQUEST_CAMERA3=3002;

    }
    var mIdCardDiscernModel:IdCardDiscernModel?=null
    var facePath=""
    private var selectList: List<LocalMedia> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_user_info)
        initUI()
    }
    private fun initUI() {
        //titlebar_withdrawsetTitle("个人信息")
        //titlebar_withdrawsetTitleStyle(Typeface.DEFAULT_BOLD)
        //titlebar_withdrawsetTxtBackVisibility(View.VISIBLE)
        //titlebar_withdrawsetTitleCustomTextColor(resources.getColor(R.color.black))
        //titlebar_withdrawsetDrawableForTxtBack(R.drawable.icon_back)
        //titlebar_withdrawsetBackWidgetOnClick({finish()}, null)
        mBtn.setOnClickListener {
                getIdCardCheck(facePath)
        }
        RxView.clicks(mFace)
                // Ask for permissions when button is clicked
                .compose(RxPermissions(this).ensureEach(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE))
                .subscribe(object: Observer<Permission> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(permission: Permission) {
                        if (permission.granted) {
                            PictureSelector.create(this@AuthUserInfoActivity)
                                    .openCamera(PictureMimeType.ofImage())
                                    //.selectionMedia(selectList)// 是否传入已选图片
                                    .forResult(REQUEST_CAMERA1)

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
        RxView.clicks(image1)
                // Ask for permissions when button is clicked
                .compose(RxPermissions(this).ensureEach(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE))
                .subscribe(object: Observer<Permission> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(permission: Permission) {
                        if (permission.granted) {
                            PictureSelector.create(this@AuthUserInfoActivity)
                                    .openCamera(PictureMimeType.ofImage())
                                    //.selectionMedia(selectList)// 是否传入已选图片
                                    .forResult(REQUEST_CAMERA2)

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
        RxView.clicks(image2)
                // Ask for permissions when button is clicked
                .compose(RxPermissions(this).ensureEach(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA))
                .subscribe(object: Observer<Permission> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(permission: Permission) {
                        if (permission.granted) {
                            PictureSelector.create(this@AuthUserInfoActivity)
                                    .openCamera(PictureMimeType.ofImage())
                                    //.selectionMedia(selectList)// 是否传入已选图片
                                    .forResult(REQUEST_CAMERA3)

                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // Denied permission without ask never again
                            toast("权限被拒绝!")
                        } else {
                            // Denied permission with ask never again
                            // Need to go to the settings
                            toast("权限被拒绝，请在设置中开启app的访问权限")
                        }
                    }

                    override fun onError(e: Throwable) {
                        toast("onError")
                    }

                })


    }


    /**
     * 身份证识别
     */
    fun getIdCardDiscern(mPhotoPath1:String=""){
        var map =HashMap<String, RequestBody>()
        map[SpConfig.memberId] = RequestBody.create(MediaType.parse("text/plain"), Hawk.get<String>(SpConfig.memberId))
        map[SpConfig.accessToken] = RequestBody.create(MediaType.parse("text/plain"), Hawk.get<String>(SpConfig.accessToken))
//        val file = File(mPhotoPath)
//        map["image"] = RequestBody.create(MediaType.parse("image/jpeg"), file)
        showApiProgress()
        Api.getApiService().getIdCardDiscern(map,getImageContent1(mPhotoPath1)!!)
                .compose(RxUtils.handleGlobalError<BaseResult<IdCardDiscernModel>>(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<IdCardDiscernModel>> {
                    override fun onComplete() {
                        hideApiProgress()
                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)

                    }

                    override fun onNext(t: BaseResult<IdCardDiscernModel>) {
                        t.result?.let {
                            mIdCardDiscernModel=it
                            mName.setText(it.NAME)
                            mIdCard.setText(it.NUM)
                        }

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CAMERA1 -> {
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data)
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    for (media in selectList) {
                        Log.i("图片1-----》", media.getPath())
                    }
                    if(selectList!=null&& selectList.isNotEmpty()){
                        facePath=selectList[0].path
                        Glide.with(this@AuthUserInfoActivity).load(File(facePath)).into(faceImage)
                    }

                    //adapter.setList(selectList)
                    //adapter.notifyDataSetChanged()
                }
                REQUEST_CAMERA2 -> {
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data)
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    for (media in selectList) {
                        Log.i("图片2-----》", media.getPath())
                    }
                    if(selectList!=null&& selectList.isNotEmpty()){
                        var path=selectList[0].path
                        Glide.with(this@AuthUserInfoActivity).load(File(path)).into(image1)
                        getIdCardDiscern(path)
                    }

                    //adapter.setList(selectList)
                    //adapter.notifyDataSetChanged()
                }
                REQUEST_CAMERA3 -> {
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data)
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    for (media in selectList) {
                        Log.i("图片3-----》", media.getPath())
                    }
                    if(selectList!=null&& selectList.isNotEmpty()){
                        Glide.with(this@AuthUserInfoActivity).load(File(selectList[0].path)).into(image2)
                    }
                    //adapter.setList(selectList)
                    //adapter.notifyDataSetChanged()
                }
            }
        }
    }

    /**
     * 保存身份数据
     */
    fun getIdCardCheck(mPhotoPath1:String=""){
        if(facePath.isNullOrBlank()){
            toast("请先上传人脸识别照片！")
        }
        if(mIdCardDiscernModel==null){
            toast("请先上传身份证正反面!")
            return
        }
        if(mName.text.toString().isNullOrBlank()){
            toast("姓名不能为空")
            return
        }
        if(mIdCard.text.toString().isNullOrBlank()){
            toast("身份证不能为空")
            return
        }

        var map =HashMap<String, RequestBody>()
        map[SpConfig.memberId] = RequestBody.create(MediaType.parse("text/plain"), Hawk.get<String>(SpConfig.memberId))
        map[SpConfig.accessToken] = RequestBody.create(MediaType.parse("text/plain"), Hawk.get<String>(SpConfig.accessToken))
        map["realName"] = RequestBody.create(MediaType.parse("text/plain"), mName.text.toString().trim())
        map["idcard"] = RequestBody.create(MediaType.parse("text/plain"), mIdCard.text.toString().trim())
//        val file = File(mPhotoPath)
//        map["image"] = RequestBody.create(MediaType.parse("image/jpeg"), file)
        showApiProgress()
        Api.getApiService().getIdCardCheck(map,getImageContent2(mPhotoPath1)!!)
                .compose(RxUtils.handleGlobalError<BaseResult<String>>(this))
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

                        toast("保存成功")
                        finish()

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
    }
    private fun getImageContent1(mPhotoPath:String=""): MultipartBody.Part? {
        if(mPhotoPath.isNullOrBlank()){
            return null
        }
        val MIMETYPE_IMAGE_JPEG = "image/jpeg"
        val file = File(mPhotoPath)
        return MultipartBody.Part.createFormData("idCardImage", file.getName(), RequestBody.create(MediaType.parse(MIMETYPE_IMAGE_JPEG), file))
    }
    private fun getImageContent2(mPhotoPath:String=""): MultipartBody.Part? {
        if(mPhotoPath.isNullOrBlank()){
            return null
        }
        val MIMETYPE_IMAGE_JPEG = "image/jpeg"
        val file = File(mPhotoPath)
        return MultipartBody.Part.createFormData("faceImage", file.getName(), RequestBody.create(MediaType.parse(MIMETYPE_IMAGE_JPEG), file))
    }

}
