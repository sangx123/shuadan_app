package com.xinfu.qianxiaozhuang

import android.os.Environment
import java.io.File

/**
 * 文件目录常量配置
 */
 class Config{
    companion object {
        // app文件存放路径
        open val ROOT_PATH:String=(Environment
                .getExternalStorageDirectory().toString()
        + File.separator
        + "one"
        + File.separator)
        @JvmField val APK_DOWNLOAD_PATH:String = (ROOT_PATH+
                "downloadApk" + File.separator)
        const val SUCCESS:String ="000"
        //腾讯bugly
        const val BuglyAppID="6f1df86aca"
        const val MAX_FAILURE_COUNT = 1

        const val requestCode100="100"
        const val requestCode101="101"
        const val requestCode102="102"



    }
}