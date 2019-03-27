package com.xinfu.qianxiaozhuang.activity.login

import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import cn.smssdk.EventHandler
import cn.smssdk.SMSSDK
import com.orhanobut.hawk.Hawk
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
import com.xinfu.qianxiaozhuang.utils.Utils
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

    //新增验证码功能
    private var eventHandler = object: EventHandler(){
        override fun afterEvent(event: Int, result: Int, data: Any) {
            // afterEvent会在子线程被调用，因此如果后续有UI相关操作，需要将数据发送到UI线程
            val msg = Message()
            msg.arg1 = event
            msg.arg2 = result
            msg.obj = data
            Handler(Looper.getMainLooper(), Handler.Callback { msg ->
                val event = msg!!.arg1
                val result = msg!!.arg2
                val data = msg!!.obj
                if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    hideApiProgress()
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        //处理成功得到验证码的结果 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                        toast("验证码已发送")
                    } else {
                        //处理错误的结果
                        toast("验证码发送失败")
                        (data as Throwable).printStackTrace()
                    }
                } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        //处理验证码验证通过的结果
                        Log.e("sangxiang","验证码验证通过！")
                        apiRegister()
                    } else {
                        //处理错误的结果
                        toast("验证码输入错误！")
                        (data as Throwable).printStackTrace()
                    }
                }
                //其他接口的返回结果也类似，根据event判断当前数据属于哪个接口
                false
            }).sendMessage(msg)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initView()
        SMSSDK.registerEventHandler(eventHandler)
    }

    private fun initView() {

        //titlebar_withdrawsetTitle(resources.getString(R.string.register))
        //titlebar_withdrawsetTitleStyle(Typeface.DEFAULT_BOLD)
        //titlebar_withdrawsetTxtBackVisibility(View.VISIBLE)
        //titlebar_withdrawsetTitleCustomTextColor(resources.getColor(R.color.black))
        //titlebar_withdrawsetDrawableForTxtBack(R.drawable.icon_back)
        //titlebar_withdrawsetBackWidgetOnClick(this, null)

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
            //验证验证码是否正确
            if(et_check_code.text.toString()=="000000") {
                //通用验证码不用校验
                //直接注册
                apiRegister()
            }else{
                SMSSDK.submitVerificationCode("86", et_input_mobile.text.toString(), et_check_code.text.toString())
            }
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
        showApiProgress()
        SMSSDK.getVerificationCode("86",et_input_mobile.text.toString())
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

    fun apiRegister(){
        if(et_input_mobile.text.toString().isNullOrBlank()){
            toast("手机号不能为空！")
            return;
        }
        var model= RegisterParam()
        model.name=et_input_mobile.text.toString()
        model.mobile=et_input_mobile.text.toString()
        model.password= Utils.getMd5Hash(et_input_password.text.toString())
        showApiProgress()
        Api.getApiService().register(model)
                .compose(RxUtils.handleGlobalError<BaseResult<LoginModel>>(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<LoginModel>> {
                    override fun onComplete() {
                        hideApiProgress()
                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(t: BaseResult<LoginModel>) {
                        toast("注册成功！")
//                        if(t.respCode== Config.SUCCESS) {
//                            Hawk.put<String>(SharePerferenceConfig.user_phone, model.mobile)
//                            Hawk.put<String>(SharePerferenceConfig.user_password, model.password)
//                            Hawk.put<String>(SharePerferenceConfig.userToken, t.data!!.userToken)
//                            Constants.setLoginUser(t.data)
//                            startActivity<MainActivity>()
//                            finish()
//                        }else{
//                            toast(t.respMsg)
//                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
    }

    override fun onDestroy() {
        super.onDestroy()
        SMSSDK.unregisterAllEventHandler()
    }


}
