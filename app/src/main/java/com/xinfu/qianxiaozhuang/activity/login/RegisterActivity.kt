package com.xinfu.qianxiaozhuang.activity.login

import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.xiang.one.network.error.RxUtils
import com.xinfu.qianxiaozhuang.activity.BaseActivity
import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.activity.NoticeWebActivity
import com.xinfu.qianxiaozhuang.api.Api
import com.xinfu.qianxiaozhuang.api.BaseResult
import com.xinfu.qianxiaozhuang.api.model.LoginModel
import com.xinfu.qianxiaozhuang.api.model.params.LoginParam
import com.xinfu.qianxiaozhuang.api.model.params.RegisterParam
import com.xinfu.qianxiaozhuang.api.model.params.SmsParam
import com.xinfu.qianxiaozhuang.widget.CommonTitleBar
import io.reactivex.Flowable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.error
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit

/**
 * 注册页
 */
class RegisterActivity : BaseActivity(), CommonTitleBar.IClickTxtBack {
    var choice=true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initView()
    }

    private fun initView() {

        titlebar_withdraw.setTitle(resources.getString(R.string.register))
        titlebar_withdraw.setTitleStyle(Typeface.DEFAULT_BOLD)
        titlebar_withdraw.setTxtBackVisibility(View.VISIBLE)
        titlebar_withdraw.setTitleCustomTextColor(resources.getColor(R.color.black))
        titlebar_withdraw.setDrawableForTxtBack(R.drawable.icon_back)
        titlebar_withdraw.setBackWidgetOnClick(this, null)

        //获取短信验证码
        txt_get_code.setOnClickListener {
            getSMS()
        }

        choiceLayout.setOnClickListener {
            choice = !choice
            if (choice) {
                choiceImg.setImageDrawable(resources.getDrawable(R.drawable.ic_choice_yes))
            } else {
                choiceImg.setImageDrawable(resources.getDrawable(R.drawable.ic_chose))
            }
        }
        toWeb.setOnClickListener {
            startActivity<NoticeWebActivity>(NoticeWebActivity.param_title to "平台用户注册服务协议",NoticeWebActivity.param_local_html to "file:///android_asset/assessment.html")
        }
        txt_register.setOnClickListener {
            if(!choice){
                toast("请同意《平台用户注册服务协议》")
                return@setOnClickListener
            }
            getRegister()
        }

        et_input_mobile.addTextChangedListener(object : TextWatcher {
            var temp = ""
            var editStart = 0;
            var editEnd = 0
            override fun afterTextChanged(p0: Editable?) {
                temp = p0.toString();

                if (!et_input_password.text.toString().isNullOrBlank()&&!et_check_code.text.toString().isNullOrBlank() && temp.length == 11) {
                    //如果手机长度大于0，手机号长度为11位的话
                    txt_register.setBackgroundResource(R.drawable.btn_blue)
                } else {
                    txt_register.setBackgroundResource(R.drawable.drawable_concer_login_silver)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        et_input_password.addTextChangedListener(object : TextWatcher {
            var temp = ""
            var editStart = 0;
            var editEnd = 0
            override fun afterTextChanged(p0: Editable?) {
                if (!et_input_password.text.toString().isNullOrBlank()&&!et_check_code.text.toString().isNullOrBlank() && et_input_mobile.text.toString().length == 11) {
                    //如果手机长度大于0，手机号长度为11位的话
                    txt_register.setBackgroundResource(R.drawable.btn_blue)
                } else {
                    txt_register.setBackgroundResource(R.drawable.drawable_concer_login_silver)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
        et_check_code.addTextChangedListener(object : TextWatcher {
            var temp = ""
            var editStart = 0;
            var editEnd = 0
            override fun afterTextChanged(p0: Editable?) {
                if (!et_input_password.text.toString().isNullOrBlank()&&!et_check_code.text.toString().isNullOrBlank() && et_input_mobile.text.toString().length== 11) {
                    //如果手机长度大于0，手机号长度为11位的话
                    txt_register.setBackgroundResource(R.drawable.btn_blue)
                } else {
                    txt_register.setBackgroundResource(R.drawable.drawable_concer_login_silver)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

    }

    override fun onClickTxtBackCallBack() {

        finish()

    }

    /**
     * 获取注册验证码
     */
    fun getSMS(){
        if(et_input_mobile.text.toString().isNullOrBlank()){
            toast("手机号不能为空！")
            return;
        }
        var model= SmsParam()
        model.mobile=et_input_mobile.text.toString()
        showApiProgress()
        Api.getApiService().getSMS(model)
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
                                    txt_get_code.isEnabled=false
                                    txt_get_code.text = "倒计时 " + (60 -it) + " 秒"
                                }
                                .doOnComplete {
                                    txt_get_code.isEnabled=true
                                    txt_get_code.text="获取验证码"
                                }
                                .subscribe())
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        toast("注册失败")
                    }

                })
    }

    /**
     * 注册
     */

    fun getRegister(){
        if(et_input_mobile.text.toString().isNullOrBlank()){
            toast("手机号不能为空！")
            return;
        }
        var model= RegisterParam()
        model.username=et_input_mobile.text.toString()
        model.password=et_input_password.text.toString()
        model.authCode=et_check_code.text.toString()
        showApiProgress()
        Api.getApiService().register(model)
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
                        //
                        toast("注册成功！")
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
    }


}
