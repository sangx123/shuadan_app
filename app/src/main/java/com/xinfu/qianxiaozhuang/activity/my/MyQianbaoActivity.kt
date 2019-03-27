package com.xinfu.qianxiaozhuang.activity.my

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import com.xiang.one.network.error.RxUtils
import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.activity.BaseActivity
import com.xinfu.qianxiaozhuang.api.Api
import com.xinfu.qianxiaozhuang.api.BaseResult
import com.xinfu.qianxiaozhuang.api.model.MyWalletModel
import com.xinfu.qianxiaozhuang.api.model.params.BaseParam
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_my_qianbao.*
import kotlinx.android.synthetic.main.layout_key_value_item.view.*
import org.jetbrains.anko.error
import org.jetbrains.anko.textColor
import org.jetbrains.anko.toast

/**
 * 我的钱包
 */
class MyQianbaoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_qianbao)
       initUI()
        getMyWallet()
    }
    private fun initUI() {
        //titlebar_withdrawsetTitle("我的钱包")
        //titlebar_withdrawsetTitleStyle(Typeface.DEFAULT_BOLD)
        //titlebar_withdrawsetTxtBackVisibility(View.VISIBLE)
        //titlebar_withdrawsetTitleCustomTextColor(resources.getColor(R.color.black))
        //titlebar_withdrawsetDrawableForTxtBack(R.drawable.icon_back)
        //titlebar_withdrawsetBackWidgetOnClick({finish()}, null)
        mBtn.setOnClickListener {
            toast("需满100元才能提现")
        }
    }

    /**
     * 我的钱包
     */
    fun getMyWallet(){
        var model= BaseParam()
        showApiProgress()
        Api.getApiService().getMyWallet(model)
                .compose(RxUtils.handleGlobalError<BaseResult<MyWalletModel>>(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<MyWalletModel>> {
                    override fun onComplete() {
                        hideApiProgress()
                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(t: BaseResult<MyWalletModel>) {
                        t.result?.let {
                            mMoney.setResult(it.cash.toString()+"元")
                            mYinghangka.setDest(it.authenticateBank!!)
                            mShouxufei.setResult(it.handlingFee.toString()+"元")
                            var left =if(it.cash-it.handlingFee>0){it.cash-it.handlingFee} else{0}
                            mShijidaozhang.setResult(left.toString()+"元")
                            mMoney.tv_content.textColor= resources.getColor(R.color.color_30A3FF)
                            mDesc.text="2、成功提现，手续费${it.handlingFee}元/次。"

                        }

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
    }
}
