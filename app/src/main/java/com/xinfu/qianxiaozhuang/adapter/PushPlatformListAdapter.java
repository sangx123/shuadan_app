package com.xinfu.qianxiaozhuang.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinfu.qianxiaozhuang.R;
import com.xinfu.qianxiaozhuang.activity.NoticeWebActivity;
import com.xinfu.qianxiaozhuang.api.BaseRequest;
import com.xinfu.qianxiaozhuang.api.model.RefundViewItemlModel;
import com.xinfu.qianxiaozhuang.utils.ExpresssoinValidateUtil;
import com.xinfu.qianxiaozhuang.utils.GlideUtil;

import java.util.ArrayList;

/**
 * 推送平台适配器
 * Created by FanBei on 2017/4/10.
 */

public class PushPlatformListAdapter extends RecyclerView.Adapter {

    private final static int HEAD_COUNT = 1;
    private final static int FOOT_COUNT = 1;

    private final static int TYPE_HEAD = 0;
    private final static int TYPE_CONTENT = 1;
    private final static int TYPE_FOOTER = 2;

    private Activity activity;
    private ArrayList<RefundViewItemlModel> items;
    private IRecycleViewCallBack iRecycleViewCallBack;
    private double creditLimit;

    public PushPlatformListAdapter(Activity activity, ArrayList<RefundViewItemlModel> items, IRecycleViewCallBack iRecycleViewCallBack, Double creditLimit) {
        this.activity = activity;
        this.items = items;
        this.iRecycleViewCallBack = iRecycleViewCallBack;
        this.creditLimit = creditLimit;
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
            View itemView = LayoutInflater.from(activity).inflate(R.layout.header_platform_layout, parent, false);
            return new PushPlatformListAdapter.HeadHolder(itemView);
        } else if (viewType == TYPE_CONTENT) {
            View itemView = LayoutInflater.from(activity).inflate(R.layout.item_platfrom_layout, parent, false);
            return new PushPlatformListAdapter.ItemViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(activity).inflate(R.layout.bottom_platform_layout, parent, false);
            return new PushPlatformListAdapter.FootHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PushPlatformListAdapter.HeadHolder) { // 头部

            HeadHolder myHolder = (HeadHolder) holder;
            myHolder.mMoney.setText(ExpresssoinValidateUtil.fomatMoneyThree(creditLimit));
            myHolder.mKefu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iRecycleViewCallBack.onCustomerServiceBack();
                }
            });
            myHolder.mDaojishi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iRecycleViewCallBack.onCountDownBack();
                }
            });

        } else if (holder instanceof PushPlatformListAdapter.ItemViewHolder) { // 内容
            ItemViewHolder myHolder = (ItemViewHolder) holder;
            int tempPosition = position - 1;
            String logo = items.get(tempPosition).getLogo();
            GlideUtil.loadImageFive(activity, logo, myHolder.image);
            final String platform = items.get(tempPosition).getPlatform();
            myHolder.txt_platform.setText(platform);
            String amountitems = items.get(tempPosition).getAmount();//可放金额
            myHolder.txt_amount.setText(amountitems);
            String probability = items.get(tempPosition).getProbability();//概率
            myHolder.txt_probability.setText(probability);
            String description = items.get(tempPosition).getDescription();//推荐描述
            myHolder.txt_description_one.setText(description);
            displayOrHideView(myHolder.txt_description_one, View.VISIBLE);
            final String link = items.get(tempPosition).getLink();//链接地址
            myHolder.txt_go_apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String postData = new BaseRequest().getCommonJsonData();
                    activity.startActivity(new Intent(activity, NoticeWebActivity.class).putExtra("title", platform).putExtra("url", link).putExtra("postData", postData));

                }
            });


        } else if (holder instanceof PushPlatformListAdapter.FootHolder) { // 尾部

            FootHolder myHolder = (FootHolder) holder;
            myHolder.toApplyLoanToBackMoney.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iRecycleViewCallBack.onRefundCallBack();
                }
            });

        }
    }

    private void displayOrHideView(View view, int visible) {

        if (view != null && view.getVisibility() != visible) {

            view.setVisibility(visible);

        }
    }

    @Override
    public int getItemCount() {
        return items.size() + HEAD_COUNT + FOOT_COUNT;
    }

    // 头部
    private class HeadHolder extends RecyclerView.ViewHolder {

        TextView mMoney;
        LinearLayout mKefu;
        LinearLayout mDaojishi;

        public HeadHolder(View itemView) {
            super(itemView);
            mMoney = itemView.findViewById(R.id.mMoney);
            mKefu = itemView.findViewById(R.id.mKefu);
            mDaojishi = itemView.findViewById(R.id.mDaojishi);
        }
    }

    // 内容
    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView txt_platform;
        TextView txt_amount;
        TextView txt_probability;
        TextView txt_description_one;
        TextView txt_go_apply;


        public ItemViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            txt_platform = itemView.findViewById(R.id.txt_platform);
            txt_amount = itemView.findViewById(R.id.txt_amount);
            txt_probability = itemView.findViewById(R.id.txt_probability);
            txt_description_one = itemView.findViewById(R.id.txt_description_one);
            txt_go_apply = itemView.findViewById(R.id.txt_go_apply);
        }
    }

    // 尾部
    private class FootHolder extends RecyclerView.ViewHolder {

        Button toApplyLoanToBackMoney;

        public FootHolder(View itemView) {
            super(itemView);
            toApplyLoanToBackMoney = itemView.findViewById(R.id.toApplyLoanToBackMoney);
        }
    }

    public interface IRecycleViewCallBack {

        public void onCustomerServiceBack();

        public void onCountDownBack();

        public void onRefundCallBack();


    }

}
