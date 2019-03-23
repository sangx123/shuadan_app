package com.xinfu.qianxiaozhuang.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


/**
 * 开始页引导适配
 * Created by FanBei on 2016/4/25.
 */
public class GuideViewPagerAdapter extends PagerAdapter {

    private List<View> views;
    private Context context;


    public GuideViewPagerAdapter(Context context, List<View> views){

        this.context=context;
        this.views=views;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView(views.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager)container).addView(views.get(position));
        return views.get(position);
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==object);
    }
}
