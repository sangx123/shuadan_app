package com.xinfu.qianxiaozhuang.activity.login;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.orhanobut.hawk.Hawk;
import com.xinfu.qianxiaozhuang.App;
import com.xinfu.qianxiaozhuang.R;
import com.xinfu.qianxiaozhuang.SpConfig;
import com.xinfu.qianxiaozhuang.activity.BaseActivity;
import com.xinfu.qianxiaozhuang.activity.MainActivity;
import com.xinfu.qianxiaozhuang.adapter.GuideViewPagerAdapter;
import com.xinfu.qianxiaozhuang.utils.MetricTransformationUtil;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 引导页
 */
public class GuideActivity extends BaseActivity implements View.OnClickListener {

    private List<View> views;
    private ViewPager myViewPager;
    private LinearLayout ll_indicator;
    private Button btnNext;
    private GuideViewPagerAdapter adapter;
    private int[] pics = {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};
    private ImageView[] imageViews;
    private int mCurrentItem = 0;//当前Item的索引

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guide);
        initUI();
        initViews();
    }

    private void initUI() {

        myViewPager = (ViewPager) findViewById(R.id.myViewPager);
        myViewPager.setOnPageChangeListener(new MyChageListener());
        btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(GuideActivity.this);
        ll_indicator = (LinearLayout) findViewById(R.id.ll_indicator);

    }

    private void initViews() {

        LayoutInflater inflater = LayoutInflater.from(GuideActivity.this);
        views = new ArrayList<View>(3);
        for (int i = 0; i < pics.length; i++) {
            View view = inflater.inflate(R.layout.item_pager_image, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.image);
//          imageView.setImageResource(pics[i]);//会OOM
//　当使用 imageView.setBackgroundResource，imageView.setImageResource, 或者 BitmapFactory.decodeResource 这样的方法来设置一张大图片时，
// 这些函数在完成decode后，最终都是通过Java层的createBitmap来完成的，需要消耗更多内存
            Bitmap bitmap = readBitMap(GuideActivity.this, pics[i]);
            imageView.setImageBitmap(bitmap);
            views.add(view);
        }
        adapter = new GuideViewPagerAdapter(GuideActivity.this, views);
        myViewPager.setAdapter(adapter);
        setBannerImageIndactor(pics.length);
    }

    //以最省内存的方式读取本地资源的图片
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;//内存空间不足的时候，允许所创建的bitmap被回收。
        options.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, options);
    }

    @NotNull
    @Override
    public String getLoggerTag() {
        return null;
    }

    private class MyChageListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            mCurrentItem = position;
            for (int i = 0; i < imageViews.length; i++) {

                if (mCurrentItem == i) {

                    imageViews[mCurrentItem].setImageResource(R.drawable.drawable_dot_cycle_blue);

                } else {

                    imageViews[i].setImageResource(R.drawable.drawable_dot_cycle_white);

                }
            }

            if (position == imageViews.length - 1) {
                if (btnNext.getVisibility() != View.VISIBLE) {
                    btnNext.setVisibility(View.VISIBLE);
                }
            } else {
                if (btnNext.getVisibility() != View.GONE) {
                    btnNext.setVisibility(View.GONE);
                }
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

    }

    /**
     * 设置轮播图引导
     */
    private void setBannerImageIndactor(int imageAmount) {

        mCurrentItem = 0;
        ll_indicator.removeAllViews();
        imageViews = new ImageView[imageAmount];
        for (int i = 0; i < imageAmount; i++) {

            imageViews[i] = new ImageView(GuideActivity.this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(MetricTransformationUtil.dip2px(GuideActivity.this, 10), 0, MetricTransformationUtil.dip2px(GuideActivity.this, 10), 0);
            imageViews[i].setLayoutParams(layoutParams);
            imageViews[i].setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (i == 0) {
                imageViews[i].setImageResource(R.drawable.drawable_dot_cycle_blue);
            } else {
                imageViews[i].setImageResource(R.drawable.drawable_dot_cycle_white);
            }
            ll_indicator.addView(imageViews[i], i);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnNext:
                Hawk.put(SpConfig.GUIDE_STATUS,true);
                startActivity(new Intent(GuideActivity.this, MainActivity.class).putExtra("pressIndex", 0));//跳转到首页
                finish();
                break;
        }
    }
}

