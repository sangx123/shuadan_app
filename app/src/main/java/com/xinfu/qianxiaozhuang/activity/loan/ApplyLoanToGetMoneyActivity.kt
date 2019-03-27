package com.xinfu.qianxiaozhuang.activity.loan

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.xiang.one.network.error.RxUtils
import com.xiang.one.utils.dialog.AlertDialogNew
import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.activity.BaseActivity
import com.xinfu.qianxiaozhuang.adapter.PushPlatformListAdapter
import com.xinfu.qianxiaozhuang.api.Api
import com.xinfu.qianxiaozhuang.api.BaseResult
import com.xinfu.qianxiaozhuang.api.model.*
import com.xinfu.qianxiaozhuang.api.model.params.BaseParam
import com.xinfu.qianxiaozhuang.utils.Utils
import com.xinfu.qianxiaozhuang.widget.DividerItemDecoration
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_apply_loan_to_get_money.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.util.*

/**
 * 借款申请---去领钱推送结果
 */
class ApplyLoanToGetMoneyActivity : BaseActivity(), PushPlatformListAdapter.IRecycleViewCallBack {

    var creditLimit: Double? = 0.00// 预授信额度
    var isShow: Boolean? = false //扣费弹窗显示  false不显示
    var list: ArrayList<RefundViewItemlModel>? = null

    lateinit var dialog: AlertDialogNew
    lateinit var dialogDaojishi: AlertDialogNew
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_loan_to_get_money)
        initUI()
        getRefundView()
    }

    private fun initUI() {
        //titlebar_withdrawsetTitle("推送结果")
        //titlebar_withdrawsetTitleStyle(Typeface.DEFAULT_BOLD)
        //titlebar_withdrawsetTxtBackVisibility(View.VISIBLE)
        //titlebar_withdrawsetTitleCustomTextColor(resources.getColor(R.color.black))
        //titlebar_withdrawsetDrawableForTxtBack(R.drawable.icon_back)
        //titlebar_withdrawsetBackWidgetOnClick({ finish() }, null)
        dialog = AlertDialogNew(this)
        dialogDaojishi = AlertDialogNew(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this@ApplyLoanToGetMoneyActivity, DividerItemDecoration.VERTICAL_LIST))
        recyclerView.setHasFixedSize(true)
        recyclerView.setLayoutManager(LinearLayoutManager(this@ApplyLoanToGetMoneyActivity))

    }

    /**
     * 客服回调
     */
    override fun onCustomerServiceBack() {
        getCustomerService()
    }

    /**
     * 倒计时回调
     */
    override fun onCountDownBack() {
        getDeadline()
    }

    /**
     * 退款回调
     */
    override fun onRefundCallBack() {
        startActivity<ApplyLoanToBackMoneyActivity>()

    }


    /**
     * 客服
     */

    fun getCustomerService() {
        var model = BaseParam()

        showApiProgress()
        Api.getApiService().getCustomerService()
                .compose(RxUtils.handleGlobalError<BaseResult<CustomerServiceModel>>(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<CustomerServiceModel>> {
                    override fun onComplete() {
                        hideApiProgress()
                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(t: BaseResult<CustomerServiceModel>) {
                        t.result?.let {
                            it.list?.let {
                                var str1 = ""
                                var str2 = ""
                                var str3 = ""
                                for (i in it.indices) {
                                    when (i) {
                                        0 -> {
                                            str1 = it[i].name + ":" + it[i].sequence
                                        }
                                        1 -> {
                                            str2 = it[i].name + ":" + it[i].sequence
                                        }
                                        2 -> {
                                            str3 = it[i].name + ":" + it[i].sequence
                                        }
                                    }
                                }
                                dialog.setHeader(t.result!!.date).setTitle(str1, str2, str3).show()
                            }
                        }

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
    }

    /**
     * 倒计时
     */

    fun getDeadline() {
        var model = BaseParam()
        showApiProgress()
        Api.getApiService().getDeadline(model)
                .compose(RxUtils.handleGlobalError<BaseResult<DeadlineModel>>(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<DeadlineModel>> {
                    override fun onComplete() {
                        hideApiProgress()
                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(t: BaseResult<DeadlineModel>) {
                        t.result?.let {
                            var currDate = Date(System.currentTimeMillis());
                            var endDate = Utils.getDate(it.deadline!!, "yyyy-MM-dd HH:mm:ss")
                            var diff = endDate.getTime() - currDate.getTime(); // 得到的差值是微秒级别，可以忽略
                            var days = diff / (1000 * 60 * 60 * 24);
                            var hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                            var minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
                            var str = "还剩${days}天${hours}小时${minutes}分"
                            dialogDaojishi.setHeader("温馨提示").setTitle("系统14天内为您不断持续推荐优质产品,请您保持关注", str, "").setBtnRightTxt("确定").setBtnRightTxtColor(Color.parseColor("#30A3FF")).setTitle2Color(Color.parseColor("#30A3FF")).setRightClickListener { dialog, _ ->
                                dialog.dismiss()
                            }.show()
                        }

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
    }

    /**
     * 急速退款页面
     */
    fun getRefundView() {
        var model = BaseParam()
        showApiProgress()
        Api.getApiService().getBorrow(model)
                .compose(RxUtils.handleGlobalError<BaseResult<PushResultModel>>(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<PushResultModel>> {
                    override fun onComplete() {
                        hideApiProgress()
                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(t: BaseResult<PushResultModel>) {

                        if (t.getResultSuccess()) {
                            t.result?.let {
                                var pushResultModel: PushResultModel = it
                                creditLimit = pushResultModel.creditLimit
                                isShow = pushResultModel.isShow
                                if (isShow!!) {
                                    AlertDialogNew(this@ApplyLoanToGetMoneyActivity).setHeader("推荐成功").setTitle("已成功为您推送借款产品，服务费用为200元，系统将自动扣款，如有疑问请联系客服。", "", "").setBtnLeftTxt("关闭").setLeftClickListener { alertDialog, button ->
                                        alertDialog.dismiss()
                                    }.show()
                                }
                                list = pushResultModel.list
                                if (list!!.size != 0) {
                                    var platformListAdapter: PushPlatformListAdapter = PushPlatformListAdapter(this@ApplyLoanToGetMoneyActivity, list, this@ApplyLoanToGetMoneyActivity, creditLimit)
                                    recyclerView.adapter = platformListAdapter
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


}