package com.xinfu.qianxiaozhuang.utils

import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import com.xinfu.qianxiaozhuang.App
class ResourcesUtils{
    companion object {
       fun getColor(@ColorRes id:Int):Int{
         return  ContextCompat.getColor(App.getInstance(),id)
       }

    }
}
