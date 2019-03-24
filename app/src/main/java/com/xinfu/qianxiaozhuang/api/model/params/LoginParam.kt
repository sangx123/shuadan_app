package com.xinfu.qianxiaozhuang.api.model.params

import com.google.gson.annotations.SerializedName

data class LoginParam(var mobile:String?=null, var password:String?=null,var authCode:String?=null)
//data class RegisterParam( var authCode: String? = null,
//                          var username: String? = null,
//                          var password: String? = null,
//                          var channel: String? = null)

data class RegisterParam(
       var name: String ?=null,
       var mobile: String ?=null,
       var password: String?=null
)

data class SmsParam(var mobile:String?=null)