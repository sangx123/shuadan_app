package com.xinfu.qianxiaozhuang.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xinfu.qianxiaozhuang.R;
import com.xinfu.qianxiaozhuang.api.model.RefundViewItemlModel;
import com.xinfu.qianxiaozhuang.utils.GlideUtil;

import java.io.File;
import java.util.ArrayList;

/**
 * 各个推荐平台的适配器
 * Created by FanBei on 2017/4/10.
 */

public class RefundPictureUploadAdapter extends RecyclerView.Adapter {

    private final static int HEAD_COUNT = 1;
    private final static int FOOT_COUNT = 1;

    private final static int TYPE_HEAD = 0;
    private final static int TYPE_CONTENT = 1;
    private final static int TYPE_FOOTER = 2;

    private Activity activity;
    private ArrayList<RefundViewItemlModel> items;
    private IRecycleViewCallBack iRecycleViewCallBack;

    public RefundPictureUploadAdapter(Activity activity, ArrayList<RefundViewItemlModel> items, IRecycleViewCallBack iRecycleViewCallBack) {
        this.activity = activity;
        this.items = items;
        this.iRecycleViewCallBack = iRecycleViewCallBack;
    }

    public int getContentSize() {
        return items.size();
    }

    public boolean isHead(int position) {
        return HEAD_COUNT != 0 && position == 0;
    }

    public boolean isFoot(int position) {
        return FOOT_COUNT != 0 && position == getContentSize() + HEAD_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        int contentSize = getContentSize();
        if (HEAD_COUNT != 0 && position == 0) { // 头部
            return TYPE_HEAD;
        } else if (FOOT_COUNT != 0 && position == HEAD_COUNT + contentSize) { // 尾部
            return TYPE_FOOTER;
        } else {
            return TYPE_CONTENT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEAD) {
            View itemView = LayoutInflater.from(activity).inflate(R.layout.item_refund_upload_header, parent, false);
            return new RefundPictureUploadAdapter.HeadHolder(itemView);
        } else if (viewType == TYPE_CONTENT) {
            View itemView = LayoutInflater.from(activity).inflate(R.layout.item_refund_upload, parent, false);
            return new RefundPictureUploadAdapter.ItemViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(activity).inflate(R.layout.item_refund_upload_bottom, parent, false);
            return new RefundPictureUploadAdapter.FootHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RefundPictureUploadAdapter.HeadHolder) { // 头部

            HeadHolder myHolder = (HeadHolder) holder;

        } else if (holder instanceof RefundPictureUploadAdapter.ItemViewHolder) { // 内容
            ItemViewHolder myHolder = (ItemViewHolder) holder;
            final int tempPosition = position - 1;
            String logo = items.get(tempPosition).getLogo();
            GlideUtil.loadImageFive(activity, logo, myHolder.image_logo);
            final String platform = items.get(tempPosition).getPlatform();
            myHolder.image_thumbnail1.setVisibility(View.GONE);
            myHolder.image_thumbnail2.setVisibility(View.GONE);
            myHolder.image_thumbnail3.setVisibility(View.GONE);
            if(!TextUtils.isEmpty(items.get(tempPosition).getPath1())||!TextUtils.isEmpty(items.get(tempPosition).getPath2())||!TextUtils.isEmpty(items.get(tempPosition).getPath3())){
                myHolder.layout_upload.setVisibility(View.GONE);
            }else {
                myHolder.layout_upload.setVisibility(View.VISIBLE);
            }
            if(!TextUtils.isEmpty(items.get(tempPosition).getPath1())){
                myHolder.image_thumbnail1.setVisibility(View.VISIBLE);
                Glide.with(activity).load(new File(items.get(tempPosition).getPath1())).into(myHolder.image_thumbnail1);
            }
            if(!TextUtils.isEmpty(items.get(tempPosition).getPath2())){
                myHolder.image_thumbnail2.setVisibility(View.VISIBLE);
                Glide.with(activity).load(new File(items.get(tempPosition).getPath2())).into(myHolder.image_thumbnail2);
            }
            if(!TextUtils.isEmpty(items.get(tempPosition).getPath3())){
                myHolder.image_thumbnail3.setVisibility(View.VISIBLE);
                Glide.with(activity).load(new File(items.get(tempPosition).getPath3())).into(myHolder.image_thumbnail3);
            }
            myHolder.txt_platform.setText(platform);
            myHolder.layout_upload.setOnClickListener(new View.OnClickListener() {//拍照缩略图
                @Override
                public void onClick(View v) {
                    iRecycleViewCallBack.choicePhoto(tempPosition);
                    //activity.startActivity(new Intent(activity, NoticeWebActivity.class).putExtra("title", platform).putExtra("url", link));

                }
            });


        } else if (holder instanceof RefundPictureUploadAdapter.FootHolder) { // 尾部

            FootHolder myHolder = (FootHolder) holder;
            myHolder.toSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iRecycleViewCallBack.onUploadPictureBack();
                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return items.size() + HEAD_COUNT + FOOT_COUNT;
    }

    // 头部
    private class HeadHolder extends RecyclerView.ViewHolder {


        public HeadHolder(View itemView) {
            super(itemView);
        }
    }

    // 内容
    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView image_logo;//平台logo
        TextView txt_platform;
        ImageView image_thumbnail;//缩略图
        LinearLayout layout_upload;
        ImageView image_thumbnail1;//缩略图
        ImageView image_thumbnail2;//缩略图
        ImageView image_thumbnail3;//缩略图
        public ItemViewHolder(View itemView) {
            super(itemView);
            image_logo = itemView.findViewById(R.id.image_logo);
            txt_platform = itemView.findViewById(R.id.txt_platform);
            image_thumbnail = itemView.findViewById(R.id.image_thumbnail);
            layout_upload = itemView.findViewById(R.id.layout_upload);
            image_thumbnail1 = itemView.findViewById(R.id.image_thumbnail1);
            image_thumbnail2 = itemView.findViewById(R.id.image_thumbnail2);
            image_thumbnail3 = itemView.findViewById(R.id.image_thumbnail3);
        }
    }

    // 尾部
    private class FootHolder extends RecyclerView.ViewHolder {

        Button toSubmit;

        public FootHolder(View itemView) {
            super(itemView);
            toSubmit = itemView.findViewById(R.id.toSubmit);
        }
    }

    public interface IRecycleViewCallBack {

        public void onUploadPictureBack();

        void choicePhoto(int position);
    }

}
