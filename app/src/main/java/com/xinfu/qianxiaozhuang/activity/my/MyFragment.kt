package com.xinfu.qianxiaozhuang.activity.my


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.activity.BaseFragment
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.gyf.barlibrary.ImmersionBar
import com.orhanobut.hawk.Hawk
import com.xiang.one.network.error.RxUtils
import com.xiang.one.utils.dialog.AlertDialogNew
import com.xinfu.qianxiaozhuang.SpConfig
import com.xinfu.qianxiaozhuang.activity.loan.ApplyLoanActivity
import com.xinfu.qianxiaozhuang.activity.loan.AuthCenterActivity
import com.xinfu.qianxiaozhuang.activity.loan.AuthUserBankActivity
import com.xinfu.qianxiaozhuang.activity.login.LoginActivity
import com.xinfu.qianxiaozhuang.activity.publish.PublishTaskActivity
import com.xinfu.qianxiaozhuang.api.Api
import com.xinfu.qianxiaozhuang.api.BaseResult
import com.xinfu.qianxiaozhuang.api.model.UserCenterModel
import com.xinfu.qianxiaozhuang.api.model.params.BaseParam
import com.xinfu.qianxiaozhuang.dialog.PerfectDialog
import com.xinfu.qianxiaozhuang.utils.ExpresssoinValidateUtil
import com.xinfu.qianxiaozhuang.utils.StatusBarUtil
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_my.*
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 *  我的主界面
 */
class MyFragment : BaseFragment(){
    override fun initImmersionBar() {
        ImmersionBar.with(this)
                .statusBarView(top_view)//解决顶部和状态栏重叠问题
                .statusBarDarkFont(true, 0.2f)//解决白色状态栏问题
                //.navigationBarDarkIcon(true, 0.2f)//解决白色状态栏问题
                .keyboardEnable(true) //解决软键盘与底部输入框冲突问题
                .init()
    }

    var authentication: Boolean = false
    var perfectDialog: PerfectDialog? = null
    var perfectDialogTag: String = "MyFragment_PerfectDialog"
    var alertDialogNew:AlertDialogNew?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initUI()
        //getData()
    }

    private fun initUI() {

        //var path = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1552814176721&di=a3cc48bfab61dfd9a831ec7d6c67cc0d&imgtype=0&src=http%3A%2F%2Fwww.gupen.com%2Fuploads%2Fallimg%2F111101%2F11-111101093422328.jpg"
        Glide.with(this).load(R.mipmap.ic_launcher).apply(RequestOptions.bitmapTransform(CircleCrop())).into(mHead)

        mPublishTask.setOnClickListener {
            startActivity<PublishTaskActivity>()
        }
//        mMangerPublishTask.setOnClickListener {
//            startActivity<MyPublishTaskListActivity>()
//        }


        mYue.setOnClickListener {
            startActivity<MyQianbaoActivity>()
        }
        mYinhangka.setOnClickListener {

            if (authentication) {//已经实名认证(跳转银行卡列表)
                startActivity<MyBankListActivity>()
            } else {
                //startActivity<AuthUserBankActivity>()
                if(alertDialogNew==null)
                    alertDialogNew=AlertDialogNew(activity!!)
                alertDialogNew!!.setHeader("温馨提示!").setTitle("请完善你的资料","","").setBtnLeftTxt("确定").setBtnRightTxt("取消").setLeftClickListener { alertDialog, button ->
                    startActivity<AuthCenterActivity>()
                    alertDialog.dismiss() }.show()
            }

        }
        mMyPublishedTask.setOnClickListener {
            startActivity<MyPublishTaskListActivity>()
        }
        mOrder.setOnClickListener {
            startActivity<MyOrderActivity>()
        }
        toLoginOut.setOnClickListener {
            Hawk.deleteAll()
            Hawk.put(SpConfig.GUIDE_STATUS, true)
            //startActivity<LoginActivity>()
            var intent: Intent = Intent()
            intent.setClass(activity, LoginActivity::class.java)
            intent.putExtra("isBackToLoanFragment", true)
            startActivity(intent)
        }

        mYjianfankui.setOnClickListener {
            startActivity<FeedbackActivity>()
        }
    }

    /**
     * 外部调用
     */
    open fun onRefreshData() {

        getData()

    }

    /**
     * 获取用户中心的数据
     */
    fun getData() {

        var baseParam = BaseParam()
        Api.getApiService().getUserCenter(baseParam)
                .compose(RxUtils.handleGlobalError<BaseResult<UserCenterModel>>(activity!!))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<UserCenterModel>> {
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(t: BaseResult<UserCenterModel>) {
                        if (t.getResultSuccess()) {
                            t.result?.let {
                                //非空情况执行
                                var userCenterModel: UserCenterModel = it
                                var mobile: String? = userCenterModel.mobile
                                authentication = userCenterModel.authentication
                                txt_user_name.setText(ExpresssoinValidateUtil.formatUserNameFour(mobile))
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
