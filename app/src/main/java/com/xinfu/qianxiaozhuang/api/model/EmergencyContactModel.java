package com.xinfu.qianxiaozhuang.api.model;


/**
 * 获取紧急联系人信息
 */
public class EmergencyContactModel {
        private String contactName;
        private String contactRel;
        private String contactPhone;
        private String otherName;
        private String otherRel;
        private String otherPhone;
        public void setContactName(String contactName) {
            this.contactName = contactName;
        }
        public String getContactName() {
            return contactName;
        }

        public void setContactRel(String contactRel) {
            this.contactRel = contactRel;
        }
        public String getContactRel() {
            return contactRel;
        }

        public void setContactPhone(String contactPhone) {
            this.contactPhone = contactPhone;
        }
        public String getContactPhone() {
            return contactPhone;
        }

        public void setOtherName(String otherName) {
            this.otherName = otherName;
        }
        public String getOtherName() {
            return otherName;
        }

        public void setOtherRel(String otherRel) {
            this.otherRel = otherRel;
        }
        public String getOtherRel() {
            return otherRel;
        }

        public void setOtherPhone(String otherPhone) {
            this.otherPhone = otherPhone;
        }
        public String getOtherPhone() {
            return otherPhone;
        }
}
