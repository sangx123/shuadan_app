package com.xinfu.qianxiaozhuang.api;

import com.orhanobut.hawk.Hawk;
import com.xinfu.qianxiaozhuang.SpConfig;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class BaseRequest {

    /**
     * 不加密
     *
     * @return
     */
    public String getCommonJsonData() {

        Map<String, Object> childParams = new HashMap<String, Object>();
        childParams.put("memberId", Hawk.get(SpConfig.memberId));
        childParams.put("accessToken", Hawk.get(SpConfig.accessToken));
        return getJSONObjectNew(childParams).toString();
    }

    /**
     * 在安卓系统为4.2.2以上这个方法可以不用，
     *
     * @param map
     * @return
     */
    public JSONObject getJSONObjectNew(Map<String, Object> map) {

        return new JSONObject(map);

    }

}
