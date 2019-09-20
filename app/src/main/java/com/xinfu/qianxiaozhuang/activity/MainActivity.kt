package com.xinfu.qianxiaozhuang.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.widget.Toast
import com.orhanobut.hawk.Hawk
import com.tbruyelle.rxpermissions2.RxPermissions
import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.SpConfig
import com.xinfu.qianxiaozhuang.activity.home.HomeFragment
import com.xinfu.qianxiaozhuang.activity.loan.LoanFragment
import com.xinfu.qianxiaozhuang.activity.login.LoginActivity
import com.xinfu.qianxiaozhuang.activity.my.MyFragment
import com.xinfu.qianxiaozhuang.utils.StatusBarUtil
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity


class MainActivity : BaseActivity() {
    private var currentFragment: Fragment? = null
    private var nav1Fragment: Fragment = HomeFragment()
    private var nav2Fragment: Fragment = MyFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var rxPermissions= RxPermissions(this);
//        rxPermissions.requestEach(Manifest.permission.CAMERA,
//                Manifest.permission.RECORD_AUDIO)
//                .subscribe(new Consumer<Permission>() {
//                    @Override
//                    public void accept(Permission permission) throws Exception {
//                        if (permission.name.equals(Manifest.permission.CAMERA)) {
//                            if (permission.granted) {
//                                String jpgPath = getCacheDir() + "test.jpg";
//                                //takePhotoByPath(jpgPath, 2);
//                                Log.e(TAG, "accept: CAMERA" );
//                            } else {
//                                // 未获取权限
//                                Toast.makeText(MainActivity.this, "您没有授权摄像头该权限，请在设置中打开授权", Toast.LENGTH_SHORT).show();
//                            }
//
//                        } else if (permission.name.equals(Manifest.permission.RECORD_AUDIO)) {
//                            if (permission.granted) {
//                                Log.e(TAG, "accept: RECORD_AUDIO" );
//                                String jpgPath = getCacheDir() + "test.jpg";
//                                //takePhotoByPath(jpgPath, 2);
//                            } else {
//                                // 未获取权限
//                                Toast.makeText(MainActivity.this, "您没有授权录音该权限，请在设置中打开授权", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                });
        rxPermissions.request(Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO)
                .subscribe(Consumer<Boolean> { aBoolean ->
                    if (aBoolean!!) {
                        //申请的权限全部允许
                        Toast.makeText(this@MainActivity, "允许了权限!", Toast.LENGTH_SHORT).show()
                    } else {
                        //只要有一个权限被拒绝，就会执行
                        Toast.makeText(this@MainActivity, "未授权权限，部分功能不能使用", Toast.LENGTH_SHORT).show()
                    }
                })
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


    override fun onNewIntent(intent: Intent?) {

        var where_active: Int = intent!!.getIntExtra("where_active", 1)
        mNavBar.active = where_active

    }

}
