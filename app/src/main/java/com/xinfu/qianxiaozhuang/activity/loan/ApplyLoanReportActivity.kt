package com.xinfu.qianxiaozhuang.activity.loan

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_apply_loan_report.*

/**
 * 借款申请---评估报告
 */
class ApplyLoanReportActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_loan_report)
        initUI()
    }
    private fun initUI() {
        //titlebar_withdrawsetTitle("评估报告")
        //titlebar_withdrawsetTitleStyle(Typeface.DEFAULT_BOLD)
        //titlebar_withdrawsetTxtBackVisibility(View.VISIBLE)
        //titlebar_withdrawsetTitleCustomTextColor(resources.getColor(R.color.black))
        //titlebar_withdrawsetDrawableForTxtBack(R.drawable.icon_back)
        //titlebar_withdrawsetBackWidgetOnClick({finish()}, null)
    }
}
