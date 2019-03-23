package com.xiang.one.utils.dialog

import android.app.Dialog
import android.content.Context
import android.opengl.Visibility
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.xinfu.qianxiaozhuang.R
import kotlinx.android.synthetic.main.dialog_alert_common_new.*
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.textColor


class AlertDialogNew : Dialog {
    constructor(context: Context) : this(context,R.style.Dialog_Fullscreen)

    constructor(context: Context, themeResId: Int) : super(context, themeResId){
        init()
    }

    private fun init() {
        setContentView(R.layout.dialog_alert_common_new)
        initBottom()
        mBtnLeft.onClick { dismiss() }
        mBtnRight.onClick { dismiss() }
    }
    private fun initBottom() {
        val dialogWindow = this.window
        val lp = dialogWindow!!.attributes
        // 设置显示动画
        window.setWindowAnimations(R.style.PickerAnimStyle)
        dialogWindow.setGravity(Gravity.CENTER)
        dialogWindow.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        //lp.alpha = 0.7f; // 透明度
        dialogWindow.attributes = lp
    }

    fun setTitle(str1:String,str2:String,str3:String):AlertDialogNew{
        mTitle1.text=str1
        if(!str2.isNullOrBlank()){
            mTitle2.text=str2
            mTitle2.visibility=View.VISIBLE
        }else{
            mTitle2.visibility=View.GONE
        }
        if(!str3.isNullOrBlank()) {
            mTitle3.visibility=View.VISIBLE
            mTitle3.text = str3
        }else{
            mTitle3.visibility=View.GONE
        }
        return this
    }

    fun setHeader(str1:String):AlertDialogNew{
        if(str1.isNullOrBlank()){
            headerLayout.visibility=View.INVISIBLE
        }else{
            headerLayout.visibility=View.VISIBLE
        }
        mHeader.text = str1
        return this
    }

    fun setLeftClickListener(clickListener:(alertDialog:AlertDialogNew,button:View)->Unit):AlertDialogNew{
        bottomClick.visibility=View.VISIBLE
        //mDialogContainer.backgroundResource=R.drawable.drawable_concer_white
        mBtnLeft.onClick {
            clickListener.invoke(this@AlertDialogNew,mBtnLeft)
        }
        return this
    }
    fun setRightClickListener(clickListener:(alertDialog:AlertDialogNew,button:View)->Unit ):AlertDialogNew{
        bottomClick.visibility=View.VISIBLE
        //mDialogContainer.backgroundResource=R.drawable.drawable_concer_white
        mBtnRight.onClick {
            clickListener.invoke(this@AlertDialogNew,mBtnRight)
        }
        return this
    }

    fun setBtnLeftTxt(str:String):AlertDialogNew{
        //mDialogContainer.backgroundResource=R.drawable.drawable_concer_white
        bottomClick.visibility=View.VISIBLE
        mBtnLeft.visibility=View.VISIBLE
        mBtnLeft.text=str
        return this
    }

    fun setBtnRightTxt(str:String):AlertDialogNew{
        //mDialogContainer.backgroundResource=R.drawable.drawable_concer_white
        bottomClick.visibility=View.VISIBLE
        mBtnRight.visibility=View.VISIBLE
        mBtnRight.text=str
        return this
    }
    fun setBtnRightTxtColor(color:Int):AlertDialogNew{
        mBtnRight.textColor=color
        return this
    }
    fun setBtnLeftTxtColor(color:Int):AlertDialogNew{
        mBtnLeft.textColor=color
        return this
    }

    fun setTitle2Color(color:Int):AlertDialogNew{
        mTitle2.textColor=color
        return this
    }
}
