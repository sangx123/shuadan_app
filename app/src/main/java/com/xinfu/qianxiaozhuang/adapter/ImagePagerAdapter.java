/*
 * Copyright 2014 trinea.cn All right reserved. This software is the confidential and proprietary information of
 * trinea.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with trinea.cn.
 */
package com.xinfu.qianxiaozhuang.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.xinfu.qianxiaozhuang.R;
import com.xinfu.qianxiaozhuang.activity.NoticeWebActivity;
import com.xinfu.qianxiaozhuang.api.BaseRequest;
import com.xinfu.qianxiaozhuang.api.model.HomeModel;
import com.xinfu.qianxiaozhuang.utils.GlideUtil;
import java.util.List;
import me.crosswall.lib.coverflow.jakewharton.RecyclingPagerAdapter;
import me.crosswall.lib.coverflow.util.ListUtils;


/**
 * ImagePagerAdapter
 *
 * @author
 */
public class ImagePagerAdapter extends RecyclingPagerAdapter {

    private Context context;
    private List<HomeModel.NoticeItemlModel> imageIdList;

    private int size;
    private boolean isInfiniteLoop;
    private LayoutInflater inflater;

    public ImagePagerAdapter(Context context, List<HomeModel.NoticeItemlModel> imageIdList) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.imageIdList = imageIdList;
        this.size = ListUtils.getSize(imageIdList);
        isInfiniteLoop = false;
    }

    @Override
    public int getCount() {
        // Infinite loop
        return isInfiniteLoop ? Integer.MAX_VALUE : ListUtils.getSize(imageIdList);
    }

    /**
     * get really position
     *
     * @param position
     * @return
     */
    private int getPosition(int position) {
        return isInfiniteLoop ? position % size : position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup container) {

        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_pager_two_image, null);
            holder.imageView = view.findViewById(R.id.image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = imageIdList.get(getPosition(position)).getTitle();
                String url = imageIdList.get(getPosition(position)).getLink();
                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(url)) {

                    BaseRequest baseRequest = new BaseRequest();
                    String postData = baseRequest.getCommonJsonData();
                    context.startActivity(new Intent(context, NoticeWebActivity.class).putExtra("title", title).putExtra("url", url).putExtra("postData", postData));
                }
            }
        });
        GlideUtil.loadImageTwo(context, imageIdList.get(getPosition(position)).getLogo(), holder.imageView);//不带圆角的
        return view;
    }

    private static class ViewHolder {

        ImageView imageView;
    }

    /**
     * @return the isInfiniteLoop
     */
    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }
}
