package com.xinfu.qianxiaozhuang.api

import android.os.Bundle
import android.text.TextUtils
import com.orhanobut.hawk.Hawk
import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.SpConfig
import com.xinfu.qianxiaozhuang.activity.BaseActivity
import com.xinfu.qianxiaozhuang.api.model.*
import com.xinfu.qianxiaozhuang.api.model.params.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_test_api.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.io.File

class TestApiActivity : BaseActivity() ,AnkoLogger{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_api)
        Hawk.put<String>(SpConfig.accessToken,"b72f8f45abdf4415aa649133ce090f4f")
        Hawk.put(SpConfig.memberId,"xinqianbao3")
        test.onClick {
            getFeedBack()
        }

    }
    //////////////////////////////////////////////////我的相关


    /**
     *用户反馈
     */
    fun getFeedBack(){
        var model= FeedbackParam()
        model.content="110"
        Api.getApiService().getFeedback(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<FeedbackModel>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(t: BaseResult<FeedbackModel>) {
                        t.result?.let {
                            error { it.message }
                        }

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
    }

//    /**
//     *上传图片
//     */
//    fun getRefundUpload(mPhotoPath:String=""){
//        var model= FeedbackParam()
//
//        Api.getApiService().getRefundUpload(model,getImageContent(mPhotoPath))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(object : Observer<BaseResult<String>> {
//                    override fun onComplete() {
//
//                    }
//
//                    override fun onSubscribe(d: Disposable) {
//                        mDisposables.add(d)
//                    }
//
//                    override fun onNext(t: BaseResult<String>) {
//                        t.result?.let {
//                        }
//
//                    }
//
//                    override fun onError(e: Throwable) {
//                        e.printStackTrace()
//                    }
//
//                })
//    }
//
//    private fun getImageContent(mPhotoPath:String=""): MultipartBody.Part {
//        val MIMETYPE_IMAGE_JPEG = "image/jpeg"
//        val file = File(mPhotoPath)
//        return MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse(MIMETYPE_IMAGE_JPEG), file))
//    }

    /**
     * 借款记录
     */
    fun getLoanRecord(){
        var model= BaseParam()
        Api.getApiService().getLoanRecord(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<LoanRecordModel>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(t: BaseResult<LoanRecordModel>) {
                        t.result?.let {
                            error { "getLoanRecord 请求成功！" }
                        }

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
    }
    /**
     * 我的钱包
     */
    fun getMyWallet(){
        var model= BaseParam()
        Api.getApiService().getMyWallet(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<MyWalletModel>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(t: BaseResult<MyWalletModel>) {
                        t.result?.let {
                            error { "getMyWallet 请求成功！" }
                        }

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
    }

    /**
     * 急速退款页面
     */
    fun getRefundView(){
        var model= BaseParam()
        Api.getApiService().getRefundView(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<RefundViewModel>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(t: BaseResult<RefundViewModel>) {
                        t.result?.let {
                            error { "getRefundView 请求成功！" }
                        }

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
    }



    ////////////////////////////////////////////////////////借款相关

    /**
     * 认证接口
     */
    fun getExamineAndApprove(){
        var model= BaseParam()
        Api.getApiService().getExamineAndApprove(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<ExamineAndApproveModel>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(t: BaseResult<ExamineAndApproveModel>) {
                        t.result?.let {
                            error { "请求成功！" }
                        }

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
    }




    /**
     * 绑定银行卡页面
     */

//    fun getSignBankPayView(){
//        var model= BaseParam()
//        Api.getApiService().getSignBankPayView(model)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(object : Observer<BaseResult<String>> {
//                    override fun onComplete() {
//
//                    }
//
//                    override fun onSubscribe(d: Disposable) {
//                        mDisposables.add(d)
//                    }
//
//                    override fun onNext(t: BaseResult<String>) {
//                        t.result?.let {
//                            error { "请求成功！" }
//                        }
//
//                    }
//
//                    override fun onError(e: Throwable) {
//                        e.printStackTrace()
//                    }
//
//                })
//    }


    /**
     * 签约短信
     */

    fun getSignSms(){
        var model= SignSmsParam()
        Api.getApiService().getSignSms(model)
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
                            error { "请求成功！" }
                        }

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
    }


    /**
     * 签约银行卡
     */

    fun getSignBankPay(){
        var model= SignBankPayParam()
        Api.getApiService().getSignBankPay(model)
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
                            error { "请求成功！" }
                        }

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
    }

    /**
     * 评估数据
     */

    fun getCreditLimit(){
        var model= BaseParam()
        Api.getApiService().getCreditLimit(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<CreditLimitModel>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(t: BaseResult<CreditLimitModel>) {
                        t.result?.let {
                            error { "请求成功！" }
                        }

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
    }

    /**
     * 申请推送产品
     */

    fun getBorrow(){
        var model= BaseParam()
        Api.getApiService().getBorrow(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<PushResultModel>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(t: BaseResult<PushResultModel>) {
                        t.result?.let {
                            error { "请求成功！" }
                        }

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
    }


    /**
     * 客服
     */

    fun getCustomerService(){
        var model= BaseParam()
        Api.getApiService().getCustomerService()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<CustomerServiceModel>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(t: BaseResult<CustomerServiceModel>) {
                        t.result?.let {
                            error { "请求成功！" }
                        }

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
    }


//    /**
//     * 倒计时
//     */
//
//    fun getDeadline(){
//        var model= BaseParam()
//        Api.getApiService().getDeadline(model)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(object : Observer<BaseResult<String>> {
//                    override fun onComplete() {
//
//                    }
//
//                    override fun onSubscribe(d: Disposable) {
//                        mDisposables.add(d)
//                    }
//
//                    override fun onNext(t: BaseResult<String>) {
//                        t.result?.let {
//                            error { "请求成功！" }
//                        }
//
//                    }
//
//                    override fun onError(e: Throwable) {
//                        e.printStackTrace()
//                    }
//
//                })
//    }



}
