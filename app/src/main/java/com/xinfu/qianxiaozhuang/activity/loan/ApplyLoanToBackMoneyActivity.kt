package com.xinfu.qianxiaozhuang.activity.loan

import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.xiang.one.network.error.RxUtils
import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.activity.BaseActivity
import com.xinfu.qianxiaozhuang.adapter.RefundPlatformListAdapter
import com.xinfu.qianxiaozhuang.api.Api
import com.xinfu.qianxiaozhuang.api.BaseResult
import com.xinfu.qianxiaozhuang.api.model.RefundViewItemlModel
import com.xinfu.qianxiaozhuang.api.model.RefundViewModel
import com.xinfu.qianxiaozhuang.api.model.params.BaseParam
import com.xinfu.qianxiaozhuang.widget.DividerItemDecoration
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_apply_loan_to_back_money.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.util.ArrayList

/**
 * 借款申请--急速退款
 */
class ApplyLoanToBackMoneyActivity : BaseActivity(), RefundPlatformListAdapter.IRecycleViewCallBack {


    var isRefund: Boolean = false//是否可以退款
    var reportId: String? = null//报告id
    var list: ArrayList<RefundViewItemlModel>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_loan_to_back_money)
        initUI()
        getRefundView()
    }

    private fun initUI() {
        //titlebar_withdrawsetTitle(getString(R.string.quick_refund))
        //titlebar_withdrawsetTitleStyle(Typeface.DEFAULT_BOLD)
        //titlebar_withdrawsetTxtBackVisibility(View.VISIBLE)
        //titlebar_withdrawsetTitleCustomTextColor(resources.getColor(R.color.black))
        //titlebar_withdrawsetDrawableForTxtBack(R.drawable.icon_back)
        //titlebar_withdrawsetBackWidgetOnClick({ finish() }, null)

        recyclerView.addItemDecoration(DividerItemDecoration(this@ApplyLoanToBackMoneyActivity, DividerItemDecoration.VERTICAL_LIST))
        recyclerView.setHasFixedSize(true)
        recyclerView.setLayoutManager(LinearLayoutManager(this@ApplyLoanToBackMoneyActivity))
    }


    /**
     * 急速退款页面
     */
    fun getRefundView() {
        var model = BaseParam()
        showApiProgress()
        Api.getApiService().getRefundView(model)
                .compose(RxUtils.handleGlobalError<BaseResult<RefundViewModel>>(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<RefundViewModel>> {
                    override fun onComplete() {
                        hideApiProgress()
                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(t: BaseResult<RefundViewModel>) {
                        if (t.getResultSuccess()) {
                            t.result?.let {
                                var refundViewModel: RefundViewModel = it
                                isRefund = refundViewModel.isRefund
                                reportId = refundViewModel.reportId
                                list = refundViewModel.list
                                if (list!!.size != 0) {
                                    var platformListAdapter: RefundPlatformListAdapter = RefundPlatformListAdapter(this@ApplyLoanToBackMoneyActivity, list, this@ApplyLoanToBackMoneyActivity)
                                    recyclerView.adapter = platformListAdapter
                                }

                            }
                        } else {

                            toast(t.message.toString())

                        }

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        //.putExtra("data", list)
                    }

                })
    }

    /**
     * 退款回调
     */
    override fun onRefundCallBack() {
        startActivity<ApplyLoanToBackMoneyUploadActivity>(ApplyLoanToBackMoneyUploadActivity.param_data to list,ApplyLoanToBackMoneyUploadActivity.param_reportID to reportId!!)
    }
}
