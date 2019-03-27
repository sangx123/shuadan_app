package com.xinfu.qianxiaozhuang.activity.loan

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import com.xiang.one.network.error.RxUtils
import com.xinfu.qianxiaozhuang.BuildConfig
import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.activity.BaseActivity
import com.xinfu.qianxiaozhuang.activity.NoticeWebActivity
import com.xinfu.qianxiaozhuang.api.Api
import com.xinfu.qianxiaozhuang.api.BaseResult
import com.xinfu.qianxiaozhuang.api.model.CreditLimitModel
import com.xinfu.qianxiaozhuang.api.model.params.BaseParam
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_apply_loan.*
import org.jetbrains.anko.error
import org.jetbrains.anko.startActivity
import android.system.Os.link
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import com.xinfu.qianxiaozhuang.api.BaseRequest



/**
 * 借款申请--评估数据
 */
class ApplyLoanActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_loan)
        initUI()
        progress_circular.setProgressNum(190)
        getCreditLimit()
    }

    private fun initUI() {
        //titlebar_withdrawsetTitle("评估数据")
        //titlebar_withdrawsetTitleStyle(Typeface.DEFAULT_BOLD)
        //titlebar_withdrawsetTxtBackVisibility(View.VISIBLE)
        //titlebar_withdrawsetTitleCustomTextColor(resources.getColor(R.color.black))
        //titlebar_withdrawsetDrawableForTxtBack(R.drawable.icon_back)
        //titlebar_withdrawsetBackWidgetOnClick({finish()}, null)
        toGetMoneyBtn.setOnClickListener {
            startActivity<ApplyLoanToGetMoneyActivity>()
        }
        toApplyLoanReport.setOnClickListener {
            startActivity<NoticeWebActivity>(NoticeWebActivity.param_title to "评估报告",NoticeWebActivity.param_httpUrl to BuildConfig.HOST_URL+"index/myReport",NoticeWebActivity.param_postData to BaseRequest().commonJsonData)
        }

    }


    /**
     * 评估数据
     */

    fun getCreditLimit(){
        var model= BaseParam()
        showApiProgress()
        Api.getApiService().getCreditLimit(model)
                .compose(RxUtils.handleGlobalError<BaseResult<CreditLimitModel>>(this@ApplyLoanActivity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<CreditLimitModel>> {
                    override fun onComplete() {
                            hideApiProgress()
                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(t: BaseResult<CreditLimitModel>) {
                        t.result?.let {
                            mNum.text = it.creditLimit.toString()
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
    }

}
