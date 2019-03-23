package com.xinfu.qianxiaozhuang.activity

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xinfu.qianxiaozhuang.dialog.ApiRequestDialog
import io.reactivex.disposables.CompositeDisposable

/**
 * 02/02/2018  11:28 AM
 * Created by Zhang.
 */
abstract class BaseFragment : Fragment(){
    var ApiRequestDialog: ApiRequestDialog?=null
    //添加支持
    public lateinit var mDisposables: CompositeDisposable
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mDisposables = CompositeDisposable()
    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposables.dispose()
    }

    fun showApiProgress(){
        if(ApiRequestDialog==null){
            ApiRequestDialog=ApiRequestDialog(activity)
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
//fragment缓存view，暂时不需要缓存fragment
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        //Fragment之间切换时每次都会调用onCreateView方法，导致每次Fragment的布局都重绘，无法保持Fragment原有状态。
//        if (rootView == null) {
//            rootView = inflater.inflate(layoutId, container, false)
//        }else{
//            val parent = rootView!!.parent as ViewGroup?
//            parent?.removeView(rootView)
//        }
//        return rootView
//    }
}