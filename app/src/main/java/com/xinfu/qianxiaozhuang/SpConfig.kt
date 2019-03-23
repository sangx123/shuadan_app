package com.xinfu.qianxiaozhuang

import android.os.Environment
import java.io.File

/**
 * shareperference变量的存储
 */
class SpConfig{
    companion object {
        /**
         * 是否启动过GUID
         */
        const val GUIDE_STATUS:String ="GUIDE_STATUS"

        /**
         * 是否已登录
         */
        const val LOGIN_STATUS:String ="LOGIN_STATUS"

        const val memberId:String ="memberId"
        const val accessToken:String ="accessToken"

    }
}