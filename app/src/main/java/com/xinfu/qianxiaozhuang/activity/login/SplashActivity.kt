package com.xinfu.qianxiaozhuang.activity.login

import android.os.Bundle
import com.xinfu.qianxiaozhuang.activity.BaseActivity
import com.xinfu.qianxiaozhuang.R
import org.greenrobot.eventbus.Subscribe
import android.util.Log
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.orhanobut.hawk.Hawk
import com.xinfu.qianxiaozhuang.SpConfig
import com.xinfu.qianxiaozhuang.activity.MainActivity
import com.xinfu.qianxiaozhuang.api.TestApiActivity
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.startActivity
import java.util.concurrent.TimeUnit


/**
 * 启动页
 */
class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)
        Glide.with(this).load(R.drawable.drawable_start_page).into(mBg)
        // 倒计时 60s
        mDisposables.add(Flowable.intervalRange(0, 2, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    Log.e("sangxiang", "计时:" + it)
                }
                .doOnComplete {
                    Log.e("sangxiang", "计时完毕")
                    onNextUI()
                    //startActivity<TestApiActivity>()
                }
                .subscribe())
    }

    private fun onNextUI() {
        if (Hawk.get<Boolean>(SpConfig.GUIDE_STATUS) != null && Hawk.get<Boolean>(SpConfig.GUIDE_STATUS)) {
//            //如果存在GUIDE_STATUS则表示已经打开过引导页
//            if (Hawk.get<Boolean>(SpConfig.LOGIN_STATUS) != null && Hawk.get<Boolean>(SpConfig.LOGIN_STATUS)) {
//                //如果LOGIN_STATUS=true则表示已登录，直接跳转到MainActivity
//                startActivity<MainActivity>()
//            } else {
//                //否则跳转到登录页
//                startActivity<LoginActivity>()
//            }
            startActivity<MainActivity>()

        } else {
            //不存在GUIDE_STATUS则跳转引导页
            startActivity<GuideActivity>()
        }
        finish()
    }

    /**
     * 测试
     */
    @Subscribe
    fun test(ss: String) {

    }

}
