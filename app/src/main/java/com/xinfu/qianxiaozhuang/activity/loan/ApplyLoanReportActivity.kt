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
        titlebar_withdraw.setTitle("评估报告")
        titlebar_withdraw.setTitleStyle(Typeface.DEFAULT_BOLD)
        titlebar_withdraw.setTxtBackVisibility(View.VISIBLE)
        titlebar_withdraw.setTitleCustomTextColor(resources.getColor(R.color.black))
        titlebar_withdraw.setDrawableForTxtBack(R.drawable.icon_back)
        titlebar_withdraw.setBackWidgetOnClick({finish()}, null)
    }
}
