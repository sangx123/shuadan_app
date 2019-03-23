package com.xinfu.qianxiaozhuang.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.xinfu.qianxiaozhuang.R;


/**
 * Created by FanBei on 2016/3/16.
 */
public class ApiRequestDialog implements DialogInterface.OnKeyListener {

    private Dialog dialog;
    private IListenerDialogBack iListenerDialogBack;

    public ApiRequestDialog(Context context) {
        createLoadingDialog(context);
    }

    public ApiRequestDialog(Context context, IListenerDialogBack iListenerDialogBack) {

        this(context);
        this.iListenerDialogBack = iListenerDialogBack;

    }

    /**
     * 得到自定义的progressDialog
     *
     * @param context
     * @return
     */
    private void createLoadingDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_loading, null);
        //TextView title = (TextView) view.findViewById(R.id.id_dialog_loading_msg);
        //title.setText(mMsg);
        dialog = new Dialog(context, R.style.ShareDialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.setOnKeyListener(ApiRequestDialog.this);
        dialog.setContentView(view);
        dialog.getWindow().setDimAmount(0f);
        Window window = dialog.getWindow();
//        //window.getDecorView().setPadding(30, 50, 30, 100);
        WindowManager.LayoutParams params = window.getAttributes();
//        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        params.height = WindowManager.LayoutParams.MATCH_PARENT;
//        params.gravity = Gravity.CENTER;
//        window.setAttributes(params);
//        dialog.setContentView(view);
        DisplayMetrics dm = new DisplayMetrics();
        window.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Rect rect = new Rect();
        View view1 = window.getDecorView();//decorView是window中的最顶层view，可以从window中获取到decorView
        view1.getWindowVisibleDisplayFrame(rect);
        params.height = dm.heightPixels - rect.top;
        params.width = dm.widthPixels;
    }


    public void showRequestDataDialog() {

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void dismissRequestDataDialog() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(iListenerDialogBack!=null){
                iListenerDialogBack.onListenerDialogBack();
            }
        }
        return false;
    }


    public void setIListenerDialogBack(IListenerDialogBack iListenerDialogBack){

        this.iListenerDialogBack=iListenerDialogBack;

    }

    /**
     * 监听对话框消失
     */
    public interface IListenerDialogBack {

        public void onListenerDialogBack();

    }
}
