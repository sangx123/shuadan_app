package com.xinfu.qianxiaozhuang.activity.loan

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.xiang.one.network.error.RxUtils
import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.activity.BaseActivity
import com.xinfu.qianxiaozhuang.activity.NoticeWebActivity
import com.xinfu.qianxiaozhuang.api.Api
import com.xinfu.qianxiaozhuang.api.BaseResult
import com.xinfu.qianxiaozhuang.api.model.ExamineAndApproveModel
import com.xinfu.qianxiaozhuang.api.model.params.BaseParam
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_auth_center.*
import org.jetbrains.anko.error
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

/**
 * 认证中心
 */
class AuthCenterActivity : BaseActivity() {
    var choice: Boolean = true
    var mExamineAndApproveModel:ExamineAndApproveModel?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_center)
        initUI()

    }

    override fun onResume() {
        super.onResume()
        getExamineAndApprove()
    }
    private fun initUI() {
        //titlebar_withdrawsetTitle("认证中心")
        //titlebar_withdrawsetTitleStyle(Typeface.DEFAULT_BOLD)
        //titlebar_withdrawsetTxtBackVisibility(View.VISIBLE)
        //titlebar_withdrawsetTitleCustomTextColor(resources.getColor(R.color.black))
        //titlebar_withdrawsetDrawableForTxtBack(R.drawable.icon_back)
        //titlebar_withdrawsetBackWidgetOnClick({ finish() }, null)

        mBtn.setOnClickListener {
            check()
            mExamineAndApproveModel?.let {
                if(it.memberInfo&&it.memberContact&&it.authenticateBank)
                    if (choice)
                        startActivity<ApplyLoanActivity>()
                    else {
                        toast("请同意<<评估及推荐服务协议>>")
                    }
            }

        }
        toUserInfoLayout.setOnClickListener {
            if(mExamineAndApproveModel==null||!mExamineAndApproveModel!!.memberInfo)
            startActivity<AuthUserInfoActivity>()
        }
        toUserContactLayout.setOnClickListener {
            if(mExamineAndApproveModel==null||!mExamineAndApproveModel!!.memberInfo){
                toast("请先完成个人信息")
                return@setOnClickListener
            }
            if(mExamineAndApproveModel!!.memberInfo&&!mExamineAndApproveModel!!.memberContact)
            startActivity<AuthUserContactActivity>()
        }

        toUserBankLayout.setOnClickListener {
            if(mExamineAndApproveModel==null||!mExamineAndApproveModel!!.memberInfo){
                toast("请先完成个人信息")
                return@setOnClickListener
            }
            if(!mExamineAndApproveModel!!.memberInfo&&!mExamineAndApproveModel!!.memberContact){
                toast("请先完成紧急联系人")
                return@setOnClickListener
            }
            if(mExamineAndApproveModel!!.memberInfo&&mExamineAndApproveModel!!.memberContact&&!mExamineAndApproveModel!!.authenticateBank)
                    startActivity<AuthUserBankActivity>()


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
            startActivity<NoticeWebActivity>(NoticeWebActivity.param_title to "评估及推荐服务协议",NoticeWebActivity.param_local_html to "file:///android_asset/assessment.html")
        }
    }


    fun check(){
        if(mExamineAndApproveModel==null||!mExamineAndApproveModel!!.memberInfo){
            toast("请先完成个人信息")
            return
        }
        if(!mExamineAndApproveModel!!.memberInfo&&!mExamineAndApproveModel!!.memberContact){
            toast("请先完成紧急联系人")
            return
        }
        if(mExamineAndApproveModel!!.memberInfo&&mExamineAndApproveModel!!.memberContact&&!mExamineAndApproveModel!!.authenticateBank){
            toast("请先完成绑定银行卡")
        }
    }
    /**
     * 认证接口
     */
    fun getExamineAndApprove() {
        var model = BaseParam()
        showApiProgress()
        Api.getApiService().getExamineAndApprove(model)
                .compose(RxUtils.handleGlobalError<BaseResult<ExamineAndApproveModel>>(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<ExamineAndApproveModel>> {
                    override fun onComplete() {
                        hideApiProgress()
                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(t: BaseResult<ExamineAndApproveModel>) {
                        t.result?.let {
                            showText(it.memberInfo, tv_executor_count)
                            showText(it.memberContact, tv_executor_count1)
                            showText(it.authenticateBank, tv_executor_count2)

                            showImage(it.memberInfo, img1)
                            showImage(it.memberContact, img2)
                            showImage(it.authenticateBank, img3)

                            mExamineAndApproveModel=it
                        }

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
    }

    fun showText(has: Boolean, text: TextView) {
        when (has) {
            true -> {
                text.setTextColor(resources.getColor(R.color.color_30A3FF))
                text.text = "已认证"

            }
            false -> {
                text.setTextColor(resources.getColor(R.color.color_cccccc))
                text.text = "未认证"
            }
        }
    }

    fun showImage(has: Boolean, img: ImageView) {
        when (has) {
            true -> {
                img.setImageDrawable(resources.getDrawable(R.drawable.ic_arrow_right_blue))
            }
            false -> {
                img.setImageDrawable(resources.getDrawable(R.drawable.ic_arrow_right_gray))

            }
        }
    }

}
