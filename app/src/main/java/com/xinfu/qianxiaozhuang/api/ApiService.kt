package com.xinfu.qianxiaozhuang.api

import com.xinfu.qianxiaozhuang.api.model.*
import com.xinfu.qianxiaozhuang.api.model.params.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * 定义接口
 */
@JvmSuppressWildcards
interface ApiService {

    /**
     * 通用
     */
    @InterfaceUseCase("七牛图片上传的token获取")
    @POST("api/qiniu/appCommonToken")
    //@POST("/qiniu/appCommonToken")
    fun getQiNiuToken(): Observable<BaseResult<QiniuModel>>

    /**
     * 通用上传图片
     */
    @InterfaceUseCase("上传图片")
    @Multipart
    @POST("api/upload/uploadImage")
    fun uploadImage(@Part image: MultipartBody.Part): Observable<BaseResult<String>>

    /**
     * 通用上传图片数组
     */
    @InterfaceUseCase("上传图片数组")
    @Multipart
    @POST("api/upload/uploadImageList")
    fun uploadImageList(@PartMap  model:Map<String, RequestBody>): Observable<BaseResult<String>>


    //新的api
    /**
     * 发布创建任务
     */
    @InterfaceUseCase("上传图片数组")
    @Multipart
    @POST("api/task/createTask")
    fun createTask(@PartMap  model:Map<String, RequestBody>): Observable<BaseResult<String>>

    /**
     * 获取首页的任务列表
     */
    @InterfaceUseCase("获取首页的任务列表")
    @POST("api/task/getHomeTaskList")
    fun getHomeTaskList(@Body model: HomeTaskParam): Observable<BaseResult<ArrayList<Task>>>


    /**
     * 申请任务
     */
    @InterfaceUseCase("申请任务")
    @POST("api/task/applyTask")
    fun getApplyTask(@Body model: ApplyTaskParam): Observable<BaseResult<String>>

    /**
     * 我发布的任务列表
     */
    @InterfaceUseCase("我发布的任务列表")
    @POST("api/task/getMyPublishTaskList")
    fun getMyPublishTaskList(@Body model: HomeTaskParam): Observable<BaseResult<ArrayList<Task>>>

//    /**
//     * 登录相关
//     */
//    @InterfaceUseCase("登录")
//    @POST("login/index")
//    fun login(@Body model: LoginParam): Observable<BaseResult<LoginModel>>

    @InterfaceUseCase("登录")
    @POST("/api/index/login")
    fun login(@Body model: LoginParam): Observable<BaseResult<LoginModel>>

//    @InterfaceUseCase("注册")
//    @POST("register/index")
//    fun register(@Body model: RegisterParam): Observable<BaseResult<String>>

    @InterfaceUseCase("注册")
    @POST("/api/index/register")
    fun register(@Body model: RegisterParam): Observable<BaseResult<LoginModel>>

    @InterfaceUseCase("获取注册验证码")
    @POST("register/sms")
    fun getSMS(@Body model: SmsParam): Observable<BaseResult<String>>

    @InterfaceUseCase("获取注册验证码")
    @POST("login/sms")
    fun loginSMS(@Body model: SmsParam): Observable<BaseResult<String>>



    @InterfaceUseCase("首页")
    @POST("index/index")
    fun myhome(@Body model: HomeParam): Observable<BaseResult<HomeModel>>


    /**
     * 个人中心
     */
    @InterfaceUseCase("用户中心")
    @POST("center-home/index")
    fun getUserCenter(@Body model: BaseParam): Observable<BaseResult<UserCenterModel>>

    @InterfaceUseCase("用户反馈")
    @POST("center-home/feedback")
    fun getFeedback(@Body model: FeedbackParam):Observable<BaseResult<FeedbackModel>>

    @InterfaceUseCase("我的订单")
    @POST("center-home/order-record")
    fun getOrderRecord(@Body model: BaseParam):Observable<BaseResult<LoanRecordModel>>

    @InterfaceUseCase("借款记录")
    @POST("center-home/loan-record")
    fun getLoanRecord(@Body model: BaseParam):Observable<BaseResult<LoanRecordModel>>

    @InterfaceUseCase("我的钱包")
    @POST("center-home/my-wallet")
    fun getMyWallet(@Body model: BaseParam):Observable<BaseResult<MyWalletModel>>

    @InterfaceUseCase("急速退款页面")
    @POST("center-home/refund-view")
    fun getRefundView(@Body model: BaseParam): Observable<BaseResult<RefundViewModel>>

    @InterfaceUseCase("上传图片")
    @Multipart
    @POST("center-home/refund-upload")
    fun getRefundUpload(@PartMap model:Map<String, RequestBody>,@Part image: MultipartBody.Part,@Part image1: MultipartBody.Part?,@Part image2: MultipartBody.Part?):Observable<BaseResult<RefundUploadModel>>

    /**
     * 借款相关
     */

    @InterfaceUseCase("认证接口")
    @POST("member-info/examine-and-approve")
    fun getExamineAndApprove(@Body model: BaseParam):Observable<BaseResult<ExamineAndApproveModel>>



    @InterfaceUseCase("保存联系人")
    @POST("member-info/member-contact")
    fun saveMemberContact(@Body model: MemberContactParam):Observable<BaseResult<EmergencyContactModel>>


    @InterfaceUseCase("查询联系人")
    @POST("member-info/get-member-contact")
    fun getMemberContact(@Body model: BaseParam):Observable<BaseResult<EmergencyContactModel>>


    @InterfaceUseCase("绑定银行卡页面")
    @POST("member-info/sign-bank-pay-view")
    fun getSignBankPayView(@Body model: BaseParam):Observable<BaseResult<SignBankPayViewModel>>

    @InterfaceUseCase("银行卡列表")
    @POST("center-home/my-bank-card")
    fun getBankList(@Body model: BaseParam): Observable<BaseResult<BankListModel>>

    @InterfaceUseCase("签约短信")
    @POST("member-info/sign-sms")
    fun getSignSms(@Body model: SignSmsParam):Observable<BaseResult<String>>

    @InterfaceUseCase("签约银行卡")
    @POST("member-info/sign-bank-pay")
    fun getSignBankPay(@Body model: SignBankPayParam):Observable<BaseResult<String>>

    @InterfaceUseCase("评估数据")
    @POST("member-info/credit-limit")
    fun getCreditLimit(@Body model: BaseParam):Observable<BaseResult<CreditLimitModel>>

    @InterfaceUseCase("申请推送产品")
    @POST("borrow/index")
    fun getBorrow(@Body model: BaseParam):Observable<BaseResult<PushResultModel>>

    @InterfaceUseCase("客服")
    @POST("center-home/customer-service")
    fun getCustomerService():Observable<BaseResult<CustomerServiceModel>>

    @InterfaceUseCase("倒计时")
    @POST("borrow/deadline")
    fun getDeadline(@Body model: BaseParam):Observable<BaseResult<DeadlineModel>>

    @InterfaceUseCase("申请退款")
    @POST("center-home/refund-application")
    fun getRefundApplication(@Body model: RefundApplicationParam):Observable<BaseResult<String>>

    @InterfaceUseCase("身份证识别")
    @Multipart
    @POST("member-info/id-card-discern")
    fun getIdCardDiscern(@PartMap model:Map<String, RequestBody>,@Part image: MultipartBody.Part):Observable<BaseResult<IdCardDiscernModel>>

    @InterfaceUseCase("保存身份数据")
    @Multipart
    @POST("member-info/id-card-check")
    fun getIdCardCheck(@PartMap model:Map<String, RequestBody>,@Part image: MultipartBody.Part):Observable<BaseResult<String>>


}

