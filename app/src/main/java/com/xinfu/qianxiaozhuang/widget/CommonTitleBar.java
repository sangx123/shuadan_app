package com.xinfu.qianxiaozhuang.widget;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinfu.qianxiaozhuang.R;


/**
 * Created by fanwuye on 2016/1/19.
 * @author
 */
public class CommonTitleBar extends LinearLayout implements OnClickListener {

    private TextView txt_back;
    private TextView txt_title;
    private TextView txt_more;
    private RelativeLayout layout_back;
    private LinearLayout layout_title;
    private LinearLayout layout_more;
    private RelativeLayout layout;
    private View view;
    private String title;
    private Context context;
    private Animation rotatAnimation;//旋转动画
    private IClickTxtBack iClickTxtBack;
    private IClickTxtMore iClickTxtMore;
    private IClickMiddleTxt iClickMiddleTxt;//中间按钮监听

    private TextView text_back_two;

    @SuppressLint("Recycle")
    public CommonTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        title = mTypedArray.getString(R.styleable.TitleBar_titleContent);
        //this.setBackgroundColor(getResources().getColor(R.color.transparent));
        initView();
    }

    public CommonTitleBar(Context context) {
        this(context, null);
    }

    /**
     * 添加监听
     *
     * @param iClickTxtBack
     * @param iClickTxtMore
     */
    public void setBackWidgetOnClick(IClickTxtBack iClickTxtBack, IClickTxtMore iClickTxtMore) {

        this.iClickTxtBack = iClickTxtBack;
        this.iClickTxtMore = iClickTxtMore;

    }


    private void initView() {
        view = LayoutInflater.from(getContext()).inflate(R.layout.common_title_layout, null);
        txt_back = (TextView) view.findViewById(R.id.txt_back);
        //txt_back.setOnClickListener(this);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        //txt_title.setOnClickListener(this);
        txt_more = (TextView) view.findViewById(R.id.txt_more);
        //txt_more.setOnClickListener(this);

        text_back_two = (TextView) view.findViewById(R.id.text_back_two);

        if (!TextUtils.isEmpty(title)) {
            txt_title.setText(title);
        }

        layout_back = (RelativeLayout) view.findViewById(R.id.layout_back);
        layout_back.setOnClickListener(this);
        layout_title = (LinearLayout) view.findViewById(R.id.layout_title);
        layout_title.setOnClickListener(this);
        layout_more = (LinearLayout) view.findViewById(R.id.layout_more);
        layout_more.setOnClickListener(this);
        layout=view.findViewById(R.id.layout);

        this.addView(view);
    }

    public TextView setTxtBack() {

        return txt_back;

    }

    public TextView setTxtTitle() {

        return txt_title;

    }

    public TextView setTxtMore() {

        return txt_more;

    }

    public void setTitle(String content) {

        txt_title.setText(content);

    }

    /**
     * 设置字体
     * @param tf
     */
    public void setTitleStyle(Typeface tf){

        txt_title.setTypeface(tf);

    }

    public TextView getText_back_two() {
        return text_back_two;
    }

    public void setText_back_two(TextView text_back_two) {
        this.text_back_two = text_back_two;
    }

    public TextView getTxt_back() {
        return txt_back;
    }

    public void setTxt_back(TextView txt_back) {
        this.txt_back = txt_back;
    }

    public TextView getTxt_title() {
        return txt_title;
    }

    public void setTxt_title(TextView txt_title) {
        this.txt_title = txt_title;
    }

    public TextView getTxt_more() {
        return txt_more;
    }

    public void setTxt_more(TextView txt_more) {
        this.txt_more = txt_more;
    }

    public LinearLayout getLayout_more() {
        return layout_more;
    }

    /**
     * 返回按钮设置背景
     *
     * @param drawableId
     */
    public void setDrawableForTxtBack(int drawableId) {

        Drawable drawable = getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        txt_back.setCompoundDrawables(drawable, null, null, null);
        txt_back.setPadding(15, 0, 0, 0);

    }

    /**
     * 中间按钮背景
     *
     * @param drawableId
     */
    public void setDrawableForTxtMiddle(int drawableId) {

        Drawable drawable = getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        txt_title.setCompoundDrawables(null, null, drawable, null);
        txt_title.setPadding(0, 0, 15, 0);

    }

    /**
     * 更多按钮设置背景
     *
     * @param drawableId
     */
    public void setDrawableForTxtMore(int drawableId) {

        Drawable drawable = getResources().getDrawable(drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        txt_more.setCompoundDrawables(null, null, drawable, null);
        txt_more.setPadding(0, 0, 15, 0);

    }

    /**
     * 设置标题颜色
     * @param color
     */
    public void setTitleCustomTextColor(@ColorInt int color){

        txt_title.setTextColor(color);

    }

    /**
     * 设置背景颜色
     *
     * @param color
     */
    public void setCustomBackgroundColor(@DrawableRes int color) {

        view.setBackgroundResource(color);

    }

    /**
     * 设置标题栏右边的内容
     *
     * @param title
     */
    public void setTextForTxtMore(String title) {

        txt_more.setText(title);

    }


    /**
     * 设置标题栏左边的内容
     *
     * @param title
     */
    public void setTextForTxtBack(String title) {

        text_back_two.setText(title);

    }

    /**
     * 显示/隐藏返回控件
     *
     * @param visibility
     */
    public void setTxtBackVisibility(int visibility) {

        txt_back.setVisibility(visibility);

    }

    /**
     * 设置中间按钮的监听
     */
    public void setOnClickMiddleTxt(IClickMiddleTxt iClickMiddleTxt) {
        this.iClickMiddleTxt = iClickMiddleTxt;
    }

    public void setLayoutBackground(@ColorInt int color){

        this.layout.setBackgroundColor(color);

    }

    /**
     * 显示/隐藏返更多控件
     *
     * @param visibility
     */
    public void setTxtMoreVisibility(int visibility) {
        txt_more.setVisibility(visibility);
    }

    /**
     * 显示/隐藏返回文字显示的控件
     *
     * @param visibility
     */
    public void setTxtBackTwoVisibility(int visibility) {
        text_back_two.setVisibility(visibility);
    }

    /**
     * 获取更多按钮的显示状态
     */
    public int getTxtMoreVisibility() {
        return txt_more.getVisibility();
    }


//	/**
//	 * 给某个控件增加动画特效,现在默认是给更多按钮增加特效
//	 */
//	public void setRotateAnimation(){
//
//		if(rotatAnimation==null){
//
//			rotatAnimation=AnimationUtil.rotatAnimation(context);
//
//		}
//		txt_more.startAnimation(rotatAnimation);
//
//	}
//	/**
//	 * 停止旋转按钮
//	 */
//	public void setStopRotateAnimation(){
//
//		if (rotatAnimation != null && rotatAnimation.hasStarted()) {
//			txt_more.clearAnimation();
//	    }
//
//	}

    /**
     * 点击返回按钮的接口
     *
     * @author fanwuye
     */
    public interface IClickTxtBack {

        public void onClickTxtBackCallBack();

    }

    /**
     * 点击中间按钮的接口
     */
    public interface IClickMiddleTxt {

        public void onClickMiddleTxt();

    }

    /**
     * 点击更多按钮的接口
     *
     * @author fanwuye
     */
    public interface IClickTxtMore {

        public void onClickTxtMoreCallBack();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
//            case R.id.layout_back:
//                iClickTxtBack.onClickTxtBackCallBack();
//                break;
//            case R.id.layout_title:
//                if (iClickMiddleTxt != null) {
//                    iClickMiddleTxt.onClickMiddleTxt();
//                }
//                break;
//            case R.id.layout_more:
//                iClickTxtMore.onClickTxtMoreCallBack();
//                break;

            case R.id.layout_back:
                if(iClickTxtBack!=null){
                    iClickTxtBack.onClickTxtBackCallBack();
                }
                break;
            case R.id.layout_title:
                if (iClickMiddleTxt != null) {
                    iClickMiddleTxt.onClickMiddleTxt();
                }
                break;
            case R.id.layout_more:
                if(iClickTxtMore!=null){
                    iClickTxtMore.onClickTxtMoreCallBack();
                }
                break;
        }
    }
}


