package com.xinfu.qianxiaozhuang.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinfu.qianxiaozhuang.R;
import com.xinfu.qianxiaozhuang.activity.MainActivity;
import com.xinfu.qianxiaozhuang.utils.ImageUtil;
import com.xinfu.qianxiaozhuang.utils.ResourcesUtils;

public class NavBar extends RelativeLayout {
    private View mNav1;
    private ImageView mNav1Icon;
    private TextView mNav1Label;

    private View mNav2;
    private ImageView mNav2Icon;
    private TextView mNav2Label;

    private View mNav3;
    private ImageView mNav3Icon;
    private TextView mNav3Label;

    private View mNav4;
    private ImageView mNav4Icon;
    private TextView mNav4Label;

    private ImageView mPingduoduoIcon;
    private TextView mPingduoduoLabel;
    private ImageView mProfileIcon;
    private TextView mProfileLabel;
    private ImageView mBaby;
    private View mBabyMenuBackdrop;
    private View mBabyMenuButtons;

    private int mLastActive;
    private TextView pregnancyCheckButton;
    private ImageView mJingdongIcon;
    private TextView mJingdongLabel;

    private View mBgCenter;
    private FrameLayout mBabyLayout;
    private Context mContext;

    public NavBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.common_nav_bar, this, true);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mNav1Icon.setImageDrawable(null);
        mNav2Icon.setImageDrawable(null);
//        mNav3Icon.setImageDrawable(null);
//        mNav4Icon.setImageDrawable(null);

//        mPingduoduoIcon.setImageDrawable(null);
//        mProfileIcon.setImageDrawable(null);
//        mBaby.setImageDrawable(null);
//        mJingdongIcon.setImageDrawable(null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (isInEditMode()) {
            return;
        }
        mNav1 = findViewById(R.id.nav1);
        mNav1Icon = (ImageView) findViewById(R.id.nav1_icon);
        mNav1Label = (TextView) findViewById(R.id.nav1_label);
        mNav1Label.setText("借款");

        mNav2 = findViewById(R.id.nav2);
        mNav2Icon = (ImageView) findViewById(R.id.nav2_icon);
        mNav2Label = (TextView) findViewById(R.id.nav2_label);
        mNav2Label.setText("我的");

//        mNav3 = findViewById(R.id.nav3);
//        mNav3Icon = (ImageView) findViewById(R.id.nav3_icon);
//        mNav3Label = (TextView) findViewById(R.id.nav3_label);
//        mNav3Label.setText("买家任务");
//
//        mNav4 = findViewById(R.id.nav4);
//        mNav4Icon = (ImageView) findViewById(R.id.nav4_icon);
//        mNav4Label = (TextView) findViewById(R.id.nav4_label);
//        mNav4Label.setText("我的");

        //MyTask
//        View mJingdong=findViewById(R.id.nav_jingdong);
//        mJingdongIcon = (ImageView) findViewById(R.id.nav_jingdong_icon);
//        mJingdongLabel = (TextView) findViewById(R.id.nav_jingdong_label);
//        mBaby = (ImageView) findViewById(R.id.nav_baby);
//        mBabyLayout=(FrameLayout)findViewById(R.id.nav_baby_layout);
//        mBgCenter=(View)findViewById(R.id.mBgCenter);
//        View mPingduoduo = findViewById(R.id.nav_pingduoduo);
//        mPingduoduoIcon = (ImageView) findViewById(R.id.pingduoduo_icon);
//        mPingduoduoLabel = (TextView) findViewById(R.id.pingduoduo_label);
//        View mProfile = findViewById(R.id.nav_profile);
//        mProfileIcon = (ImageView) findViewById(R.id.profile_icon);
//        mProfileLabel = (TextView) findViewById(R.id.profile_label);
        // init views
        final Context context = getContext();
        mContext = context;
        if (context instanceof MainActivity) {
            final MainActivity mainActivity = (MainActivity) context;
            // init top buttons in baby button pop layout


            mNav1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    setActive(1);
                }
            });


            mNav2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((MainActivity) mContext).isLoginStatus()) {
                        setActive(2);
                    }
                }
            });

//            mNav3.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    setActive(3);
//
//                }
//            });
//
//            mNav4.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    setActive(4);
//                }
//            });
        }
    }


    public void setActive(int number) {
        int defaultColor = R.color.color_ff999999;
        int choseColor = R.color.color_ff4d4d4d;
        mNav2Icon.setSelected(false);
        mNav1Icon.setSelected(false);
//        clearColorFilter(mNav1Icon);
//        clearColorFilter(mNav2Icon);
//        clearColorFilter(mNav3Icon);
//        clearColorFilter(mNav4Icon);
//        mNav1Icon.setImageResource(R.drawable.ic_page_loan_selector);
//        changeColorFilter(mNav1Icon,defaultColor);
//        mNav2Icon.setImageResource(R.drawable.ic_page_mine_selector);
//        changeColorFilter(mNav2Icon,defaultColor);
//        mNav3Icon.setImageResource(R.drawable.ic_assignment_black_48dp);
//        changeColorFilter(mNav3Icon,defaultColor);
//        mNav4Icon.setImageResource(R.drawable.ic_person_black_36dp);
//        changeColorFilter(mNav4Icon,defaultColor);

        mNav1Label.setTextColor(ResourcesUtils.Companion.getColor(defaultColor));
        mNav2Label.setTextColor(ResourcesUtils.Companion.getColor(defaultColor));
//        mNav3Label.setTextColor(ResourcesUtils.Companion.getColor(defaultColor));
//        mNav4Label.setTextColor(ResourcesUtils.Companion.getColor(defaultColor));
        ((MainActivity) mContext).switchFragment(number);
        switch (number) {
            case 1:
                //mNav1Icon.setImageResource(R.drawable.button_dashboard_select);
                mNav1Label.setTextColor(ResourcesUtils.Companion.getColor(choseColor));
                mLastActive = number;
                mNav1Icon.setSelected(true);
                //clearColorFilter(mNav1Icon);
                //changeColorFilter(mNav1Icon,choseColor);
                break;
            case 2:
                //mNav2Icon.setImageResource(R.drawable.button_community_select);
                mNav2Label.setTextColor(ResourcesUtils.Companion.getColor(choseColor));
                mNav2Icon.setSelected(true);
                //clearColorFilter(mNav2Icon);
                //changeColorFilter(mNav2Icon,choseColor);
                mLastActive = number;
                break;
//            case 3:
//                //mNav3Icon.setImageResource(R.drawable.icon_shangcheng1);
//                mNav3Label.setTextColor(getDefault(getContext()));
//                mLastActive = number;
//                clearColorFilter(mNav3Icon);
//                changeColorFilter(mNav3Icon,choseColor);
//                break;
//            case 4:
//                //mNav4Icon.setImageResource(R.drawable.button_profile_select);
//                mNav4Label.setTextColor(getDefault(getContext()));
//                mLastActive = number;
//                clearColorFilter(mNav4Icon);
//                changeColorFilter(mNav4Icon,choseColor);
//                break;

            default:
                break;
        }
    }

    public int getActive() {
        return mLastActive;
    }


    private void changeColorFilter(ImageView imageView, @ColorRes int tintColor) {
        Drawable drawable = imageView.getDrawable();
        int _tintColor = ContextCompat.getColor(imageView.getContext(), tintColor);
        if (drawable != null) {
            imageView.setColorFilter(_tintColor);
            imageView.setImageDrawable(ImageUtil.tintDrawable(drawable, _tintColor));
        }
    }

    private void clearColorFilter(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        if (drawable != null) {
            imageView.clearColorFilter();
            clearTint(imageView);
        }
    }

    private void clearTint(ImageView imageView) {
        if (imageView.getDrawable() != null) {
            // Seems like Android support library has bug here, so do a specially operation here.
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                imageView.setImageTintList(null);
            } else {
                imageView.setImageDrawable(ImageUtil.clearTintDrawable(imageView.getDrawable()));
            }
        }
    }
}
