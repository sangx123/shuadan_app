package com.xinfu.qianxiaozhuang.activity.loan


import android.os.Build
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import com.cjj.MaterialRefreshLayout
import com.cjj.MaterialRefreshListener
import com.xiang.one.network.error.RxUtils
import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.adapter.ImagePagerAdapter
import com.xinfu.qianxiaozhuang.api.Api
import com.xinfu.qianxiaozhuang.api.BaseResult
import com.xinfu.qianxiaozhuang.api.model.HomeModel
import com.xinfu.qianxiaozhuang.api.model.params.HomeParam
import com.xinfu.qianxiaozhuang.activity.BaseFragment
import com.xinfu.qianxiaozhuang.activity.login.LoginActivity
import com.xinfu.qianxiaozhuang.activity.my.MyOrderActivity
import com.xinfu.qianxiaozhuang.dialog.PerfectDialog
import com.xinfu.qianxiaozhuang.dialog.UniversalDialog
import com.xinfu.qianxiaozhuang.utils.ExpresssoinValidateUtil
import com.xinfu.qianxiaozhuang.utils.StatusBarUtil
import com.xinfu.qianxiaozhuang.widget.VerticalTextview
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_loan.*
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import java.util.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * 借款主界面
 */
class LoanFragment : BaseFragment(), UniversalDialog.IHitSureCallBack {

    var universalDialog: UniversalDialog? = null
    var universalDialogTag: String = "LoanFragment_UniversalDialog"
    var mAdapter: ImagePagerAdapter? = null
    var onlineStatus: Boolean = false //在线状态
    var idCard: Boolean = false//信用认证
    var creditLimit: Double = 0.0
    var creditMin: Int = 0
    var creditMax: Int = 0
    var remainDate:Int=0
    var circuit=false
    lateinit var homeBanner: ArrayList<HomeModel.NoticeItemlModel>
    lateinit var imageViews: ArrayList<ImageView>
    lateinit var loanRecord: ArrayList<String>
    var anim: CircleAnim? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_loan, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ininUI()
        //getData()
        setProgressNum()
    }

    fun ininUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            top_view.setVisibility(View.VISIBLE)
            top_view.setLayoutParams(StatusBarUtil.getLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, context))
            //top_view.setBackgroundColor(resources.getColor(R.color.color_e1e5f7))
        } else {
            top_view.setVisibility(View.GONE)
        }

        refresh.setWaveColor(resources.getColor(R.color.color_82C7ff))
        refresh.setIsOverLay(false)
        refresh.setWaveShow(true)
        refresh.setMaterialRefreshListener(object : MaterialRefreshListener() {
            override fun onRefresh(materialRefreshLayout: MaterialRefreshLayout) {

                materialRefreshLayout.postDelayed({ getData() }, 2000)
            }
        })

        my_seekbar.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                my_seekbar.parent.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        my_seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

                txt_credit_amount.setText(ExpresssoinValidateUtil.fomatMoneyThree(progress.toDouble()))

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })

        viewpager.setInterval(3000)
        setPageChangeListener()
        viewpager.setClipChildren(false)
        viewpager.setOffscreenPageLimit(15)


        txt_notice.setText(12f, 5, resources.getColor(R.color.color_999999))//设置属性
        txt_notice.setTextStillTime(3000)//设置停留时长间隔
        txt_notice.setAnimTime(500)//设置进入和退出的时间间隔
        txt_notice.setOnItemClickListener(object : VerticalTextview.OnItemClickListener {

            override fun onItemClick(position: Int) {
                //Toast.makeText(this@MainActivity, "点击了 : " + titleList.get(position), Toast.LENGTH_SHORT).show()
            }
        })

        txt_login.setOnClickListener {

            if (onlineStatus) {//已经登录

                if (circuit) {//已认证


                    if (universalDialog == null) {
                        universalDialog = UniversalDialog()
                    }
                    universalDialog?.let {
                        if (!universalDialog!!.isVisible() && !universalDialog!!.isAdded()) {
                            val fm = activity?.getSupportFragmentManager()
                            val ft = fm?.beginTransaction()
                            ft?.add(universalDialog!!, universalDialogTag)
                            ft?.commitAllowingStateLoss()
                            universalDialog?.setInsertData("您有正在进行中的订单，如需查询可在订单列表中查看。${remainDate}天后可重新申请借款。", getString(R.string.cancel), getString(R.string.view_order_list), null, this@LoanFragment)
                        }
                    }


                } else {
                    startActivity<AuthCenterActivity>()
                }

            } else {
                startActivity<LoginActivity>()
            }
        }


    }

    //  ViewPager页面改变监听
    private fun setPageChangeListener() {
        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
                //                indexText.setText(new StringBuilder().append((position) % ListUtils.getSize(imageIdList) + 1).append("/")
                //                .append(ListUtils.getSize(imageIdList)));
                if (homeBanner != null && homeBanner?.size!! > 0) {
                    pageSelected(position % homeBanner?.size!!)
                }
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            /**
             * 此方法是在状态改变的时候调用，其中state这个参数有三种状态（0，1，2）。state==1的时,正在滑动，state==2的时滑动完毕，state==0 无事件.
             * @param arg0
             */
            override fun onPageScrollStateChanged(arg0: Int) {}

        })
    }

    private fun pageSelected(position: Int) {
        for (i in imageViews!!.indices) {

            imageViews[i].setImageResource(R.drawable.btn_gray_circle)
            if (position == i) {

                imageViews[i].setImageResource(R.drawable.btn_gray_deep_circle)

            } else {
                imageViews[i].setImageResource(R.drawable.btn_gray_circle)
            }
        }

    }


    /**
     * 设置轮播图
     */
    private fun setBannerImage() {

        setBannerImageIndactor(homeBanner!!.size)
        mAdapter = ImagePagerAdapter(activity, homeBanner).setInfiniteLoop(true)
        viewpager.setAdapter(mAdapter)
        //mViewPager.startAutoScroll();
        viewpager.setCurrentItem(100 * homeBanner!!.size)

    }

    /**
     * 设置轮播图引导
     */
    private fun setBannerImageIndactor(imageAmount: Int) {

        ll_indicator.removeAllViews()
        imageViews = ArrayList()
        for (i in 0 until imageAmount) {
            var item: ImageView = ImageView(activity);
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(0, 0, 10, 0)
            item.setLayoutParams(layoutParams)
            if (i == 0) {
                item.setImageResource(R.drawable.btn_gray_deep_circle)
            } else {
                item.setImageResource(R.drawable.btn_gray_circle)
            }
            ll_indicator.addView(item, i)
            imageViews.add(item)
        }
    }


    /**
     * 外部调用
     */
    open fun onRefreshData() {

        getData()

    }

    /**
     * 获取主页的数据
     */
    fun getData() {

        var homeParam = HomeParam()
        Api.getApiService().myhome(homeParam)
                .compose(RxUtils.handleGlobalError<BaseResult<HomeModel>>(activity!!))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<HomeModel>> {
                    override fun onComplete() {
                        if (refresh.isRefreshing) {
                            refresh.finishRefresh()
                        }
                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(t: BaseResult<HomeModel>) {
                        //error { "111" }
                        if (t.getResultSuccess()) {
                            t.result?.let {
                                //非空情况执行
                                //toast(it.toString())
                                //toast(t.result.toString())

                                var homeModel: HomeModel = it

                                homeBanner = homeModel.list
                                setBannerImage()
                                loanRecord = homeModel.loanRecord
                                txt_notice.setTextList(loanRecord)
                                onlineStatus = homeModel.isOnlineStatus
                                remainDate=it.remainDate
                                circuit=it.isCircuit
                                if (onlineStatus) {//已登录
                                    txt_login.setText(getString(R.string.rapid_application))
                                } else {//未登录
                                    txt_login.setText(getString(R.string.login_find_surprise))
                                }
                                idCard = homeModel.isIdCard
                                creditLimit = homeModel.creditLimit
                                txt_credit_amount.setText(ExpresssoinValidateUtil.fomatMoneyThree(creditLimit))
                                creditMin = homeModel.creditMin
                                creditMax = homeModel.creditMax
                                txt_left_value.setText(creditMin.toString() + getString(R.string.yuan))
                                txt_right_value.setText(creditMax.toString() + getString(R.string.yuan))

                                my_seekbar.max = creditMax
                                if (idCard) {
                                    setSeekBarClickable(0)//禁止滑动
                                } else {
                                    setSeekBarClickable(1)//开启滑动
                                }
                                anim?.progressLength = creditMax
                                my_seekbar.startAnimation(anim)
                            }

                        } else {
                            toast(t.message.toString())
                        }
                    }

                    override fun onError(e: Throwable) {0
                        refresh.finishRefresh()
                        e.printStackTrace()
                    }

                })
    }

    /**
     * seek启动或禁止滑动
     */
    fun setSeekBarClickable(index: Int) {
        if (index == 1) {   //启用状态
            my_seekbar.setClickable(true)
            my_seekbar.setEnabled(true)
            my_seekbar.setSelected(true)
            my_seekbar.setFocusable(true)
//            Drawable drawable=getResources().getDrawable(R.drawable.yellow_mid_img_40);
//            mSeekBar.setThumb(drawable);
//            my_seekbar.setProgress(50)

        } else {  //禁用状态
            my_seekbar.setClickable(false)
            my_seekbar.setEnabled(false)
            my_seekbar.setSelected(false)
            my_seekbar.setFocusable(false)
//            Drawable drawable=getResources().getDrawable(R.drawable.seek_bar_grey_img_40);
//            mSeekBar.setThumb(drawable);
//            my_seekbar.setProgress(50)

        }
    }

    /**
     * 查看订单
     */
    override fun onHitSureCallBack() {

        startActivity<MyOrderActivity>()

    }


    override fun onPause() {
        super.onPause()
        // stop auto scroll when onPause
        viewpager.stopAutoScroll()
        txt_notice.stopAutoScroll()
    }

    override fun onResume() {
        super.onResume()
        // start auto scroll when onResume
        viewpager.startAutoScroll()
        txt_notice.startAutoScroll()
    }


    inner class CircleAnim : Animation() {

        open var progressLength: Int = 0

        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            super.applyTransformation(interpolatedTime, t)
            my_seekbar.setProgress((interpolatedTime * progressLength).toInt())
        }
    }

    fun setProgressNum() {
        anim = CircleAnim()
        anim!!.duration = 1200
        //my_seekbar.startAnimation(anim)
    }

}

