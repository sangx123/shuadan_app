package com.xinfu.qianxiaozhuang.activity.home


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.activity.BaseFragment
import com.xinfu.qianxiaozhuang.api.Api
import com.xinfu.qianxiaozhuang.api.BaseResult
import com.xinfu.qianxiaozhuang.api.model.FeedbackModel
import com.xinfu.qianxiaozhuang.api.model.Task
import com.xinfu.qianxiaozhuang.api.model.params.FeedbackParam
import com.xinfu.qianxiaozhuang.api.model.params.HomeTaskParam
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.error

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getHomeTaskList()


    }

    /**
     *获取大厅任务列表反馈
     */
    fun getHomeTaskList(){
        var model= HomeTaskParam()
        model.pageSize=10
        model.pageNumber=1
        Api.getApiService().getHomeTaskList(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<List<Task>>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(t: BaseResult<List<Task>>) {
                        t.result?.let {

                        }

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
    }
}
