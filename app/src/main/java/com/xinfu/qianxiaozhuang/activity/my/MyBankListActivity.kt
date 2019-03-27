package com.xinfu.qianxiaozhuang.activity.my

import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.xiang.one.network.error.RxUtils
import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.activity.BaseActivity
import com.xinfu.qianxiaozhuang.api.Api
import com.xinfu.qianxiaozhuang.api.BaseResult
import com.xinfu.qianxiaozhuang.api.model.BankListModel
import com.xinfu.qianxiaozhuang.api.model.params.BaseParam
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_my_bank_list.*
import org.jetbrains.anko.toast

/**
 * 银行卡列表
 */
class MyBankListActivity : BaseActivity() {

    var bankName: String? = null
    var authenticateBank: String? = null
    //var bankLogo: String? = null //预留字段

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_bank_list)

        initUI()
        getData()

    }

    fun initUI() {

        //titlebar_withdrawsetTitle(getString(R.string.bank_list))
        //titlebar_withdrawsetTitleStyle(Typeface.DEFAULT_BOLD)
        //titlebar_withdrawsetTxtBackVisibility(View.VISIBLE)
        //titlebar_withdrawsetTitleCustomTextColor(resources.getColor(R.color.black))
        //titlebar_withdrawsetDrawableForTxtBack(R.drawable.icon_back)
        //titlebar_withdrawsetBackWidgetOnClick({ finish() }, null)

    }


    /**
     * 获取用户中心的数据
     */
    fun getData() {

        var baseParam = BaseParam()
        showApiProgress()
        Api.getApiService().getBankList(baseParam)
                .compose(RxUtils.handleGlobalError<BaseResult<BankListModel>>(this@MyBankListActivity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<BankListModel>> {
                    override fun onComplete() {

                        if (!TextUtils.isEmpty(bankName) && !TextUtils.isEmpty(authenticateBank)) {
                            displayView(layout_content, View.VISIBLE)
                            displayView(layout_no_data, View.GONE)
                        } else {
                            displayView(layout_content, View.GONE)
                            displayView(layout_no_data, View.VISIBLE)
                        }
                        hideApiProgress()

                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(t: BaseResult<BankListModel>) {
                        if (t.getResultSuccess()) {
                            t.result?.let {
                                //非空情况执行
                                var bankListModel: BankListModel = it
                                bankName = bankListModel.bankName
                                authenticateBank = bankListModel.authenticateBank
                                //bankLogo = bankListModel.bankLogo

                                //GlideUtil.loadImageFive(this@MyBankListActivity, bankLogo, image_bank_logo)
                                txt_bank_name.setText(bankName)
                                txt_card_no.setText(authenticateBank)

                            }

                        } else {
                            toast(t.message.toString())
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
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
