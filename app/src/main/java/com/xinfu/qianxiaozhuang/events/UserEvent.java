package com.xinfu.qianxiaozhuang.events;

/**
 * 事件实体类
 */
public class UserEvent {

    private boolean isRefreshForMyOrderActivity;//MyOrderActivity 界面刷新


    public boolean isRefreshForMyOrderActivity() {
        return isRefreshForMyOrderActivity;
    }

    public void setRefreshForMyOrderActivity(boolean refreshForMyOrderActivity) {
        isRefreshForMyOrderActivity = refreshForMyOrderActivity;
    }
}
