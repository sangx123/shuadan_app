package com.xinfu.qianxiaozhuang.activity.home


import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import cn.bingoogolapple.bgabanner.BGABanner
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.activity.BaseFragment
import com.xinfu.qianxiaozhuang.api.Api
import com.xinfu.qianxiaozhuang.api.BaseResult
import com.xinfu.qianxiaozhuang.api.model.FeedbackModel
import com.xinfu.qianxiaozhuang.api.model.Task
import com.xinfu.qianxiaozhuang.api.model.params.FeedbackParam
import com.xinfu.qianxiaozhuang.api.model.params.HomeTaskParam
import com.xinfu.qianxiaozhuang.utils.DataUtil
import com.xinfu.qianxiaozhuang.utils.DisplayUtil
import com.xinfu.qianxiaozhuang.utils.StringUtils
import com.xinfu.qianxiaozhuang.utils.recycleView.RecycleViewHelper
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.error
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.startActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * 首页fragment
 *
 */
class HomeFragment : BaseFragment() {
    var homeList=ArrayList<Task>()
    lateinit var mAdapter:BaseQuickAdapter<Task,BaseViewHolder>
    lateinit var mRecycleViewHelper: RecycleViewHelper<Task>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initBanner()
        mAdapter = object : BaseQuickAdapter<Task, BaseViewHolder>(R.layout.listitem_home_list, homeList) {
            override fun convert(helper: BaseViewHolder?, item: Task) {


                var img1=helper!!.getView<ImageView>(R.id.mImage)
                var img2=helper!!.getView<ImageView>(R.id.mImage1)
                var img3=helper!!.getView<ImageView>(R.id.mImage2)
                var mHead=helper!!.getView<ImageView>(R.id.mHead)
                img1.visibility=View.GONE
                img2.visibility=View.GONE
                img3.visibility=View.GONE
                helper!!.setText(R.id.mBenjin,item.goodsPrice.toString())
                helper!!.setText(R.id.mJiangli,item.workerPrice.toString())
                helper!!.setText(R.id.mTitle,item.title)
                helper.getView<View>(R.id.uiContainer).setOnClickListener {
                    startActivity<TaskDetailActivity>(TaskDetailActivity.param_data_task to item)

                }


                helper!!.setText(R.id.mJindu,(item.workerNum-item.workingNum).toString())
                helper!!.setText(R.id.mName,item.username)
                helper!!.setText(R.id.mTime, DataUtil.getStringFromDate(item.createTime))
                Glide.with(this@HomeFragment).load(R.mipmap.ic_launcher).apply(RequestOptions.bitmapTransform(CircleCrop())).into(mHead)


                item.images?.let {
                    when(it.size){
                        3-> {
                            img1.visibility = View.VISIBLE
                            img2.visibility = View.VISIBLE
                            img3.visibility = View.VISIBLE
                            Glide.with(context!!).load(it[0]).into(img1)
                            Glide.with(context!!).load(it[1]).into(img2)
                            Glide.with(context!!).load(it[2]).into(img3)
                        }
                        2->{
                            img1.visibility = View.VISIBLE
                            img2.visibility = View.VISIBLE
                            img3.visibility = View.INVISIBLE
                            Glide.with(context!!).load(it[0]).into(img1)
                            Glide.with(context!!).load(it[1]).into(img2)
                        }
                        1->{
                            img1.visibility = View.VISIBLE
                            img2.visibility = View.INVISIBLE
                            img3.visibility = View.INVISIBLE
                            Glide.with(context!!).load(it[0]).into(img1)
                        }else->{

                        }
                    }
                }




            }

        }

        mRecyclerView.adapter=mAdapter
        mRecycleViewHelper= RecycleViewHelper(activity!!,mRecyclerView,mSwipeRefreshLayout,true, RecycleViewHelper.RecycleViewListener { pageIndex, pageSize ->
            var model= HomeTaskParam()
            model.pageSize=10
            model.pageNumber=1
            model.state=0
            Api.getApiService().getHomeTaskList(model)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : Observer<BaseResult<ArrayList<Task>>> {
                        override fun onComplete() {

                        }

                        override fun onSubscribe(d: Disposable) {
                            mDisposables.add(d)
                        }

                        override fun onNext(t: BaseResult<ArrayList<Task>>) {
                            mRecycleViewHelper.onApiSuccess(t,t.data)
                        }

                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                            mRecycleViewHelper.onApiError()
                        }

                    })
        })

    }

    private fun initBanner() {

        //初始化banner
        var list= ArrayList<BannerBean>()
        var model= BannerBean()
        model.imageUrl="http://www.vz18.com/upload/201607/15/201607152321397655.png"
        list.add(model)
        model= BannerBean()
        model.imageUrl="http://www.vz18.com/upload/201607/15/201607152333240637.png"
        list.add(model)
        mBannerContainer.removeAllViews()
        mBannerContainer.addView(addBannerView(list, 5 * 1000))
    }

    //设置banner
    private fun addBannerView(banners: List<BannerBean>, bannerInterval: Int): View {
        // calculate banner height
        val displayUtil = DisplayUtil(context)
        val width = displayUtil.screenWidth
        val height = width * 172 / 375
        val view = LayoutInflater.from(context).inflate(R.layout.banner_view, null, false)
        val banner = view.findViewById(R.id.dashboard_banner_raw) as BGABanner
        banner.layoutParams.height = height
        val imageUrls = ArrayList<String>(banners.size)
        val contentStrs = ArrayList<String>(banners.size)

        for (item in banners){
            contentStrs.add("")
            imageUrls.add(item.imageUrl)
        }

        banner.setAdapter { banner, itemView, model, position ->
            val actionItem = banners[position]
            if (!TextUtils.isEmpty(actionItem.imageUrl)&& itemView is ImageView){
                activity?.let {
                    Glide.with(it)
                            .load(actionItem.imageUrl).into(itemView)
                }

            }

        }
        //Log.e("sangxiang", "addBannerView: "+banners.size());
        banner.setAutoPlayAble(banners.size > 1)
        banner.setData(imageUrls, contentStrs)
        banner.setDelegate { banner, itemView, model, position ->
            val actionItem = banners[position]
            //点击往后台传递数据
//            val call = KaishiApp.getApiService().insertBannerView(BannerView(actionItem.objId, Settings.Global.getMe().getId()))
//            call.enqueue(object : KaishiCallback<String>(callList, context, true) {
//                protected fun success(response: Response<String>) {}
//
//                protected fun dismissProgress() {}
//            })
//            callList.add(call)

            // go to target screen
        }

        banner.setAutoPlayInterval(bannerInterval)
        banner.startAutoPlay()
        return view
    }
}
