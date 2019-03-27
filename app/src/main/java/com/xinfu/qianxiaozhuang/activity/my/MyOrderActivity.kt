package com.xinfu.qianxiaozhuang.activity.my

import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.xiang.one.network.error.RxUtils
import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.activity.BaseActivity
import com.xinfu.qianxiaozhuang.adapter.MyOrderAdapter
import com.xinfu.qianxiaozhuang.api.Api
import com.xinfu.qianxiaozhuang.api.BaseResult
import com.xinfu.qianxiaozhuang.api.model.LoanRecordItemModel
import com.xinfu.qianxiaozhuang.api.model.LoanRecordModel
import com.xinfu.qianxiaozhuang.api.model.params.BaseParam
import com.xinfu.qianxiaozhuang.events.UserEvent
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_my_order.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast
import java.util.ArrayList

/**
 * 订单列表
 */
class MyOrderActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_order)
        EventBus.getDefault().register(this)

        initUI()
        getLoanRecord()
    }

    fun initUI() {

        //titlebar_withdrawsetTitle(resources.getString(R.string.order_list))
        //titlebar_withdrawsetTitleStyle(Typeface.DEFAULT_BOLD)
        //titlebar_withdrawsetTxtBackVisibility(View.VISIBLE)
        //titlebar_withdrawsetTitleCustomTextColor(resources.getColor(R.color.black))
        //titlebar_withdrawsetDrawableForTxtBack(R.drawable.icon_back)
        //titlebar_withdrawsetBackWidgetOnClick({ finish() }, null)

    }


    /**
     * 借款记录
     */
    fun getLoanRecord() {
        var model = BaseParam()
        showApiProgress()
        Api.getApiService().getOrderRecord(model)
                .compose(RxUtils.handleGlobalError<BaseResult<LoanRecordModel>>(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<LoanRecordModel>> {
                    override fun onComplete() {
                        hideApiProgress()
                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(t: BaseResult<LoanRecordModel>) {

                        if (t.getResultSuccess()) {
                            t.result?.let {
                                //非空判断

                                var loanRecordModel: LoanRecordModel = it
                                var list: ArrayList<LoanRecordItemModel>? = loanRecordModel.list
                                if (list != null && list.size > 0) {

                                    if (list.size > 0) {
                                        displayView(my_ListView, View.VISIBLE)
                                        displayView(layout_no_data, View.GONE)
                                        var myOrderAdapter: MyOrderAdapter = MyOrderAdapter(this@MyOrderActivity, list)
                                        my_ListView.adapter = myOrderAdapter
                                    } else {
                                        displayView(my_ListView, View.GONE)
                                        displayView(layout_no_data, View.VISIBLE)
                                    }
                                }

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

    //主线程接收消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUserEvent(event: UserEvent) {
        if (event.isRefreshForMyOrderActivity() === java.lang.Boolean.TRUE) {

            getLoanRecord()

        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

}
