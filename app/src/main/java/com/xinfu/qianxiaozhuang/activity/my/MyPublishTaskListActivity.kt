package com.xinfu.qianxiaozhuang.activity.my

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.activity.BaseActivity
import com.xinfu.qianxiaozhuang.activity.home.TaskDetailActivity
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
import kotlinx.android.synthetic.main.activity_my_publish_task_list.*
import kotlinx.android.synthetic.main.listitem_home_list.*
import org.jetbrains.anko.support.v4.startActivity

class MyPublishTaskListActivity : BaseActivity() {
    var myPublishList=ArrayList<Task>()
    lateinit var mAdapter: BaseQuickAdapter<Task, BaseViewHolder>
    lateinit var mRecycleViewHelper: RecycleViewHelper<Task>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_publish_task_list)
        mAdapter = object : BaseQuickAdapter<Task, BaseViewHolder>(R.layout.listitem_home_list, myPublishList) {
            override fun convert(helper: BaseViewHolder?, item: Task) {

                var img1=helper!!.getView<ImageView>(R.id.mImage)
                var img2=helper!!.getView<ImageView>(R.id.mImage1)
                var img3=helper!!.getView<ImageView>(R.id.mImage2)
                var mHead=helper!!.getView<ImageView>(R.id.mHead)
                img1.visibility= View.GONE
                img2.visibility= View.GONE
                img3.visibility= View.GONE
                helper!!.setText(R.id.mBenjin,item.goodsPrice.toString())
                helper!!.setText(R.id.mJiangli,item.workerPrice.toString())
                helper!!.setText(R.id.mTitle,item.title)
                helper!!.setText(R.id.mName,item.username)
                helper!!.setText(R.id.mJindu,(item.workerNum-item.workingNum).toString())
                helper!!.setText(R.id.mTime,DataUtil.getStringFromDate(item.createTime))
                Glide.with(mActivity).load(R.mipmap.ic_launcher).apply(RequestOptions.bitmapTransform(CircleCrop())).into(mHead)
                helper.getView<View>(R.id.uiContainer).setOnClickListener {
                    //startActivity(TaskDetailActivity.param_data_task to item)

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
            model.pageSize=10
            model.pageNumber=1
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
