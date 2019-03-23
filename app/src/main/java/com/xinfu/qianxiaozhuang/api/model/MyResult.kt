package com.xinfu.qianxiaozhuang.api.model

import java.io.Serializable
import java.util.ArrayList

data class FeedbackModel(var message: String? = null)
data class LoanRecordModel(var list: ArrayList<LoanRecordItemModel>? = null) : Serializable

data class LoanRecordItemModel(var orderId: String? = null, var reportId: String? = null, var status: Int? = null)

data class MyWalletModel(var cash: Int = 0,
                         var authenticateBank: String? = null,
                         var handlingFee: Int = 0)

/**
 * 推送结果
 */
data class PushResultModel(var creditLimit: Double? = 0.00,// 预授信额度
                           var isShow: Boolean? = false, //扣费弹窗显示  false不显示
                           var list: ArrayList<RefundViewItemlModel>? = null) : Serializable


/**
 * 急速退款
 */
data class RefundViewModel(var isRefund: Boolean = false,
                           var reportId: String? = null,
                           var list: ArrayList<RefundViewItemlModel>? = null) : Serializable

data class RefundViewItemlModel(var title: String? = null, // 标题
                                var id: Long = 0L,//平台编号
                                var platform: String,//平台名称
                                var amount: String? = null,//可借金额
                                var probability: String,// 成功概率
                                var description: String,// 平台描述
                                var link: String,// 平台连接
                                var logo: String,// 平台logo
                                var path1:String="",
                                var path2:String="",
                                var path3:String=""

) : Serializable


data class ExamineAndApproveModel(var memberInfo: Boolean = false,
                                  var memberContact: Boolean = false,
                                  var authenticateBank: Boolean = false)

/**
 * 用户中心
 */
data class UserCenterModel(var mobile: String? = null,
                           var authentication: Boolean = false)

/**
 * 银行卡列表
 */
data class BankListModel(var bankName: String? = null,
                         var authenticateBank: String? = null,
                         var bankLogo: String? = null//预留字段
                        )