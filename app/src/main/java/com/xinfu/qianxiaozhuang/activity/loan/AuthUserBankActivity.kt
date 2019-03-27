package com.xinfu.qianxiaozhuang.activity.loan

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import com.xiang.one.network.error.RxUtils
import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.activity.BaseActivity
import com.xinfu.qianxiaozhuang.activity.NoticeWebActivity
import com.xinfu.qianxiaozhuang.api.Api
import com.xinfu.qianxiaozhuang.api.BaseResult
import com.xinfu.qianxiaozhuang.api.model.SignBankPayViewModel
import com.xinfu.qianxiaozhuang.api.model.params.BaseParam
import com.xinfu.qianxiaozhuang.api.model.params.SignBankPayParam
import com.xinfu.qianxiaozhuang.api.model.params.SignSmsParam
import io.reactivex.Flowable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_auth_user_bank.*
import kotlinx.android.synthetic.main.activity_feedback.view.*
import kotlinx.android.synthetic.main.layout_key_value_item.view.*
import org.jetbrains.anko.error
import org.jetbrains.anko.*
import java.util.concurrent.TimeUnit

/**
 * 认证中心--添加银行卡
 */
class AuthUserBankActivity : BaseActivity() {
    var name=""
    var choice: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_user_bank)
        initUI()
        getSignBankPayView()


    }
    private fun initUI() {
        //titlebar_withdrawsetTitle("添加银行卡")
        //titlebar_withdrawsetTitleStyle(Typeface.DEFAULT_BOLD)
        //titlebar_withdrawsetTxtBackVisibility(View.VISIBLE)
        //titlebar_withdrawsetTitleCustomTextColor(resources.getColor(R.color.black))
        //titlebar_withdrawsetDrawableForTxtBack(R.drawable.icon_back)
        //titlebar_withdrawsetBackWidgetOnClick({finish()}, null)
        toWeb.setOnClickListener {
            startActivity<NoticeWebActivity>(NoticeWebActivity.param_title to "代扣服务协议",NoticeWebActivity.param_local_html to "file:///android_asset/deduct.html")
        }
        choiceLayout.setOnClickListener {
            choice = !choice
            if (choice) {
                choiceImg.setImageDrawable(resources.getDrawable(R.drawable.ic_choice_yes))
            } else {
                choiceImg.setImageDrawable(resources.getDrawable(R.drawable.ic_chose))
            }
        }
        //mName.edit_content.setText("桑享")
        //mName.edit_content.isEnabled=false
        mGetValidateCode.setOnClickListener {
            getSignSms()
        }


        mBtn.setOnClickListener {
            if(!choice){
                toast("请同意《代扣服务协议》")
                return@setOnClickListener
            }
            getSignBankPay()
        }


    }

    /**
     * 绑定银行卡页面
     */

    fun getSignBankPayView(){
        var model= BaseParam()
        showApiProgress()
        Api.getApiService().getSignBankPayView(model)
                .compose(RxUtils.handleGlobalError<BaseResult<SignBankPayViewModel>>(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<SignBankPayViewModel>> {
                    override fun onComplete() {
                        hideApiProgress()
                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(t: BaseResult<SignBankPayViewModel>) {
                        t.result?.let {
                            if(!it.realName.isNullOrBlank())
                            mName.setResult(it.realName)
                        }

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
    }

    /**
     * 签约短信
     */

    fun getSignSms(){
        if(mBankNum.edit_content.text.toString().isNullOrBlank()){
            toast("请填写银行卡号")
            return
        }
        if(mPhone.edit_content.text.toString().length!=11){
            toast("请填写11位手机号")
            return
        }
        var model= SignSmsParam()
        model.bankAccount=mBankNum.edit_content.text.toString()
        model.mobile=mPhone.edit_content.text.toString()
        showApiProgress()
        Api.getApiService().getSignSms(model)
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
                        //验证码请求成功就倒计时
                        mDisposables.add(Flowable.intervalRange(0, 61, 0, 1, TimeUnit.SECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnNext {
                                    mGetValidateCode.isEnabled=false
                                    mGetValidateCode.text = "倒计时 " + (60 -it) + " 秒"
                                }
                                .doOnComplete {
                                    mGetValidateCode.isEnabled=true
                                    mGetValidateCode.text="获取验证码"
                                }
                                .subscribe())

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
        if(mBankNum.edit_content.text.toString().isNullOrBlank()){
            toast("请填写银行卡号")
            return
        }
        if(mPhone.edit_content.text.toString().length!=11){
            toast("请填写11位手机号")
            return
        }
        if(mValidateCode.edit_content.text.toString().isNullOrBlank()){
            toast("验证码不能为空")
            return
        }
        var model= SignBankPayParam()
        model.bankAccount=mBankNum.edit_content.text.toString()
        model.mobile=mPhone.edit_content.text.toString()
        model.msgCode=mValidateCode.edit_content.text.toString()
        showApiProgress()
        Api.getApiService().getSignBankPay(model)
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
                        toast("保存成功！")
                        finish()

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
    }
}
