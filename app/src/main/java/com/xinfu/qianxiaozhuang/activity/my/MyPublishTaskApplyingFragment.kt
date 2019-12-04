package com.xinfu.qianxiaozhuang.activity.my


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.activity.BaseFragment
import com.xinfu.qianxiaozhuang.api.Api
import com.xinfu.qianxiaozhuang.api.BaseResult
import com.xinfu.qianxiaozhuang.api.model.Task
import com.xinfu.qianxiaozhuang.api.model.params.HomeTaskParam
import com.xinfu.qianxiaozhuang.utils.DataUtil
import com.xinfu.qianxiaozhuang.utils.recycleView.RecycleViewHelper
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_my_publish_task_applying.*
import org.jetbrains.anko.support.v4.startActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 *我发布的任务-待申请的任务
 *
 */
class MyPublishTaskApplyingFragment : BaseFragment() {
    var myPublishList=ArrayList<Task>()
    lateinit var mAdapter: BaseQuickAdapter<Task, BaseViewHolder>
    lateinit var mRecycleViewHelper: RecycleViewHelper<Task>

    override fun initImmersionBar() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_publish_task_applying, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mAdapter = object : BaseQuickAdapter<Task, BaseViewHolder>(R.layout.listitem_home_list, myPublishList) {
            override fun convert(helper: BaseViewHolder?, item: Task) {

                var img1=helper!!.getView<ImageView>(R.id.mImage)
                var img2=helper!!.getView<ImageView>(R.id.mImage1)
                var img3=helper!!.getView<ImageView>(R.id.mImage2)
                var mHead=helper!!.getView<ImageView>(R.id.mHead)
                img1.visibility= View.GONE
                img2.visibility= View.GONE
                img3.visibility= View.GONE
                //helper!!.setText(R.id.mBenjin,item.goodsPrice.toString())
                helper!!.setText(R.id.mJiangli,item.workerPrice.toString())
                helper!!.setText(R.id.mTitle,item.title)
                helper!!.setText(R.id.mName,item.username)
                //helper!!.setText(R.id.mJindu,(item.workerNum-item.workingNum).toString())
                helper!!.setText(R.id.mTime, DataUtil.getStringFromDate(item.createTime))
                Glide.with(mActivity).load(R.mipmap.ic_launcher).apply(RequestOptions.bitmapTransform(CircleCrop())).into(mHead)
                helper.getView<View>(R.id.uiContainer).setOnClickListener {
                    startActivity<MyPublishTaskApplyingListActivity>()
                }

                item.images?.let {
                    when(it.size){
                        3-> {
                            img1.visibility = View.VISIBLE
                            img2.visibility = View.VISIBLE
                            img3.visibility = View.VISIBLE
                            Glide.with(mActivity).load(it[0]).into(img1)
                            Glide.with(mActivity).load(it[1]).into(img2)
                            Glide.with(mActivity).load(it[2]).into(img3)
                        }
                        2->{
                            img1.visibility = View.VISIBLE
                            img2.visibility = View.VISIBLE
                            img3.visibility = View.INVISIBLE
                            Glide.with(mActivity).load(it[0]).into(img1)
                            Glide.with(mActivity).load(it[1]).into(img2)
                        }
                        1->{
                            img1.visibility = View.VISIBLE
                            img2.visibility = View.INVISIBLE
                            img3.visibility = View.INVISIBLE
                            Glide.with(mActivity).load(it[0]).into(img1)
                        }else->{

                    }
                    }
                }




            }

        }

        mRecyclerView.adapter=mAdapter
        mRecycleViewHelper= RecycleViewHelper(mActivity,mRecyclerView,mSwipeRefreshLayout,true, RecycleViewHelper.RecycleViewListener { pageIndex, pageSize ->
            var model= HomeTaskParam()
            model.pageSize=pageSize
            model.pageNumber=pageIndex
            model.state=0
            Api.getApiService().getMyPublishTaskList(model)
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
}
