package com.xinfu.qianxiaozhuang.api.model;

/**
 * 登录
 */
public class LoginModel  {

   private String accessToken;//令牌
   private String memberId;//用户ID


   public String getAccessToken() {
      return accessToken;
   }

   public void setAccessToken(String accessToken) {
      this.accessToken = accessToken;
   }

   public String getMemberId() {
      return memberId;
   }

   public void setMemberId(String memberId) {
      this.memberId = memberId;
   }
}
