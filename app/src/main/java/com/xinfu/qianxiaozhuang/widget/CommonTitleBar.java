package com.xinfu.qianxiaozhuang.widget;


import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinfu.qianxiaozhuang.R;


/**
 * Created by fanwuye on 2016/1/19.
 * @author
 */
public class CommonTitleBar extends LinearLayout implements OnClickListener {
    private TextView txt_title;
    private RelativeLayout layout_back;
    private LinearLayout layout_title;
    private RelativeLayout layout;
    private View view;
    private String title;
    private Context context;
    private Animation rotatAnimation;//旋转动画
    private IClickTxtBack iClickTxtBack;
    private IClickTxtMore iClickTxtMore;
    private IClickMiddleTxt iClickMiddleTxt;//中间按钮监听

    //新
    private String mTitle="";
    private int INVALIDATE_RESOURCE=-1;
    private int mBgRes=0;
    private int mLeftIconRes=0;
    private int mRightIconRes=0;
    private int mRightIconTwoRes=0;
    private ImageView mIvTitleLeft;
    private ImageView mIvTitleRight;
    private ImageView mIvTitleRightSecond;
    private TextView mTvRight;
    private String mRightText="";
    private String mLeftText="";
    private int mRightTextColor=0;
    private  int mTitleColor=0;

    @SuppressLint("Recycle")
    public CommonTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.MTitleBar);
        //this.setBackgroundColor(getResources().getColor(R.color.transparent));
        initView();
        for (int i=0;i< attrs.getAttributeCount()-1;i++) {
            int attr = mTypedArray.getIndex(i);
            switch (attr) {
                case R.styleable.MTitleBar_title :
                    mTitle = mTypedArray.getString(attr);
                    if (!TextUtils.isEmpty(mTitle)) {
                        setTitle(mTitle);
                    }
                    break;
                case R.styleable.MTitleBar_background_color:
                    mBgRes = mTypedArray.getResourceId(attr, INVALIDATE_RESOURCE);
                    if (mBgRes != INVALIDATE_RESOURCE) {
                        setToolBarBackground(mBgRes);
                    }
                     break;
                case R.styleable.MTitleBar_left_icon :
                    mLeftIconRes = mTypedArray.getResourceId(attr, INVALIDATE_RESOURCE);
                    if (mLeftIconRes != INVALIDATE_RESOURCE) {
                        setLeftIcon(mLeftIconRes);
                    }
                    break;
                case R.styleable.MTitleBar_right_icon :
                    mRightIconRes = mTypedArray.getResourceId(attr, INVALIDATE_RESOURCE);
                    if (mRightIconRes != INVALIDATE_RESOURCE) {
                        setRightIcon(mRightIconRes);
                    }
                    break;
                case R.styleable.MTitleBar_right_icon_two :
                    mRightIconTwoRes = mTypedArray.getResourceId(attr, INVALIDATE_RESOURCE);
                    if (mRightIconTwoRes != INVALIDATE_RESOURCE) {
                        setRightTwoIcon(mRightIconTwoRes);
                    }
                    break;
                case R.styleable.MTitleBar_right_text :
                    mRightText = mTypedArray.getString(attr);
                    if (!TextUtils.isEmpty(mRightText)) {
                        setRightText(mRightText);
                    }
                    break;
                case R.styleable.MTitleBar_right_text_color :
                    mRightTextColor = mTypedArray.getColor(attr, getResources().getColor(R.color.colorAccent));
                    setRightTextColor(mRightTextColor);
                    break;
                case R.styleable.MTitleBar_title_color :
                    mTitleColor = mTypedArray.getColor(attr, getResources().getColor(R.color.color_333333));
                    setTitleColor(mTitleColor);
                    break;
            }

        }
        mTypedArray.recycle();

    }

    public CommonTitleBar(Context context) {
        this(context, null);
    }

    public void setLeftIcon(int mLeftIconRes) {
        mIvTitleLeft.setVisibility(View.VISIBLE);
        mIvTitleLeft.setImageResource(mLeftIconRes);
    }
    public void setRightIcon(int mRightIconRes) {
        mIvTitleRight.setVisibility(View.VISIBLE);
        mTvRight.setVisibility(View.GONE);
        mIvTitleRight.setImageResource(mRightIconRes);
    }

    public void setRightTwoIcon(int mRightIconTwoRes) {
        //        mTvRight.setVisibility(GONE);
        mIvTitleRightSecond.setVisibility(View.VISIBLE);
        mIvTitleRightSecond.setImageResource(mRightIconTwoRes);
    }
    public void setRightText(String rightText) {
        mIvTitleRight.setVisibility(View.GONE);
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setText(rightText);
    }
    public void setRightTextColor(int rightTextColor) {
        mTvRight.setTextColor(rightTextColor);
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
    void setToolBarBackground(int toolBarBackground) {
        layout.setBackgroundResource(toolBarBackground);
    }

    private void initView() {
        view = LayoutInflater.from(getContext()).inflate(R.layout.common_title_layout, this, true);
        mIvTitleLeft=(ImageView)view.findViewById(R.id.mIvTitleLeft);
        mIvTitleRight=(ImageView)view.findViewById(R.id.mIvTitleRight);
        mIvTitleRightSecond=(ImageView)view.findViewById(R.id.mIvTitleRightSecond);
        mTvRight=(TextView)view.findViewById(R.id.mTvRight);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        mIvTitleLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)context).onBackPressed();
            }
        });
        layout_back = (RelativeLayout) view.findViewById(R.id.layout_back);
        layout_back.setOnClickListener(this);
        layout_title = (LinearLayout) view.findViewById(R.id.layout_title);
        layout_title.setOnClickListener(this);
        layout=view.findViewById(R.id.layout);

        //this.addView(view);
    }

    public void setTitle(String content) {
        txt_title.setText(content);
    }

    public void setTitleColor(int mTitleColor) {
        txt_title.setTextColor(mTitleColor);
    }


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
        }
    }
}


