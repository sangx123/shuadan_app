package com.xinfu.qianxiaozhuang.activity.loan

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_query_back_money.*
import org.jetbrains.anko.startActivity

/**
 * 退款结果查询
 */
class QueryBackMoneyActivity : BaseActivity() {

    var statusCode: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query_back_money)
        statusCode = intent.getIntExtra("result", 0)
        initUI()
        insertData()
    }

    private fun initUI() {
        //titlebar_withdrawsetTitle(getString(R.string.result_check))
        //titlebar_withdrawsetTitleStyle(Typeface.DEFAULT_BOLD)
        //titlebar_withdrawsetTxtBackVisibility(View.VISIBLE)
        //titlebar_withdrawsetTitleCustomTextColor(resources.getColor(R.color.black))
        //titlebar_withdrawsetDrawableForTxtBack(R.drawable.icon_back)
        //titlebar_withdrawsetBackWidgetOnClick({ finish() }, null)


    }

    fun insertData() {

        displayView(txt_description, View.GONE)
        displayView(mBtn, View.GONE)
        if (statusCode == 5) {//已退款

            image.setImageResource(R.drawable.drawable_refunded)
            txt_result.setText(getString(R.string.refunded))
            displayView(txt_description, View.VISIBLE)
            txt_description.setText(getString(R.string.it_is_expected_to_arrive_within_one_working_day))

        } else if (statusCode == 1) {//审核中
            image.setImageResource(R.drawable.ic_passing)
            txt_result.setText(getString(R.string.refund_under_review))
            displayView(txt_description, View.VISIBLE)
            txt_description.setText(getString(R.string.the_refund_is_expected_to_be_completed_within_1_3_working_days))
        } else if (statusCode == 2) {//审核未通过(退款失败)
            image.setImageResource(R.drawable.ic_pass_no)
            txt_result.setText(getString(R.string.audit_failed))
            displayView(mBtn, View.VISIBLE)
            mBtn.setOnClickListener(View.OnClickListener {

                startActivity<ApplyLoanToBackMoneyActivity>()
            })
        }

    }

    override fun onNewIntent(intent: Intent?) {

        statusCode = intent!!.getIntExtra("result", 0)
        insertData()

    }


    /**
     * 显示或者隐藏控件
     */
    fun displayView(view: View, visible: Int) {
        if (view.visibility != visible) {
            view.visibility = visible
        }
    }

}
