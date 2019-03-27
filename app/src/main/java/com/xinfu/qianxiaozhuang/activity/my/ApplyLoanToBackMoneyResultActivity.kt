package com.xinfu.qianxiaozhuang.activity.my

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_apply_loan_to_back_money_result.*

/**
 * 借款申请---急速退款审核结果
 */
class ApplyLoanToBackMoneyResultActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_loan_to_back_money_result)
        initUI()
    }
    private fun initUI() {
        //titlebar_withdrawsetTitle("结果查询")
        //titlebar_withdrawsetTitleStyle(Typeface.DEFAULT_BOLD)
        //titlebar_withdrawsetTxtBackVisibility(View.VISIBLE)
        //titlebar_withdrawsetTitleCustomTextColor(resources.getColor(R.color.white))
        //titlebar_withdrawsetDrawableForTxtBack(R.drawable.icon_back)
        //titlebar_withdrawsetBackWidgetOnClick({finish()}, null)
    }
}
