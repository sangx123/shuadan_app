package com.xinfu.qianxiaozhuang.golbal;


/**
 * 一些常用的常量
 * Created by FanBei on 2016/1/15.
 */
public class Constant {


    /**
     * 提现记录状态
     */
    public static String getLoanRecord(int status) {
        //   1 审核通过 2审核失败 3 进行中 4 完成 5 退款
        String record = "";
        if (status == 0) {
            record = "";
        } else if (status == 1) {
            //record = "审核通过";
            record = "审核中";
        } else if (status == 2) {
            record = "退款\n失败";
        } else if (status == 3) {
            record = "进行中";
        } else if (status == 4) {
            record = "已过期";
        } else if (status == 5) {
            record = "已退款";
        }
        return record;
    }


}
