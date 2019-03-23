package com.xinfu.qianxiaozhuang.api.model
data class CreditLimitModel(var creditLimit:Int?=null)

class CustomerService {
    var name: String = ""
    var sequence: String = ""
}

class CustomerServiceModel {
    var date: String = ""
    var list: List<CustomerService>? = null
}


data class DeadlineModel(var deadline:String="")


data class RefundUploadModel( var path: List<String>? = null)


data class IdCardDiscernModel (
    var ISSUE: String? = "",
    var BIRTHDAY: String? = "",
    var NAME: String? = "",
    var headimg: String? = "",
    var ADDRESS: String? = "",
    var FOLK: String? = "",
    var SEX: String? = "",
    var PERIOD: String? = "",
    var NUM: String? = ""
)
data class SignBankPayViewModel(var realName:String="")
