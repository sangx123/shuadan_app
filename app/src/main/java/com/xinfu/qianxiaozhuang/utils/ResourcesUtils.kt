package com.xinfu.qianxiaozhuang.utils

import android.support.v4.content.ContextCompat
import com.xinfu.qianxiaozhuang.App
class ResourcesUtils{
    companion object {
       fun getColor(id:Int):Int{
         return  ContextCompat.getColor(App.getInstance(),id)
       }
    }
}
