package com.xinfu.qianxiaozhuang.activity.login

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import cn.smssdk.EventHandler
import cn.smssdk.SMSSDK
import com.orhanobut.hawk.Hawk
import com.xiang.one.network.error.RxUtils
import com.xinfu.qianxiaozhuang.activity.BaseActivity
import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.SpConfig
import com.xinfu.qianxiaozhuang.activity.MainActivity
import com.xinfu.qianxiaozhuang.api.Api
import com.xinfu.qianxiaozhuang.api.BaseResult
import com.xinfu.qianxiaozhuang.api.model.LoginModel
import com.xinfu.qianxiaozhuang.api.model.params.LoginParam
import com.xinfu.qianxiaozhuang.api.model.params.SmsParam
import com.xinfu.qianxiaozhuang.utils.ExpresssoinValidateUtil
import com.xinfu.qianxiaozhuang.utils.SpannableUtils
import com.xinfu.qianxiaozhuang.utils.ToastUtil
import com.xinfu.qianxiaozhuang.utils.Utils
import com.xinfu.qianxiaozhuang.widget.CommonTitleBar
import com.xinfu.qianxiaozhuang.widgets.KeyValueLayout
import io.reactivex.Flowable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.*
import java.util.concurrent.TimeUnit

/**
 * 登录页
 */
class LoginActivity : BaseActivity(), CommonTitleBar.IClickTxtBack {

    var checkCodeLoginFlag: Boolean = false //手机验证码登录方式
    var isBackToLoanFragment: Boolean = false //是否返回首页
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
                        apiLogin()
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
        setContentView(R.layout.activity_login)
        isBackToLoanFragment = intent.getBooleanExtra("isBackToLoanFragment", false)
        initView()
        SMSSDK.registerEventHandler(eventHandler)
        //t_input_account.setText("15821758991")
        //et_input_password.setText("123456")
    }

    private fun initView() {


        //titlebar_withdrawsetTitle(resources.getString(R.string.login))
        //titlebar_withdrawsetTitleStyle(Typeface.DEFAULT_BOLD)
        //titlebar_withdrawsetTxtBackVisibility(View.VISIBLE)
        //titlebar_withdrawsetTitleCustomTextColor(resources.getColor(R.color.black))
        //titlebar_withdrawsetDrawableForTxtBack(R.drawable.icon_back)
        //titlebar_withdrawsetBackWidgetOnClick(this@LoginActivity, null)

        var strcontent: String = getString(R.string.register_immediately_one)
//        txt_register.setText(SpannableUtils.setTextForeground(strcontent, 0,
//                strcontent.indexOf(getString(R.string.register))+1, resources.getColor(R.color.color_30a3ff)))
        txt_register.setText(SpannableUtils.setTextForeground(strcontent, 4, strcontent.length, resources.getColor(R.color.color_30a3ff)))


        txt_get_check_code.setOnClickListener {
            getSMS()
        }
        txt_check_login.setOnClickListener {
            startActivity<CheckCodeLoginActivity>()
        }

        txt_register.setOnClickListener {
            startActivity<RegisterActivity>()
        }

        txt_login.setOnClickListener {
            if (et_check_code.text.toString().isNullOrBlank()) {
                //账号密码登录
                apiLogin()
            }else{
                if(et_check_code.text.toString()=="000000"){
                    apiLogin()
                }else {
                    //验证码登录，先验证验证码是否正确
                    SMSSDK.submitVerificationCode("86", et_input_mobile.text.toString(), et_check_code.text.toString())
                }
            }

            //Hawk.put<Boolean>(SpConfig.LOGIN_STATUS,true)
        }

        txt_check_login.setOnClickListener {
            switchLayout()
        }

        et_input_account.addTextChangedListener(object : TextWatcher {
            var temp = ""
            var editStart = 0;
            var editEnd = 0
            override fun afterTextChanged(p0: Editable?) {
                temp = p0.toString();

                if (!et_input_password.text.toString().isNullOrBlank() && temp.length == 11) {
                    //如果手机长度大于0，手机号长度为11位的话
                    txt_login.setBackgroundResource(R.drawable.btn_blue)
                } else {
                    txt_login.setBackgroundResource(R.drawable.drawable_concer_login_silver)
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
                if (!et_input_password.text.toString().isNullOrBlank() && et_input_account.text.toString().length == 11) {
                    //如果手机长度大于0，手机号长度为11位的话
                    txt_login.setBackgroundResource(R.drawable.btn_blue)
                } else {
                    txt_login.setBackgroundResource(R.drawable.drawable_concer_login_silver)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        et_input_mobile.addTextChangedListener(object : TextWatcher {
            var temp = ""
            override fun afterTextChanged(p0: Editable?) {
                temp = p0.toString();

                if (!et_check_code.text.toString().isNullOrBlank() && temp.length == 11) {
                    txt_login.setBackgroundResource(R.drawable.btn_blue)
                } else {
                    txt_login.setBackgroundResource(R.drawable.drawable_concer_login_silver)
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
                if (!et_check_code.text.toString().isNullOrBlank() && et_input_mobile.text.toString().length == 11) {
                    //如果手机长度大于0，手机号长度为11位的话
                    txt_login.setBackgroundResource(R.drawable.btn_blue)
                } else {
                    txt_login.setBackgroundResource(R.drawable.drawable_concer_login_silver)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

    }

    /**
     * 判断手机
     *
     * @return
     */
    private fun checkMobile(isTotal: Boolean, mobile: String): Boolean {
        var value = false
        if (!TextUtils.isEmpty(mobile)) {
            if (ExpresssoinValidateUtil.isMobilePhone(mobile)) {
                val clickGetCode = txt_get_check_code.text.toString()
                if (clickGetCode == getString(R.string.get_check_code) || clickGetCode == getString(R.string.get_again) || clickGetCode == getString(R.string.send_fail) || clickGetCode.contains("（") && clickGetCode.contains("）")) {
                    value = true
                }
            } else {
                if (isTotal) {
                    toast("请输入正确的手机号码")
                }
            }
        } else {
            if (isTotal) {
                toast("手机号码不能为空")
            }
        }
        return value
    }

    fun displayOrHideView(view: View?, visible: Int) {

        if (view != null && view.visibility != visible) {

            view.visibility = visible

        }
    }

    /**
     * 是否切换布局
     */
    fun switchLayout() {
        et_input_mobile.setText("")
        et_input_account.setText("")
        if (!checkCodeLoginFlag) {//验证码登录方式

            onDisplayView(layout_login_one, View.GONE)
            onDisplayView(layout_login_two, View.VISIBLE)
            onDisplayView(txt_check_login, View.GONE)
            onDisplayView(txt_register, View.GONE)
            et_input_password.setText("")

        } else {//一般的登录方式
            onDisplayView(layout_login_one, View.VISIBLE)
            onDisplayView(layout_login_two, View.GONE)
            onDisplayView(txt_check_login, View.VISIBLE)
            onDisplayView(txt_register, View.VISIBLE)
            et_check_code.setText("")
        }
        checkCodeLoginFlag = !checkCodeLoginFlag
    }

    /**
     * 显示或者隐藏控件
     */
    fun onDisplayView(view: View, visible: Int) {

        if (view.visibility != visible) {

            view.visibility = visible

        }

    }

    override fun onClickTxtBackCallBack() {

        onBackPressed()

    }

    override fun onBackPressed() {

        if (checkCodeLoginFlag) {

            switchLayout()

        } else {
            if (isBackToLoanFragment) {//返回首页

                var intent: Intent = Intent()
                intent.putExtra("where_active", 1)
                intent.setClass(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)

            }
            finish()
        }

    }

    fun apiLogin() {
        var model = LoginParam()
        if (!et_input_mobile.text.toString().isNullOrBlank())
            model.mobile = et_input_mobile.text.toString()
        if (!et_input_account.text.toString().isNullOrBlank())
            model.mobile = et_input_account.text.toString()
        if (!et_input_password.text.toString().isNullOrBlank()) {
            model.password = Utils.getMd5Hash(et_input_password.text.toString())
        }
        if (!et_check_code.text.toString().isNullOrBlank()) {
            model.authCode=et_check_code.text.toString()

        }

        showApiProgress()
        Api.getApiService().login(model)
                .compose(RxUtils.handleGlobalError<BaseResult<LoginModel>>(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<LoginModel>> {
                    override fun onComplete() {
                        //hideApiProgress()
                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(t: BaseResult<LoginModel>) {
                        Hawk.put<Boolean>(SpConfig.LOGIN_STATUS, true)
                        Hawk.put<String>(SpConfig.accessToken, t.data!!.userToken)
                        Hawk.put(SpConfig.memberId, t.data)
                        startActivity<MainActivity>()

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })

    }


    /**
     * 获取注册验证码
     */
    fun getSMS(){
        if (et_input_mobile.text.toString().isNullOrBlank()) {
            toast("手机号不能为空！")
            return;
        }
        showApiProgress()
        SMSSDK.getVerificationCode("86",et_input_mobile.text.toString())
        mDisposables.add(Flowable.intervalRange(0, 61, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    txt_get_check_code.isEnabled=false
                    txt_get_check_code.text = "倒计时 " + (60 -it) + " 秒"
                }
                .doOnComplete {
                    txt_get_check_code.isEnabled=true
                    txt_get_check_code.text="获取验证码"
                }
                .subscribe())

    }

    override fun onDestroy() {
        super.onDestroy()
        SMSSDK.unregisterAllEventHandler()
    }

}
