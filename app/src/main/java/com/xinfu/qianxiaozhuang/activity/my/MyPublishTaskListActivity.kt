package com.xinfu.qianxiaozhuang.activity.my

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.gyf.barlibrary.ImmersionBar
import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.activity.BaseActivity
import com.xinfu.qianxiaozhuang.activity.home.TaskDetailActivity
import com.xinfu.qianxiaozhuang.api.Api
import com.xinfu.qianxiaozhuang.api.BaseResult
import com.xinfu.qianxiaozhuang.api.model.Task
import com.xinfu.qianxiaozhuang.api.model.params.HomeTaskParam
import com.xinfu.qianxiaozhuang.utils.DataUtil
import com.xinfu.qianxiaozhuang.utils.recycleView.RecycleViewHelper
import com.xinfu.qianxiaozhuang.utils.viewpager.MFragmentStatePagerAdapter
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


        ImmersionBar.with(this)
                .statusBarView(top_view)//解决顶部和状态栏重叠问题
                .statusBarDarkFont(true, 0.2f)//解决白色状态栏问题
                //.navigationBarDarkIcon(true, 0.2f)//解决白色状态栏问题
                .keyboardEnable(true) //解决软键盘与底部输入框冲突问题
                .init()

        val tabTitles= arrayOf("待申请的任务", "进行中任务","已完成的任务")
        var mAdapter= MFragmentStatePagerAdapter(supportFragmentManager, arrayOf(MyPublishTaskApplyingFragment(),MyPublishTaskOnGoingFragment(),MyPublishTaskFinishedFragment()).toMutableList() as List<Fragment>?, tabTitles)

        mViewPager.adapter=mAdapter
        mViewPager.offscreenPageLimit=2
        mViewPager.setPageTransformer(false, ViewPager.PageTransformer { page, position ->
            when {
                position > 1 -> page.alpha = 1f
                position >= 0 -> page.alpha = 1 - position
                position > -1 -> page.alpha = 1 + position
                else -> page.alpha = 0f
            }
        })
        mTabLayout.setupWithViewPager(mViewPager)
    }

}
