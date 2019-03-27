package com.xinfu.qianxiaozhuang.activity.my;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xinfu.qianxiaozhuang.R;
import com.xinfu.qianxiaozhuang.activity.BaseActivity;
import com.xinfu.qianxiaozhuang.api.Api;
import com.xinfu.qianxiaozhuang.api.BaseResult;
import com.xinfu.qianxiaozhuang.api.model.FeedbackModel;
import com.xinfu.qianxiaozhuang.api.model.params.FeedbackParam;
import com.xinfu.qianxiaozhuang.utils.ToastUtil;
import com.xinfu.qianxiaozhuang.widget.CommonTitleBar;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 意见反馈
 */
public class FeedbackActivity extends BaseActivity implements CommonTitleBar.IClickTxtBack, View.OnClickListener {

    private CommonTitleBar commonTitleBar;
    private EditText et_content;
    private TextView text_count;
    private TextView txt_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initUI();
    }

    private void initUI() {

//        commonTitleBar = findViewById(R.id.titlebar_withdraw);
//        commonTitleBar.setTitle("意见反馈");
//        commonTitleBar.setTitleStyle(Typeface.DEFAULT_BOLD);
//        commonTitleBar.setTxtBackVisibility(View.VISIBLE);
//        commonTitleBar.setTitleCustomTextColor(getResources().getColor(R.color.black));
//        commonTitleBar.setDrawableForTxtBack(R.drawable.icon_back);
//        commonTitleBar.setBackWidgetOnClick(FeedbackActivity.this, null);

        et_content = findViewById(R.id.et_content);
        text_count = findViewById(R.id.text_count);
        et_content.addTextChangedListener(new TextWatcher() { //对EditText进行监听
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String tempValue = editable.toString();
                if (tempValue.contains(" ")) {
                    editable = editable.delete(0, editable.length());
                }
                text_count.setText(editable.length() + "/200");
            }
        });
        txt_submit = findViewById(R.id.txt_submit);
        txt_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.txt_submit:
                String tempValue = et_content.getText().toString();
                tempValue = tempValue.replaceAll(" ", "");
                if (!TextUtils.isEmpty(tempValue)) {
                    //onSubmitData(tempValue);
                    getFeedBack(tempValue);
                } else {
                    ToastUtil.onDisplayToast(FeedbackActivity.this, "请输入您要咨询的问题");
                }
                break;
        }

    }

    /**
     * 提交数据
     */
//    private void onSubmitData(String tempValue) {
//
//        final BaseRequest br = new BaseRequest();
//        RequestParams params = new RequestParams(FeedbackActivity.this);
//        params.addFormDataPart("content",tempValue);
//        br.addCommonParams(params);
//        params.applicationJson();
//
//        HttpRequest.post(APIManager.feedbackUrl, params, new BaseHttpRequestCallback<TFeedBackModel>() {
//
//            @Override
//            public void onStart() {
//                showRequestDataDialog();
//            }
//
//            @Override
//            public void onFinish() {
//                dismissRequestDataDialog();
//            }
//
//            @Override
//            protected void onSuccess(TFeedBackModel resultModel) {
//
//                if (!resultModel.isLoginTimeOut()) {//没有超时
//                    if (resultModel.getResultSuccess()) {
//
//
//                        FeedBackModel aboutUsModel = resultModel.getResult();
//                        ToastUtil.onDisplayToast(FeedbackActivity.this, getResources().getString(R.string.opinion_info_success));
//                        finish();
//
//                    } else {
//                        ToastUtil.onDisplayToast(FeedbackActivity.this, resultModel.getMessage());
//                    }
//                } else {
//                    String tipContent = resultModel.getMessage();
//                    br.openTimeOutActivity(FeedbackActivity.this, tipContent);
//                    ToastUtil.onDisplayToast(FeedbackActivity.this, tipContent);
//                }
//            }
//
//            @Override
//            public void onFailure(int errorCode, String msg) {
//                ToastUtil.onDisplayToast(FeedbackActivity.this, msg);
//            }
//        });
//
//    }


    @Override
    public void onClickTxtBackCallBack() {
        finish();
    }

    @NotNull
    @Override
    public String getLoggerTag() {
        return null;
    }


    /**
     *用户反馈
     */
    void getFeedBack(String str){
        FeedbackParam model=new FeedbackParam();
        model.setContent(str);
        Api.getApiService().getFeedback(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResult<FeedbackModel>>(){

                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposables.add(d);
                    }

                    @Override
                    public void onNext(BaseResult<FeedbackModel> feedbackModelBaseResult) {
                        if (!feedbackModelBaseResult.isLoginTimeOut()) {//没有超时
                            if (feedbackModelBaseResult.getResultSuccess()) {

                                ToastUtil.onDisplayToast(FeedbackActivity.this, "反馈成功，多谢您的支持!");
                                finish();

                            } else {
                                ToastUtil.onDisplayToast(FeedbackActivity.this, feedbackModelBaseResult.getResult().getMessage());
                            }
                        } else {
                            String tipContent = feedbackModelBaseResult.getResult().getMessage();
                            //br.openTimeOutActivity(FeedbackActivity.this, tipContent);
                            ToastUtil.onDisplayToast(FeedbackActivity.this, tipContent);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.onDisplayToast(FeedbackActivity.this, e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
