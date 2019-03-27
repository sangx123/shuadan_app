package com.xinfu.qianxiaozhuang.activity.my

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import com.xiang.one.network.error.RxUtils.handleGlobalError
import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.activity.BaseActivity
import com.xinfu.qianxiaozhuang.api.Api
import com.xinfu.qianxiaozhuang.api.BaseResult
import com.xinfu.qianxiaozhuang.api.model.LoanRecordModel
import com.xinfu.qianxiaozhuang.api.model.params.BaseParam
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_my_qianbao.*
import org.jetbrains.anko.error

/**
 * 我的借款记录
 */
class MyJiekuanjiluActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_jiekuanjilu)
        initUI()
    }
    private fun initUI() {
        //titlebar_withdrawsetTitle("借款记录")
        //titlebar_withdrawsetTitleStyle(Typeface.DEFAULT_BOLD)
        //titlebar_withdrawsetTxtBackVisibility(View.VISIBLE)
        //titlebar_withdrawsetTitleCustomTextColor(resources.getColor(R.color.black))
        //titlebar_withdrawsetDrawableForTxtBack(R.drawable.icon_back)
        //titlebar_withdrawsetBackWidgetOnClick({finish()}, null)
    }
}
