package com.xinfu.qianxiaozhuang.api.model

/**
 * 登录
 */
data class LoginModel (
    var userToken: String? = null,//令牌

    val userId: String? = null//用户ID
)