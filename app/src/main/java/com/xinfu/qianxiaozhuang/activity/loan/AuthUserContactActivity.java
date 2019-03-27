package com.xinfu.qianxiaozhuang.activity.loan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.xiang.one.network.error.RxUtils;
import com.xinfu.qianxiaozhuang.R;
import com.xinfu.qianxiaozhuang.activity.BaseActivity;
import com.xinfu.qianxiaozhuang.api.Api;
import com.xinfu.qianxiaozhuang.api.BaseResult;
import com.xinfu.qianxiaozhuang.api.model.EmergencyContactModel;
import com.xinfu.qianxiaozhuang.api.model.params.BaseParam;
import com.xinfu.qianxiaozhuang.api.model.params.MemberContactParam;
import com.xinfu.qianxiaozhuang.utils.ToastUtil;
import com.xinfu.qianxiaozhuang.widget.CommonTitleBar;
import com.xinfu.qianxiaozhuang.widget.EditTextWithClear;
import org.jetbrains.annotations.NotNull;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 紧急联系人
 */
public class AuthUserContactActivity extends BaseActivity implements View.OnClickListener, CommonTitleBar.IClickTxtBack {


    private CommonTitleBar commonTitleBar;
    private TextView txt_relatives;
    private EditTextWithClear et_relatives_mobile;
    private TextView txt_social_relations;
    private TextView txt_my_relationship_two;
    private EditTextWithClear et_other_mobile;
    private List<String> relationshipArrayList;
    private List<String> socialRelationsArrayList;
    private OptionsPickerView relationshipPickerView;
    private OptionsPickerView socialRelationsPickerView;
    private TextView txt_sure;
    private EditText name1;//亲人联系姓名

    private String contactRel;//关系
    private String contactName;//关系
    private String otherName;//关系

    private String contactPhone;//联系电话
    private EditText name2;//其他联系人姓名
    private String otherRel;//关系
    private String otherPhone;//联系电话
    private int NEED_REFRESH = 2;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_user_contact);
        initUI();
        initData();
        getOriginalData();
        getMemberContact();
        relationshipPickerView = initOptionPicker(txt_relatives, relationshipArrayList);
        socialRelationsPickerView = initOptionPicker(txt_social_relations, socialRelationsArrayList);
    }

    private void initUI() {

//        commonTitleBar = findViewById(R.id.titlebar_withdraw);
//        commonTitleBar.setTitle(getResources().getString(R.string.emergency_contact));
//        commonTitleBar.setTitleStyle(Typeface.DEFAULT_BOLD);
//        commonTitleBar.setTxtBackVisibility(View.VISIBLE);
//        commonTitleBar.setTitleCustomTextColor(getResources().getColor(R.color.black));
//        commonTitleBar.setDrawableForTxtBack(R.drawable.icon_back);
        commonTitleBar.setBackWidgetOnClick(AuthUserContactActivity.this, null);
        name1=(EditText) findViewById(R.id.name1);
        name2=(EditText) findViewById(R.id.name2);
        txt_relatives = findViewById(R.id.txt_my_relationship_one);
        txt_relatives.setOnClickListener(this);
        txt_social_relations = findViewById(R.id.txt_social_relations);
        txt_my_relationship_two = findViewById(R.id.txt_my_relationship_two);
        txt_my_relationship_two.setOnClickListener(this);
        et_relatives_mobile = findViewById(R.id.et_relatives_mobile);
        et_other_mobile = findViewById(R.id.et_other_mobile);
        txt_sure = findViewById(R.id.txt_sure);
        txt_sure.setOnClickListener(this);

    }

    private void initData() {
        relationshipArrayList = Arrays.asList(getResources().getStringArray(R.array.item_kinsfolk_relation));
        socialRelationsArrayList = Arrays.asList(getResources().getStringArray(R.array.item_social_relation));
    }

    private OptionsPickerView initOptionPicker(final TextView textView, final List<String> list) {//条件选择器初始化
        OptionsPickerView pickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {

                String content = list.get(options1);
                textView.setText(content);
            }
        }).setContentTextSize(20) //设置滚轮文字大小
                .build();
        pickerView.setPicker(list);
        return pickerView;
    }

    /**
     * 获取原始数据
     */
    private void getOriginalData() {

        EmergencyContactModel emergencyContactModel = (EmergencyContactModel) getIntent().getSerializableExtra("emergencyContactModel");
        if (emergencyContactModel != null) {

            String contactRel = emergencyContactModel.getContactRel();//关系
            txt_relatives.setText(contactRel);
            String contactPhone = emergencyContactModel.getContactPhone();//联系电话
            et_relatives_mobile.setText(contactPhone);
            String otherRel = emergencyContactModel.getOtherRel();//关系
            txt_social_relations.setText(otherRel);
            String otherPhone = emergencyContactModel.getOtherPhone();//联系电话
            et_other_mobile.setText(otherPhone);

        }
    }
    /**
     * 查询联系人
     */

    void getMemberContact(){
        BaseParam model=new BaseParam();
        showApiProgress();
        Api.getApiService().getMemberContact(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResult<EmergencyContactModel>>(){

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResult<EmergencyContactModel> result) {
                        txt_relatives.setText(result.getResult().getContactRel());
                        name1.setText(result.getResult().getContactName());
                        et_relatives_mobile.setText(result.getResult().getContactPhone());

                        txt_social_relations.setText(result.getResult().getOtherRel());
                        name2.setText(result.getResult().getOtherName());
                        et_other_mobile.setText(result.getResult().getOtherPhone());

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                            hideApiProgress();
                    }
                });
    }


    /**
     * 保存联系人
     */

    void saveMemberContact(){
        MemberContactParam model= new MemberContactParam();
        model.setContactName(contactName);
        model.setContactRel(contactRel);
        model.setContactPhone(contactPhone);
        model.setOtherName(otherName);
        model.setOtherRel(otherRel);
        model.setOtherPhone(otherPhone);
        Api.getApiService().saveMemberContact(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResult<EmergencyContactModel>>(){

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResult<EmergencyContactModel> result) {
                        ToastUtil.onDisplayToast(AuthUserContactActivity.this,"保存成功");
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.txt_my_relationship_one:
                hintKeyBoard();
                relationshipPickerView.show();
                break;
            case R.id.txt_my_relationship_two:
                hintKeyBoard();
                socialRelationsPickerView.show();
                break;
            case R.id.txt_sure:
                if (getWedgitData()) {
                    //onSubmitData();
                    saveMemberContact();
                }
                break;
        }
    }

    private boolean getWedgitData() {

        boolean value = true;
        contactRel = txt_relatives.getText().toString();
        if (TextUtils.isEmpty(contactRel)) {
            ToastUtil.onDisplayToast(AuthUserContactActivity.this, "请选择直系亲属联系人关系");
            value = false;
            return value;
        }
        contactName=name1.getText().toString();
        if (TextUtils.isEmpty(contactName)) {
            ToastUtil.onDisplayToast(AuthUserContactActivity.this,"请填写直系亲属联系人姓名");
            value = false;
            return value;
        }


        contactPhone = et_relatives_mobile.getText().toString();
        if (TextUtils.isEmpty(contactPhone)) {
            ToastUtil.onDisplayToast(AuthUserContactActivity.this, "请填写直系亲属联系人联系方式");
            value = false;
            return value;
        }

        otherRel = txt_social_relations.getText().toString();
        if (TextUtils.isEmpty(otherRel)) {
            ToastUtil.onDisplayToast(AuthUserContactActivity.this, "请选择其他联系人关系");
            value = false;
            return value;
        }

        otherName=name2.getText().toString();
        if (TextUtils.isEmpty(otherName)) {
            ToastUtil.onDisplayToast(AuthUserContactActivity.this,"请填写其他联系人姓名");
            value = false;
            return value;
        }

        otherPhone = et_other_mobile.getText().toString();
        if (TextUtils.isEmpty(otherPhone)) {
            ToastUtil.onDisplayToast(AuthUserContactActivity.this, "请填写其他联系人联系方式");
            value = false;
            return value;
        }

        return value;
    }

    private void onSubmitData() {

//        final BaseRequest br = new BaseRequest();
//        RequestParams params = new RequestParams(AuthUserContactActivity.this);
//        br.addCommonParams(params);
//        //params.addFormDataPart("contactName", bankAccount);
//        params.addFormDataPart("contactRel", contactRel);
//        params.addFormDataPart("contactPhone", contactPhone);
//        //params.addFormDataPart("otherName", hightDegree);
//        params.addFormDataPart("otherRel", otherRel);
//        params.addFormDataPart("otherPhone", otherPhone);
//        params.applicationJson();
//
//        HttpRequest.post(APIManager.memberContactUrl, params, new BaseHttpRequestCallback<TResultModel>() {
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
//            protected void onSuccess(TResultModel resultModel) {
//
//                if (!resultModel.isLoginTimeOut()) {//没有超时
//                    if (resultModel.getResultSuccess()) {
//
//                        ToastUtil.onDisplayToast(AuthUserContactActivity.this, getString(R.string.save_success));
//                        setResult(NEED_REFRESH);
//                        finish();
//
//                    } else {
//                        ToastUtil.onDisplayToast(AuthUserContactActivity.this, resultModel.getMessage());
//                    }
//                } else {
//                    String tipContent = resultModel.getMessage();
//                    br.openTimeOutActivity(AuthUserContactActivity.this, tipContent);
//                    ToastUtil.onDisplayToast(AuthUserContactActivity.this, tipContent);
//                }
//            }
//
//            @Override
//            public void onFailure(int errorCode, String msg) {
//                ToastUtil.onDisplayToast(AuthUserContactActivity.this, msg);
//            }
//        });

    }

    /**
     * 打开电话列表
     */
    private void takeOnContactList() {

        try {
            //String number = txt_click_call.getHint().toString();
            String number = "";
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number)); //CTION_DIAL需要调用拨号盘   ACTION_CALL就可以直接拨打
            startActivity(intent);
        } catch (Exception e) {
            ToastUtil.onDisplayToast(AuthUserContactActivity.this, getString(R.string.make_sure_the_phone_function_is_available));
        }

    }

    @Override
    public void onClickTxtBackCallBack() {
        finish();
    }

    /**
     * 隐藏键盘
     */
    public void hintKeyBoard() {
        //拿到InputMethodManager
        if (imm == null) {
            imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        //如果window上view获取焦点 && view不为空
        if (imm.isActive() && getCurrentFocus() != null) {
            //拿到view的token 不为空
            if (getCurrentFocus().getWindowToken() != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


    @NotNull
    @Override
    public String getLoggerTag() {
        return null;
    }
}
