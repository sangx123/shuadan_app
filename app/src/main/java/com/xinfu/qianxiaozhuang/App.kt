package com.xinfu.qianxiaozhuang

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
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
        EventBus.builder().addIndex(MEventBusIndex()).installDefaultEventBus()
        CrashReport.initCrashReport(this, Config.BuglyAppID, true)
        //初始化shareperferrence
        Hawk.init(this).setLogInterceptor { message -> error{message} }.build()
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
        mBoxStore = MyObjectBox.builder().androidContext(this@App).build()

        if (BuildConfig.DEBUG) {
             AndroidObjectBrowser(mBoxStore).start(this);
        }

    }
}