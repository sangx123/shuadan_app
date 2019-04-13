package com.xinfu.qianxiaozhuang.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import com.orhanobut.hawk.Hawk
import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.SpConfig
import com.xinfu.qianxiaozhuang.activity.home.HomeFragment
import com.xinfu.qianxiaozhuang.activity.loan.LoanFragment
import com.xinfu.qianxiaozhuang.activity.login.LoginActivity
import com.xinfu.qianxiaozhuang.activity.my.MyFragment
import com.xinfu.qianxiaozhuang.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity


class MainActivity : BaseActivity() {
    private var currentFragment: Fragment? = null
    private var nav1Fragment: Fragment = HomeFragment()
    private var nav2Fragment: Fragment = MyFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
    }

    private fun initUI() {
        mNavBar.active = 1
    }

    fun switchFragment(position: Int): FragmentTransaction {
        var targetFragment: Fragment? = null
        when (position) {
            1 -> targetFragment = nav1Fragment
            2 -> targetFragment = nav2Fragment
        }
        var transaction = supportFragmentManager.beginTransaction()
        if (!targetFragment!!.isAdded) {
            //第一次使用switchFragment()时currentFragment为null，所以要判断一下        
            currentFragment?.let {
                transaction.hide(it)
            }
            transaction.add(R.id.mainContainer, targetFragment, targetFragment.javaClass.name)
        } else {
            currentFragment?.let {
                transaction.hide(it).show(targetFragment)
                if (position == 1) {//首页
                    //todo 暂时先不刷新页面
                    //(targetFragment as LoanFragment).onRefreshData()
                } else if (position == 2) {//我的
                    //todo 暂时先不刷新页面
                   //(targetFragment as MyFragment).onRefreshData()
                }
            }
        }
        transaction.commitAllowingStateLoss()
        currentFragment = targetFragment
        return transaction
    }

    /**
     * 是否登录
     */
    fun isLoginStatus(): Boolean {

        var isLogin: Boolean = true
        if (Hawk.get<Boolean>(SpConfig.LOGIN_STATUS) == null || !(Hawk.get<Boolean>(SpConfig.LOGIN_STATUS))) {
            //如果LOGIN_STATUS=true则表示已登录，直接跳转到MainActivity
            isLogin = false
            startActivity<LoginActivity>()
        }
        return isLogin

    }

    override fun setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this@MainActivity, null)
    }

    override fun onNewIntent(intent: Intent?) {

        var where_active: Int = intent!!.getIntExtra("where_active", 1)
        mNavBar.active = where_active

    }

}
