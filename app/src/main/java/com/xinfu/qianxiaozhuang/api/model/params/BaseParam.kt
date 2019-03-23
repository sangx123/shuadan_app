package com.xinfu.qianxiaozhuang.api.model.params

import com.orhanobut.hawk.Hawk
import com.xinfu.qianxiaozhuang.SpConfig
import java.util.RandomAccess
open class BaseParam(var memberId:String?=Hawk.get(SpConfig.memberId),var accessToken: String?=Hawk.get(SpConfig.accessToken))