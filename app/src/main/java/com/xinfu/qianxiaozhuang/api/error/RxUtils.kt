package com.xiang.one.network.error

import android.os.Looper
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.widget.Toast
import com.orhanobut.hawk.Hawk
import com.xiang.one.utils.dialog.AlertDialogNew
import com.xinfu.qianxiaozhuang.SpConfig
import com.xinfu.qianxiaozhuang.activity.BaseActivity
import com.xinfu.qianxiaozhuang.activity.login.LoginActivity
import com.xinfu.qianxiaozhuang.api.BaseResult
import com.xinfu.qianxiaozhuang.utils.ToastUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.json.JSONException
import java.net.ConnectException

object RxUtils {

    /**
     * Status code
     */
    private const val STATUS_OK = 200
    private const val STATUS_UNAUTHORIZED = 401
    private const val LOGIN_TIME_OUT="DL0001"
    private const val FORBIDDEN = 403
    private const val NOT_FOUND = 404
    private const val REQUEST_TIMEOUT = 408
    private const val INTERNAL_SERVER_ERROR = 500
    private const val BAD_GATEWAY = 502
    private const val SERVICE_UNAVAILABLE = 503
    private const val GATEWAY_TIMEOUT = 504
    private const val ServerError = "YZ0001"
    private const val ServerOK = "000"

   open fun <T : BaseResult<*>> handleGlobalError(activity: FragmentActivity): GlobalErrorTransformer<T> = GlobalErrorTransformer(
            // 通过onNext流中数据的状态进行操作
            onNextInterceptor = {
                activity.runOnUiThread {
                    (activity as BaseActivity).hideApiProgress()
                }
                when (it.respCode) {
                    ServerOK->
                    {
                        Observable.just(it)
                    }
                    else-> {
                        activity.runOnUiThread {
                            Toast.makeText(activity, it.respMsg, Toast.LENGTH_SHORT).show()
                        }
                        Observable.error(ServerException())
                    }
                }
            },

            // 通过onError中Throwable状态进行操作
            onErrorResumeNext = { error ->
                //Log.e("sangxiang",Thread.currentThread().name)
                when (error) {
                    is ConnectException -> {
                        Observable.error<T>(ConnectFailedAlertDialogException())
                    }
                    else -> Observable.error<T>(error)
                }
            },

            onErrorConsumer = { error ->
                activity.runOnUiThread {
                    (activity as BaseActivity).hideApiProgress()
                    when (error) {
                        is LoginTimeOutException -> {
                            AlertDialogNew(activity).setHeader("温馨提示").setTitle("登录超时，请重新登录！","","").setBtnLeftTxt("取消").setBtnRightTxt("立即登录").setRightClickListener { alertDialog, button ->
                                Hawk.deleteAll()
                                Hawk.put(SpConfig.GUIDE_STATUS, true)
                                activity.startActivity<LoginActivity>()
                                activity.finish()
                                alertDialog.dismiss()
                            }.show()

                        }
                        is JSONException -> {
                            Log.w("RxUtils", "全局异常捕获-Json解析异常！")
                        }
                        is TokenExpiredException -> {
                            Log.w("RxUtils", "全局异常捕获-TokenExpiredException！")
                        }
                        is ConnectFailedAlertDialogException -> {
                            Toast.makeText(activity, "网络连接失败！", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                           // Toast.makeText(activity, "1110", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
    )
}
