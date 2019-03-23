package com.xinfu.qianxiaozhuang.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.xinfu.qianxiaozhuang.R;


/**
 * 通用的对话框
 * Created by Administrator on 2016/3/15.
 */
public class UniversalDialog extends DialogFragment implements View.OnClickListener {


    private TextView txt_title;//标题
    private TextView txt_content;//内容
    private TextView txt_cancel;//取消
    private TextView txt_sure;//确定
    private View view_middle;
    boolean isVisibleTitle = true;//是否显示标题
    private IHitSureCallBack iHitSureCallBack;
    private IHitCancelBack iHitCancelBack;
    private String content;
    private String rightValue;
    private String leftValue;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.universal_dialog_layout, null);
        initUI(view);
        //对话框样式
        Dialog dialog = new Dialog(getActivity(), R.style.ShareDialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
//        Window window = dialog.getWindow();
//        window.getDecorView().setPadding(MetricTransformationUtil.dip2px(getContext(), getResources().getDimension(R.dimen.dp_10)), 0, MetricTransformationUtil.dip2px(getContext(), getResources().getDimension(R.dimen.dp_10)), 0);
//        WindowManager.LayoutParams params = window.getAttributes();
//        params.width = WindowManager.LayoutParams.MATCH_PARENT;
////        params.height = (int) (MyApplication.getInstance().getScreenWidth() * 0.8);
//        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        params.gravity = Gravity.CENTER;
//        window.setAttributes(params);
        Window window = dialog.getWindow();
        //window.getDecorView().setPadding(30, 50, 30, 100);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        dialog.setContentView(view);
        return dialog;
    }

    private void initUI(View view) {

        txt_title = view.findViewById(R.id.txt_title);
        if (isVisibleTitle) {
            txt_title.setVisibility(View.VISIBLE);
        } else {
            txt_title.setVisibility(View.GONE);
        }
        txt_content = view.findViewById(R.id.txt_content);
        if (!TextUtils.isEmpty(content)) {
            txt_content.setText(content);
        }
        txt_sure = view.findViewById(R.id.txt_sure);
        txt_sure.setOnClickListener(this);
        if (!TextUtils.isEmpty(rightValue)) {
            txt_sure.setText(rightValue);
        }
        txt_cancel = view.findViewById(R.id.txt_cancel);
        view_middle = view.findViewById(R.id.view_middle);
        if (!TextUtils.isEmpty(leftValue)) {
            txt_cancel.setOnClickListener(this);
            txt_cancel.setText(leftValue);
            displayView(txt_cancel, View.VISIBLE);
            displayView(view_middle, View.VISIBLE);
        } else {
            displayView(txt_cancel, View.GONE);
            displayView(view_middle, View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_sure:
                if (iHitSureCallBack != null) {
                    iHitSureCallBack.onHitSureCallBack();
                }
                dismiss();
                break;
            case R.id.txt_cancel:
                if (iHitCancelBack != null) {
                    iHitCancelBack.onHitCancelCallBack();
                }
                dismiss();
                break;
        }

    }


    /**
     * @param content          提示内容
     * @param rightValue       右边内容
     * @param iHitSureCallBack 确定监听
     */
    public void setInsertData(String content, String leftValue, String rightValue, IHitCancelBack iHitCancelBack, IHitSureCallBack iHitSureCallBack) {

        setInsertData(content, leftValue, rightValue, iHitCancelBack, iHitSureCallBack, false);

    }

    /**
     * @param content          提示内容
     * @param leftValue        左边内容
     * @param rightValue       右边内同
     * @param iHitCancelBack   取消监听
     * @param iHitSureCallBack 确定监听
     * @param isVisibleTitle   标题是否隐藏
     */
    public void setInsertData(String content, String leftValue, String rightValue, IHitCancelBack iHitCancelBack, IHitSureCallBack iHitSureCallBack, boolean isVisibleTitle) {

        this.content = content;
        this.leftValue = leftValue;
        this.rightValue = rightValue;
        this.iHitCancelBack = iHitCancelBack;
        this.iHitSureCallBack = iHitSureCallBack;
        this.isVisibleTitle = isVisibleTitle;

    }


    /**
     * 点击确定按钮的回调
     */
    public interface IHitSureCallBack {

        public void onHitSureCallBack();

    }


    /**
     * 点击取消按钮
     */
    public interface IHitCancelBack {

        public void onHitCancelCallBack();

    }

    /**
     * 是否隐藏控件
     *
     * @param view
     * @param visible
     */
    private void displayView(View view, int visible) {

        if (view.getVisibility() != visible) {
            view.setVisibility(visible);
        }

    }
}