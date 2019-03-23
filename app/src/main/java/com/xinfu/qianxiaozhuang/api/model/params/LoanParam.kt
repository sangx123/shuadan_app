package com.xinfu.qianxiaozhuang.api.model.params

data class MemberContactParam(
        var contactName:String?=null,
        var contactRel:String?=null,
        var contactPhone:String?=null,
        var otherName:String?=null,
        var otherRel:String?=null,
        var otherPhone:String?=null
):BaseParam()

data class SignSmsParam(var bankAccount:String?=null,var mobile:String?=null,var realName:String?=null,var idcard:String?=null):BaseParam()

data class SignBankPayParam(var bankAccount:String?=null,var mobile:String?=null,var msgCode:String?=null,var realName:String?=null,var idcard:String?=null):BaseParam()
data class RefundApplicationList(var bannerId:String?=null,var path:String?=null)

data class RefundApplicationParam(var reportId:String,var list:ArrayList<RefundApplicationList>?=null):BaseParam()

