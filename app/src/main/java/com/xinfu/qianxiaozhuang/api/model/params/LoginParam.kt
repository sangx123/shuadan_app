package com.xinfu.qianxiaozhuang.api.model.params

data class LoginParam(var username:String?=null, var password:String?=null,var authCode:String?=null): BaseParam()
data class RegisterParam( var authCode: String? = null,
                          var username: String? = null,
                          var password: String? = null,
                          var channel: String? = null)

data class SmsParam(var mobile:String?=null)