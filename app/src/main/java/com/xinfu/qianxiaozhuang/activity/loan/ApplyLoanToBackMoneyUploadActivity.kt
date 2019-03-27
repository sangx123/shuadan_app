package com.xinfu.qianxiaozhuang.activity.loan

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.orhanobut.hawk.Hawk
import com.xiang.one.network.error.RxUtils
import com.xinfu.qianxiaozhuang.R
import com.xinfu.qianxiaozhuang.SpConfig
import com.xinfu.qianxiaozhuang.activity.BaseActivity
import com.xinfu.qianxiaozhuang.activity.MainActivity
import com.xinfu.qianxiaozhuang.adapter.RefundPictureUploadAdapter
import com.xinfu.qianxiaozhuang.api.Api
import com.xinfu.qianxiaozhuang.api.BaseResult
import com.xinfu.qianxiaozhuang.api.model.RefundUploadModel
import com.xinfu.qianxiaozhuang.api.model.RefundViewItemlModel
import com.xinfu.qianxiaozhuang.api.model.params.BaseParam
import com.xinfu.qianxiaozhuang.api.model.params.FeedbackParam
import com.xinfu.qianxiaozhuang.api.model.params.RefundApplicationList
import com.xinfu.qianxiaozhuang.api.model.params.RefundApplicationParam
import com.xinfu.qianxiaozhuang.events.UserEvent
import com.xinfu.qianxiaozhuang.utils.PhotoUtils
import com.xinfu.qianxiaozhuang.utils.PhotoUtils.compressImage
import com.xinfu.qianxiaozhuang.widget.DividerItemDecoration
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_apply_loan_to_back_money_upload.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.error
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.io.File
import java.util.ArrayList

/**
 * 借款申请----急速退款上传截图
 */
class ApplyLoanToBackMoneyUploadActivity : BaseActivity(), RefundPictureUploadAdapter.IRecycleViewCallBack {


    var items: ArrayList<RefundViewItemlModel>? = null
    var lastPositon = 0;
    lateinit var selectList: MutableList<LocalMedia>
    var reportId = ""
    var RefundApplicationList = ArrayList<RefundApplicationList>()
    lateinit var refundPictureUploadAdapter: RefundPictureUploadAdapter
    var mMap = HashMap<String, Int>()

    companion object {
        val param_data = "param_data"
        val param_reportID = "param_reportID"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_loan_to_back_money_upload)
        intent.getSerializableExtra(param_data)?.let {
            items = it as ArrayList<RefundViewItemlModel>
        }
        intent.getSerializableExtra(param_reportID)?.let {
            reportId = it.toString()
        }
        initUI()
        if (reportId.isNullOrBlank()) {
            toast("reportId不能为空！")
            finish()
        }
    }

    private fun initUI() {
        //titlebar_withdrawsetTitle("急速退款")
        //titlebar_withdrawsetTitleStyle(Typeface.DEFAULT_BOLD)
        //titlebar_withdrawsetTxtBackVisibility(View.VISIBLE)
        //titlebar_withdrawsetTitleCustomTextColor(resources.getColor(R.color.black))
        //titlebar_withdrawsetDrawableForTxtBack(R.drawable.icon_back)
        //titlebar_withdrawsetBackWidgetOnClick({ finish() }, null)

        recyclerView.addItemDecoration(DividerItemDecoration(this@ApplyLoanToBackMoneyUploadActivity, DividerItemDecoration.VERTICAL_LIST))
        recyclerView.setHasFixedSize(true)
        recyclerView.setLayoutManager(LinearLayoutManager(this@ApplyLoanToBackMoneyUploadActivity))

        refundPictureUploadAdapter = RefundPictureUploadAdapter(this@ApplyLoanToBackMoneyUploadActivity, items, this@ApplyLoanToBackMoneyUploadActivity)
        recyclerView.adapter = refundPictureUploadAdapter

    }

    override fun onUploadPictureBack() {
        getRefundApplication()
    }

    override fun choicePhoto(position: Int) {
        lastPositon = position
        if (!mMap.containsKey(lastPositon.toString())) {
            PictureSelector.create(this@ApplyLoanToBackMoneyUploadActivity)
                    .openGallery(PictureMimeType.ofImage())
                    // .selectionMedia(selectList)// 是否传入已选图片
                    .maxSelectNum(3)
                    //.compress(true)
                    .forResult(PictureConfig.CHOOSE_REQUEST)
        } else {
            toast("已经上传")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data)
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
//                    for (media in selectList) {
//                        Log.i("图片-----》", media.getPath())
//                    }
                    //adapter.setList(selectList)
                    //adapter.notifyDataSetChanged()
                    selectList?.let {
                        if (it.size > 0) {
                            //上传图片
                            var path1 = ""
                            var path2 = ""
                            var path3 = ""
                            for (i in it.indices) {
                                when (i) {
                                    0 -> {
                                        path1 = it[i].path
                                    }
                                    1 -> {
                                        path2 = it[i].path
                                    }
                                    2 -> {
                                        path3 = it[i].path
                                    }
                                }
                            }
                            items!!.get(lastPositon).path1 = path1
                            items!!.get(lastPositon).path2 = path2
                            items!!.get(lastPositon).path3 = path3
                            refundPictureUploadAdapter.notifyItemRangeChanged(0, refundPictureUploadAdapter.itemCount - 1)
                            getRefundUpload(path1, path2, path3)
                        }
                    }

                }
            }
        }
    }


    /**
     *上传图片
     */
    fun getRefundUpload(mPhotoPath1: String = "", mPhotoPath2: String = "", mPhotoPath3: String = "") {
        var map = HashMap<String, RequestBody>()
        map[SpConfig.memberId] = RequestBody.create(MediaType.parse("text/plain"), Hawk.get<String>(SpConfig.memberId))
        map[SpConfig.accessToken] = RequestBody.create(MediaType.parse("text/plain"), Hawk.get<String>(SpConfig.accessToken))
//        val file = File(mPhotoPath)
//        map["image"] = RequestBody.create(MediaType.parse("image/jpeg"), file)
        showApiProgress()
        Api.getApiService().getRefundUpload(map, getImageContent(mPhotoPath1)!!, getImageContent(mPhotoPath2), getImageContent(mPhotoPath3))
                .compose(RxUtils.handleGlobalError<BaseResult<RefundUploadModel>>(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<RefundUploadModel>> {
                    override fun onComplete() {
                        hideApiProgress()
                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(t: BaseResult<RefundUploadModel>) {
                        t.result?.let {
                            toast("保存成功！")
                            if (mMap.containsKey(lastPositon.toString())) {
                                return
                            }
                            it.path?.let {
                                for (item in it) {
                                    RefundApplicationList.add(RefundApplicationList(items?.get(lastPositon)!!.id.toString(), item))
                                }
                            }
                            mMap.put(lastPositon.toString(), lastPositon)
                        }

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
    }

    private fun getImageContent(mPhotoPath: String = ""): MultipartBody.Part? {
        if (mPhotoPath.isNullOrBlank()) {
            return null
        }
        val MIMETYPE_IMAGE_JPEG = "image/jpeg"
        val file = File(mPhotoPath)
        return MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse(MIMETYPE_IMAGE_JPEG), file))
    }


    /**
     * 申请退款
     */
    fun getRefundApplication() {
        var model = RefundApplicationParam(reportId)
        model.list = RefundApplicationList
        showApiProgress()
        Api.getApiService().getRefundApplication(model)
                .compose(RxUtils.handleGlobalError<BaseResult<String>>(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<BaseResult<String>> {
                    override fun onComplete() {
                        hideApiProgress()
                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(t: BaseResult<String>) {
                        toast("申请成功！")
                        val userEvent = UserEvent()//MyOrderActivity需要数据重新刷新
                        userEvent.setRefreshForMyOrderActivity(true)
                        EventBus.getDefault().post(userEvent)
                        //QueryBackMoneyActivity 模式设置成singleTask, 点返回会退到MyOrderActivity页面，不需要再刷新
                        startActivity(Intent(this@ApplyLoanToBackMoneyUploadActivity, QueryBackMoneyActivity::class.java).putExtra("result", 1))

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
    }
}
