package com.xinfu.qianxiaozhuang.api

import com.google.gson.annotations.SerializedName

open class BaseResult<T>(
        @SerializedName("version") val version: Int = 0,
        @SerializedName("cmd") val cmd: String = "",
        @SerializedName("pageType") val pageType: String = "",
        @SerializedName("respCode") val respCode: String = "",
        @SerializedName("respMsg") val respMsg: String = "",
//		@SerializedName("data") var data: JsonObject?= null ,
        @SerializedName("data") var data: T? = null,
        @SerializedName("pageSize") val pageSize: Int = 0,
        @SerializedName("pageNumber") val pageNumber: Int = 0,
        @SerializedName("sign") val sign: String = "",
        @SerializedName("lastPage") val isLastPage:Boolean = true,
        var code: String? = null,
        var message: String? = null,
        var result: T?=null,
        var getResultFlag:Boolean = false,
        var getResultBnakFlag:Boolean=false,
        var loginStatus :Boolean= false//登录状态(true 超时)
){
    /**
     * 获取结果成功的标识符
     */
   open fun getResultSuccess(): Boolean {
        getResultFlag = code == "000000"
        return getResultFlag
    }

    /**
     * 判断登录状态(个贷后加的DL0002)
     * DL0001: 超时,DL0002: 账号信息为空
     * @return
     */
    fun isLoginTimeOut(): Boolean {

        if (code == "DL0001" || code == "DL0002") {
            loginStatus = true
        }
        return loginStatus
    }

    /*
    是否有银行卡
     */
    fun getResultBank(): Boolean {

        if (code == "YH0001") {// 没有绑定银行卡
            getResultBnakFlag = true
        }
        return getResultBnakFlag
    }

    /*
  * 是否有实名认证
  */
    fun getRealNameResult(): Boolean {

        if (code == "AQ0003") {// 没有绑定银行卡
            getResultBnakFlag = true
        }
        return getResultBnakFlag
    }
}