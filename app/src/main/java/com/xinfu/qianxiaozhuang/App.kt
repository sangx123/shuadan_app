package com.xinfu.qianxiaozhuang

import android.app.Application
import android.os.Environment
import com.jakewharton.threetenabp.AndroidThreeTen
import com.mob.MobSDK
import com.orhanobut.hawk.Hawk
import com.tencent.bugly.crashreport.CrashReport
import com.xinfu.qianxiaozhuang.BuildConfig
import com.xinfu.qianxiaozhuang.MEventBusIndex
import com.xinfu.qianxiaozhuang.sqllite.MyObjectBox
import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.*
import java.io.File
import java.io.IOException

class App :Application(),AnkoLogger {

    private lateinit var rootFileDir: File
    private lateinit var cacheImage: File
    lateinit var fileRecorder: File//七牛断点续传文件夹
    companion object {

        lateinit var mInstance: App
        lateinit var lastIp: File
        lateinit var requestLog: File
        lateinit var mBoxStore: BoxStore
        fun getInstance(): App {
            return mInstance
        }
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        AndroidThreeTen.init(this)
        //eventbus初始化
        EventBus.builder().addIndex(MEventBusIndex()).installDefaultEventBus()
        //bugly初始化
        CrashReport.initCrashReport(this, Config.BuglyAppID, true)
        //初始化shareperferrence
        Hawk.init(this).setLogInterceptor { message -> error{message} }.build()
        //短信初始化
        MobSDK.init(this)
        rootFileDir = File(Environment.getExternalStorageDirectory(), "sangxiang")
        rootFileDir?.let {
            if (!it.exists()) {
                it.mkdirs()
            }
        }

        fileRecorder = File(rootFileDir, "recordFile")
        fileRecorder?.let {
            if (!it.exists()) {
                it.mkdirs()
            }
        }

//        JPushInterface.setDebugMode(true)
//        JPushInterface.init(this)
//        rootFileDir=File(Config.ROOT_PATH)
//        if (!rootFileDir.exists()) {
//            rootFileDir.mkdir()
//        }
//        lastIp = File(rootFileDir, "lastIP")
//        if (!lastIp.exists()) {
//            try {
//                lastIp.createNewFile()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//
//        }
//
//        cacheImage = File(rootFileDir, "cacheImage")
//        if (!cacheImage.exists()) {
//            cacheImage.mkdirs()
//        }
//
//        requestLog = File(rootFileDir, "requestLog")
//        if (!requestLog.exists()) {
//            try {
//                requestLog.createNewFile()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//
//        } else {
//            val delete = requestLog.delete()
//            if (delete) {
//                try {
//                    requestLog.createNewFile()
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//
//            } else {
//                println("------can not delete request log------")
//            }
//
//        }
        //BoxStore数据库初始化
        mBoxStore = MyObjectBox.builder().androidContext(this@App).build()

        if (BuildConfig.DEBUG) {
             AndroidObjectBrowser(mBoxStore).start(this);
        }
    }
}