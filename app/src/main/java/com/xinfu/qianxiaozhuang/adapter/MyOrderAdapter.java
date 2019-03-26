package com.xinfu.qianxiaozhuang.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.xinfu.qianxiaozhuang.R;
import com.xinfu.qianxiaozhuang.activity.loan.ApplyLoanToGetMoneyActivity;
import com.xinfu.qianxiaozhuang.activity.loan.QueryBackMoneyActivity;
import com.xinfu.qianxiaozhuang.api.model.LoanRecordItemModel;
import com.xinfu.qianxiaozhuang.golbal.Constant;
import java.util.ArrayList;

/**
 * Created by FanBei on 2017/12/13.
 */

public class MyOrderAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<LoanRecordItemModel> arrayList;
    private LayoutInflater inflater;

    public MyOrderAdapter(Activity context, ArrayList<LoanRecordItemModel> arrayList) {

        this.context = context;
        this.arrayList = arrayList;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_my_loan_record_layout, null);
            viewHolder.layout_top = convertView.findViewById(R.id.layout_top);
            viewHolder.txt_order_no = convertView.findViewById(R.id.txt_order_no);
            viewHolder.txt_status = convertView.findViewById(R.id.txt_status);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txt_order_no.setText(context.getString(R.string.order_no) + "  " + arrayList.get(position).getOrderId());
        final int statusCode = arrayList.get(position).getStatus();
        viewHolder.txt_status.setText(Constant.getLoanRecord(statusCode));
        if (statusCode == 1) {//审核中
            viewHolder.txt_status.setBackgroundResource(R.drawable.drawable_blue_circle);
        } else if (statusCode == 2) {//退款失败
            viewHolder.txt_status.setBackgroundResource(R.drawable.drawable_pick_circle);
        } else if (statusCode == 4) {//已过期
            viewHolder.txt_status.setBackgroundResource(R.drawable.drawable_gray_circle);
        } else if (statusCode == 5) {//已退款
            viewHolder.txt_status.setBackgroundResource(R.drawable.drawable_gray_circle);
        }
        viewHolder.layout_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(statusCode == 4||statusCode == 5){
                    return;
                }
                if (statusCode == 3) {//进行中(进入推送结果页面)
                    context.startActivity(new Intent(context, ApplyLoanToGetMoneyActivity.class));
                }
                else {
                    context.startActivity(new Intent(context, QueryBackMoneyActivity.class).putExtra("result", statusCode));
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        LinearLayout layout_top;
        TextView txt_order_no;//订单号
        TextView txt_status;//状态

    }
}
