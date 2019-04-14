package com.xinfu.qianxiaozhuang.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.gyf.barlibrary.ImmersionBar
import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.dialog.ApiRequestDialog
import com.xinfu.qianxiaozhuang.utils.StatusBarUtil
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.AnkoLogger
import java.util.*

/**
 * 02/02/2018  11:26 AM
 * Created by Zhang.
 */

abstract class BaseActivity : AppCompatActivity(),AnkoLogger {
    lateinit var mActivity:BaseActivity
    var ApiRequestDialog: ApiRequestDialog?=null
//    var mOnResultListener: ((mNotificationRequestCode: Int, resultCode: Int, data: Intent?) -> Unit)? = null


    //private val mOnResultListeners= mutableListOf<(Int,Int,Intent?)->Unit>()
    public lateinit var mDisposables:CompositeDisposable



    public fun getDisposables() = mDisposables

//    fun onResult(listener: (requestCode:Int,resultCode:Int, Intent?) -> Unit): Unit {
////        mOnResultListener = listener
//        mOnResultListeners.add(listener)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBar()
        mActivity=this
        mActivities.add(this)
        mDisposables = CompositeDisposable()
//        ImmersionBar.with(this)
//                .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题
//                //  .keyboardEnable(true, WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
//                //                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)  //软键盘自动弹出
//                .init();

        /*
        mProgress.setOnDismissListener(object : DialogInterface.OnDismissListener{
            override fun onDismiss(p0: DialogInterface?) {
                mDisposables.dispose()
            }

        })
        */
    }

    open fun setStatusBar() {
        StatusBarUtil.setColor(this, resources.getColor(R.color.color_cccccc))
    }

    /**
     *
     * @param clz
     * @param bundle 参数可不传
     */
    fun gotoActivity(clz: Class<*>, bundle: Bundle?) {
        val intent = Intent(this, clz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
////        mOnResultListener?.invoke(mNotificationRequestCode, resultCode, data)
//        mOnResultListeners.forEach {
//            it.invoke(requestCode,resultCode,data)
//        }
//    }

    fun showApiProgress(){
        if(ApiRequestDialog==null){
            ApiRequestDialog= ApiRequestDialog(this)
        }
        ApiRequestDialog?.let {
            it.showRequestDataDialog()
        }
    }
    fun hideApiProgress(){
        ApiRequestDialog?.let {
            it.dismissRequestDataDialog()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        mActivities.remove(this)
        mDisposables.dispose()
        super.onDestroy()
    }

    companion object {
        protected var mActivities: MutableList<BaseActivity> = ArrayList()
        val TAG_MARGIN_ADDED = "TAG_MARGIN_ADDED"
        private val TAG_FAKE_STATUS_BAR_VIEW = 12345
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        if (mActivities == null) {
            return
        }
        val iterator = mActivities.iterator()
        while (iterator.hasNext()) {
            val activity = iterator.next()
            if (activity != null && activity!!.javaClass == cls) {
                activity!!.finish()
                iterator.remove()
            }
        }
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity?) {
        var activity = activity
        if (mActivities == null) {
            return
        }
        if (activity != null) {
            mActivities.remove(activity)
            activity.finish()
            activity = null
        }
    }

}
