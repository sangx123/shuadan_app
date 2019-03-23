package com.xinfu.qianxiaozhuang.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.xinfu.qianxiaozhuang.R;


/**
 * 完善对话框
 */
public class PerfectDialog extends DialogFragment implements View.OnClickListener {


    private TextView txt_sure;
    private IPerfectDialogCall iPerfectDialogCall;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_perfect, null);
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

        txt_sure = view.findViewById(R.id.txt_sure);
        txt_sure.setOnClickListener(this);

    }

    public void setOnSureCallBackListener(IPerfectDialogCall iPerfectDialogCall) {

        this.iPerfectDialogCall = iPerfectDialogCall;

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.txt_sure:
                if (iPerfectDialogCall != null) {
                    iPerfectDialogCall.onSureCallBack();
                }
                dismissAllowingStateLoss();
                break;
        }
    }

    public interface IPerfectDialogCall {

        public void onSureCallBack();

    }

}
