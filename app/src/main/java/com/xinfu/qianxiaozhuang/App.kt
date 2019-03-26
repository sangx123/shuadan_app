package com.xinfu.qianxiaozhuang

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.mob.MobSDK
import com.orhanobut.hawk.Hawk
import com.tencent.bugly.crashreport.CrashReport
import com.wenming.library.LogReport
import com.wenming.library.save.imp.CrashWriter
import com.wenming.library.upload.email.EmailReporter
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
        //eventbus初始化
        EventBus.builder().addIndex(MEventBusIndex()).installDefaultEventBus()
        //bugly初始化
        CrashReport.initCrashReport(this, Config.BuglyAppID, true)
        //初始化shareperferrence
        Hawk.init(this).setLogInterceptor { message -> error{message} }.build()
        //短信初始化
        MobSDK.init(this);
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
        //错误日志上传初始化
        initCrashReport()
        initEmailReporter()



    }


    private fun initCrashReport() {
        //FileUtil.deleteDir(File(LogReport.getInstance().root))//删除LogReport的内的文件
        LogReport.getInstance()
                .setCacheSize((30 * 1024 * 1024).toLong())//支持设置缓存大小，超出后清空
                .setLogDir(applicationContext, "sdcard/" + this.getString(this.applicationInfo.labelRes) + "/mylog/")//定义路径为：sdcard/[app name]/
                .setWifiOnly(false)//设置只在Wifi状态下上传，设置为false为Wifi和移动网络都上传
                .setLogSaver(CrashWriter(applicationContext))//支持自定义保存崩溃信息的样式
                //.setEncryption(new AESEncode()) //支持日志到AES加密或者DES加密，默认不开启
                .init(applicationContext)

    }

    /**
     * 使用EMAIL发送日志
     */
    private fun initEmailReporter() {
        val email = EmailReporter(this)
        email.setReceiver("sangx@xueshandai.com")//收件人
        email.setSender("sangx@xueshandai.com")//发送人邮箱
        email.setSendPassword("Xsd@123")//邮箱密码
        email.setSMTPHost("smtp.exmail.qq.com")//SMTP地址
        email.setPort("465")//SMTP 端口
        LogReport.getInstance().setUploadType(email)
    }
}