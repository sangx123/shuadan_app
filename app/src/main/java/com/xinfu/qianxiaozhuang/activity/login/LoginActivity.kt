package com.xinfu.qianxiaozhuang.activity.login

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
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
import com.xinfu.qianxiaozhuang.widget.CommonTitleBar
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        isBackToLoanFragment = intent.getBooleanExtra("isBackToLoanFragment", false)
        initView()
        //t_input_account.setText("15821758991")
        //et_input_password.setText("123456")
    }

    private fun initView() {


        titlebar_withdraw.setTitle(resources.getString(R.string.login))
        titlebar_withdraw.setTitleStyle(Typeface.DEFAULT_BOLD)
        titlebar_withdraw.setTxtBackVisibility(View.VISIBLE)
        titlebar_withdraw.setTitleCustomTextColor(resources.getColor(R.color.black))
        titlebar_withdraw.setDrawableForTxtBack(R.drawable.icon_back)
        titlebar_withdraw.setBackWidgetOnClick(this@LoginActivity, null)

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
            apiLogin()
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

//    private inner class MaxLengthWatcher(private val edClear: EditText) : TextWatcher {
//
//        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//
//        }
//
//        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//
//        }
//
//        override fun afterTextChanged(s: Editable) {
//            val txtAmount = s.toString()
//            if (!TextUtils.isEmpty(txtAmount) && txtAmount[0] == ' ') {
//                s.clear()
//                return
//            }
//
//            when (edClear.id) {
//
//                R.id.et_input_account -> {
//                    if (!TextUtils.isEmpty(txtAmount) && txtAmount[0] != ' ') {
//                        displayOrHideView(txt_mobile_display, View.VISIBLE)
//                        displayOrHideView(image_right_display, View.VISIBLE)
//                    } else {
//                        displayOrHideView(txt_mobile_display, View.GONE)
//                        displayOrHideView(image_right_display, View.GONE)
//                    }
//                    if (checkLoginAccount(false) && checkLoginPassword(false)) {
//                        onLoginStatus(true)
//                    } else {
//                        onLoginStatus(false)
//                    }
//                }
//                R.id.et_input_password -> {
//                    if (!TextUtils.isEmpty(txtAmount) && txtAmount[0] != ' ') {
//                        displayOrHideView(layout_password_flag_display, View.VISIBLE)
//                    } else {
//                        displayOrHideView(layout_password_flag_display, View.GONE)
//                    }
//                    if (checkLoginAccount(false) && checkLoginPassword(false)) {
//                        onLoginStatus(true)
//                    } else {
//                        onLoginStatus(false)
//                    }
//                }
//                R.id.et_input_mobile -> {
//                    if (!TextUtils.isEmpty(txtAmount) && txtAmount[0] != ' ') {
//                        displayOrHideView(txt_mobile_two_display, View.VISIBLE)
//                        displayOrHideView(image_right_display_two, View.VISIBLE)
//                    } else {
//                        displayOrHideView(txt_mobile_two_display, View.GONE)
//                        displayOrHideView(image_right_display_two, View.GONE)
//                    }
//                    if (checkMobile(false) && checkCode(false)) {
//                        onLoginStatus(true)
//                    } else {
//                        onLoginStatus(false)
//                    }
//                }
//                R.id.et_check_code -> {
//                    if (!TextUtils.isEmpty(txtAmount) && txtAmount[0] != ' ') {
//                        displayOrHideView(txt_code_display, View.VISIBLE)
//                    } else {
//                        displayOrHideView(txt_code_display, View.GONE)
//                    }
//                    if (checkMobile(false) && checkCode(false)) {
//                        onLoginStatus(true)
//                    } else {
//                        onLoginStatus(false)
//                    }
//                }
//            }
//        }
//    }
//
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
            model.username = et_input_mobile.text.toString()
        if (!et_input_account.text.toString().isNullOrBlank())
            model.username = et_input_account.text.toString()
        if (!et_input_password.text.toString().isNullOrBlank()) {
            model.password = et_input_password.text.toString()
        }
        if (!et_check_code.text.toString().isNullOrBlank()) {
            model.authCode = et_check_code.text.toString()
        }

        showApiProgress()
        Api.getApiService().login(model)
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
                        t.result?.let {
                            Hawk.put<Boolean>(SpConfig.LOGIN_STATUS, true)
                            Hawk.put<String>(SpConfig.accessToken, it.accessToken)
                            Hawk.put(SpConfig.memberId, it.memberId)
                            startActivity<MainActivity>()
                        }

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })

    }

    /**
     * 获取注册验证码
     */
    fun getSMS() {
        if (et_input_mobile.text.toString().isNullOrBlank()) {
            toast("手机号不能为空！")
            return;
        }
        var model = SmsParam()
        model.mobile = et_input_mobile.text.toString()
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
                                    txt_get_check_code.isEnabled = false
                                    txt_get_check_code.text = "倒计时 " + (60 - it) + " 秒"
                                }
                                .doOnComplete {
                                    txt_get_check_code.isEnabled = true
                                    txt_get_check_code.text = "获取验证码"
                                }
                                .subscribe())
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        toast("注册失败")
                    }

                })
    }

}
