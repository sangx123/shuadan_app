package com.xinfu.qianxiaozhuang.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: ExpresssoinValidateUtil
 * @Description: 正则表达式验证类(身份证 、 银行卡 、 邮箱 、 手机号 、 中文等常用表达式)
 * Created by FanBei on 2016/1/20.
 */
public class ExpresssoinValidateUtil {

    private static Pattern pattern = null;
    private static Matcher macher = null;
    private static DecimalFormat myformat = null;

    /*----------常用输入验证------*/

    // 匹配双字节字符(包括汉字在内)：[^x00-xff] ---已验证
    public static boolean isDoubleByteString(String inputString) {
        pattern = Pattern.compile("[^x00-xff]");
        macher = pattern.matcher(inputString);
        return macher.find();
    }

    // 匹配HTML标记的正则表达式：/< (.*)>.*|< (.*) />/ ---未验证：可以实现HTML过滤
    public static boolean isHtmlString(String inputString) {
        pattern = Pattern.compile("/< (.*)>.*|< (.*) />/");
        macher = pattern.matcher(inputString);
        return macher.find();
    }

    // 匹配首尾空格的正则表达式：[\\s*)]+\\w+[\\s*$] ---已验证
    public static boolean isTrimStartAndEndInthisString(String inputString) {
        pattern = Pattern.compile("[\\s*)]+\\w+[\\s*$]");
        macher = pattern.matcher(inputString);
        return macher.find();
    }

//    // 邮箱规则：用户名@服务器名.后缀 ---已验证
//    // 匹配Email地址的正则表达式：^([a-z0-9A-Z]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}
//    public static boolean isEmail(String inputString) {
//        pattern = Pattern
//                .compile("^([a-z0-9A-Z]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}");
//        macher = pattern.matcher(inputString);
//        return macher.find();
//    }

    /**
     * 验证邮箱
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }


    // 匹配网址URL的正则表达式：^http://[a-zA-Z0-9./\\s] ---已验证

    public static boolean isUrl(String inputString) {
        pattern = Pattern.compile("^http://[a-zA-Z0-9./\\s]");
        macher = pattern.matcher(inputString);
        return macher.find();
    }


    // 匹配网址URL的正则表达式：由数字、26个英文字母或者下划线组成的字符串:

    public static boolean isUname(String inputString) {
        pattern = Pattern.compile("^[0-9a-zA-Z_]{1,}$");
        macher = pattern.matcher(inputString);
        return macher.find();
    }


    // 验证用户密码: （`()-_=+|\[]{};’:”/><,.）
    // 正确格式为：必须包含“数字”,“字母”,“特殊字符”两种以上 ~!@#$%^&*?
    public static boolean isPassword(String inputString) {
        pattern = Pattern
                .compile("(?!^(\\d+|[a-zA-Z]+|[~!@#$%^&*?`()_=+|\\[\\]{}/\\;’:”.<>-]+)$)^[\\w~!@#\\$%\\^\\&\\*\\?`\\(\\)_=\\+\\|\\[\\]\\{}/\\\\;’:”.<>-]+$");
        macher = pattern.matcher(inputString);
        return macher.find();
    }

    // 验证身份证是否有效15位或18位 ^\\d{15}(\\d{2}[0-9xX])?$ ---已验证<包括对年月日的合法性进行验证>
    public static boolean isIdCard(String inputString) {
        pattern = Pattern.compile("^\\d{15}(\\d{2}[0-9xX])?$");
        macher = pattern.matcher(inputString);
        if (macher.find()) { // 对年月日字符串的验证
            String power = inputString.substring(inputString.length() - 12,
                    inputString.length() - 4);
            pattern = Pattern
                    .compile("^[1-2]+([0-9]{3})+(0[1-9][0-2][0-9]|0[1-9]3[0-1]|1[0-2][0-3][0-1]|1[0-2][0-2][0-9])");
            macher = pattern.matcher(power);
        }
        return macher.find();
    }

    // 验证固定电话号码 ^(([0-9]{3,4})|([0-9]{3,4})-)?[0-9]{7,8}$ ---已验证
    public static boolean isTelePhone(String inputString) {
        pattern = Pattern.compile("^(([0-9]{3,4})|([0-9]{3,4})-)?[0-9]{7,8}$");
        macher = pattern.matcher(inputString);
        return macher.find();
    }

    // 验证移动电话号码 ^[1][3-8]+\\d{9} ---已验证
    public static boolean isMobilePhone(String inputString) {
        pattern = Pattern.compile("^[1][3-9]+\\d{9}");
        macher = pattern.matcher(inputString);
        return macher.find();
    }

    // 只能输入汉字，匹配中文字符的正则表达式：^[\u4e00-\u9fa5]*$--已验证
    public static boolean isChineseString(String inputString) {
        pattern = Pattern.compile("^[\u4e00-\u9fa5]*$");
        macher = pattern.matcher(inputString);
        return macher.find();
    }

    /*--------- 数字操作验证---对于使用过正则表达式的人而言，下面的就太简单了故不再测试--*/

    // 匹配正整数 ^[1-9]d*$　 　
    public static boolean isPositiveInteger(String inputString) {
        pattern = Pattern.compile("^[1-9]d*$");
        macher = pattern.matcher(inputString);
        return macher.find();
    }

    // 匹配负整数 ^-[1-9]d*$ 　
    public static boolean isNegativeInteger(String inputString) {
        pattern = Pattern.compile("^-[1-9]d*$");
        macher = pattern.matcher(inputString);
        return macher.find();
    }

    // 匹配整数 ^-?[1-9]d*$　　
    public static boolean isInteger(String inputString) {
        pattern = Pattern.compile("^-?[1-9]d*$");
        macher = pattern.matcher(inputString);
        return macher.find();
    }

    // 匹配非负整数（正整数 + 0） ^[1-9]d*|0$　
    public static boolean isNotNegativeInteger(String inputString) {
        pattern = Pattern.compile("^[1-9]d*|0$");
        macher = pattern.matcher(inputString);
        return macher.find();
    }

    // 匹配非正整数（负整数 + 0） ^-[1-9]d*|0$　
    public static boolean isNotPositiveInteger(String inputString) {
        pattern = Pattern.compile("^-[1-9]d*|0$");
        macher = pattern.matcher(inputString);
        return macher.find();
    }

    // 匹配正浮点数 ^[1-9]d*.d*|0.d*[1-9]d*$　　
    public static boolean isPositiveFloat(String inputString) {
        pattern = Pattern.compile("^[1-9]d*.d*|0.d*[1-9]d*$");
        macher = pattern.matcher(inputString);
        return macher.find();
    }

    // 匹配负浮点数 ^-([1-9]d*.d*|0.d*[1-9]d*)$　
    public static boolean isNegativeFloat(String inputString) {
        pattern = Pattern.compile("^-([1-9]d*.d*|0.d*[1-9]d*)$");
        macher = pattern.matcher(inputString);
        return macher.find();
    }

    // 匹配浮点数 ^-?([1-9]d*.d*|0.d*[1-9]d*|0?.0+|0)$　

    public static boolean isFloat(String inputString) {
        pattern = Pattern.compile("^-?([1-9]d*.d*|0.d*[1-9]d*|0?.0+|0)$");
        macher = pattern.matcher(inputString);
        return macher.find();
    }

    // 匹配非负浮点数（正浮点数 + 0）^[1-9]d*.d*|0.d*[1-9]d*|0?.0+|0$　　
    public static boolean isNotNegativeFloat(String inputString) {
        pattern = Pattern.compile("^[1-9]d*.d*|0.d*[1-9]d*|0?.0+|0$");
        macher = pattern.matcher(inputString);
        return macher.find();
    }

    // 匹配非正浮点数（负浮点数 + 0）^(-([1-9]d*.d*|0.d*[1-9]d*))|0?.0+|0$
    public static boolean isNotPositiveFloat(String inputString) {
        pattern = Pattern.compile("^(-([1-9]d*.d*|0.d*[1-9]d*))|0?.0+|0$");
        macher = pattern.matcher(inputString);
        return macher.find();
    }

    // 只能输入数字：“^[0-9]*$”
    public static boolean isNumber(String inputString) {
        pattern = Pattern.compile("^[0-9]*$");
        macher = pattern.matcher(inputString);
        return macher.find();
    }

    // 只能输入n位的数字：“^d{n}$”
    public static boolean isNumberFormatLength(int length, String inputString) {
        pattern = Pattern.compile("^d{" + length + "}$");
        macher = pattern.matcher(inputString);
        return macher.find();
    }

    // 只能输入至少n位数字：“^d{n,}$”
    public static boolean isNumberLengthLess(int length, String inputString) {
        pattern = Pattern.compile("^d{" + length + ",}$");
        macher = pattern.matcher(inputString);
        return macher.find();
    }

    // 只能输入m-n位的数字：“^d{m,n}$”
    public static boolean isNumberLengthBetweenLowerAndUpper(int lower,
                                                             int upper, String inputString) {
        pattern = Pattern.compile("^d{" + lower + "," + upper + "}$");
        macher = pattern.matcher(inputString);
        return macher.find();
    }

    // 只能输入零和非零开头的数字：“^(0|[1-9][0-9]*)$”
    public static boolean isNumberStartWithZeroOrNot(String inputString) {
        pattern = Pattern.compile("^(0|[1-9][0-9]*)$");
        macher = pattern.matcher(inputString);
        return macher.find();
    }

    // 只能输入有两位小数的正实数：“^[0-9]+(.[0-9]{2})?$”
    public static boolean isNumberInPositiveWhichHasTwolengthAfterPoint(
            String inputString) {
        pattern = Pattern.compile("^[0-9]+(.[0-9]{2})?$");
        macher = pattern.matcher(inputString);
        return macher.find();
    }

    // 只能输入有1-3位小数的正实数：“^[0-9]+(.[0-9]{1,3})?$”
    public static boolean isNumberInPositiveWhichHasOneToThreelengthAfterPoint(
            String inputString) {
        pattern = Pattern.compile("^[0-9]+(.[0-9]{1,3})?$");
        macher = pattern.matcher(inputString);
        return macher.find();
    }

    // 只能输入非零的正整数：“^+?[1-9][0-9]*$”
    public static boolean isIntegerUpZero(String inputString) {
        pattern = Pattern.compile("^+?[1-9][0-9]*$");
        macher = pattern.matcher(inputString);
        return macher.find();
    }

    // 只能输入非零的负整数：“^-[1-9][0-9]*$”
    public static boolean isIntegerBlowZero(String inputString) {
        pattern = Pattern.compile("^-[1-9][0-9]*$");
        macher = pattern.matcher(inputString);
        return macher.find();
    }

    // 只能输入由26个英文字母组成的字符串：“^[A-Za-z]+$”
    public static boolean isEnglishAlphabetString(String inputString) {
        pattern = Pattern.compile("^[A-Za-z]+$");
        macher = pattern.matcher(inputString);
        return macher.find();
    }

    // 只能输入由26个大写英文字母组成的字符串：“^[A-Z]+$”
    public static boolean isUppercaseEnglishAlphabetString(String inputString) {
        pattern = Pattern.compile("^[A-Z]+$");
        macher = pattern.matcher(inputString);
        return macher.find();
    }

    // 只能输入由26个小写英文字母组成的字符串：“^[a-z]+$”
    public static boolean isLowerEnglishAlphabetString(String inputString) {
        pattern = Pattern.compile("^[a-z]+$");
        macher = pattern.matcher(inputString);
        return macher.find();
    }

    // 只能输入由数字和26个英文字母组成的字符串：“^[A-Za-z0-9]+$”
    public static boolean isNumberEnglishAlphabetString(String inputString) {
        pattern = Pattern.compile("^[A-Za-z0-9]+$");
        macher = pattern.matcher(inputString);
        return macher.find();
    }

    // 只能输入由数字、26个英文字母或者下划线组成的字符串：“^w+$”
    public static boolean isNumberEnglishAlphabetWithUnderlineString(
            String inputString) {
        pattern = Pattern.compile("^w+$");
        macher = pattern.matcher(inputString);
        return macher.find();
    }

//    /**
//     * 验证银卡卡号(这个目前不健全)
//     *
//     * @param cardNo
//     * @return
//     */
//    public static boolean isBankCard(String cardNo) {
//        Pattern p = Pattern.compile("^\\d{16,19}$|^\\d{6}[- ]\\d{10,13}$|^\\d{4}[- ]\\d{4}[- ]\\d{4}[- ]\\d{4,7}$");
//        Matcher m = p.matcher(cardNo);
//
//        return m.matches();
//    }

    /**
     * 判断是否是银行卡号
     *
     * @param cardId
     * @return
     */
    public static boolean isBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;

    }

    private static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null
                || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            // 如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    /**
     * 格式化银行卡
     *
     * @param cardNo
     * @return
     */
    public static String formatCard(String cardNo) {
        String card = "";
        if (!TextUtils.isEmpty(cardNo)) {
            card = cardNo.substring(0, 4) + " **** **** ";
            card += cardNo.substring(cardNo.length() - 4);
        }
        return card;
    }

    /**
     * 银行卡后四位
     *
     * @param cardNo
     * @return
     */
    public static String formatCardEndFour(String cardNo) {
        String card = "";
        if (!TextUtils.isEmpty(cardNo)) {
            card += cardNo.substring(cardNo.length() - 4);
        }
        return card;
    }

    /**
     * 格式化手机号码 手机号隐藏后3位
     *
     * @param mobileNumber
     * @return
     */
    public static String formatMobileNumber(String mobileNumber) {
        String card = "";
        if (!TextUtils.isEmpty(mobileNumber)) {
            card = mobileNumber.substring(0, 8) + " *** ";
//            card = mobileNumber.substring(0, 3) + " *****";
//            card +=mobileNumber.substring(mobileNumber.length() - 3);
        }
        return card;
    }

    /**
     * 用户名 隐藏后三位
     * \     * @param userName
     *
     * @return
     */
    public static String formatUserNameTwo(String userName) {
        String card = "";
        if (!TextUtils.isEmpty(userName)) {
            card = userName.replace(userName.substring(userName.length() - 3, userName.length()), "***");
        }
        return card;
    }

    /**
     * 用户名  显示首尾
     *
     * @param userName
     * @return
     */
    public static String formatUserName(String userName) {
        String card = "";
        if (!TextUtils.isEmpty(userName)) {
            card = userName.substring(0, 1) + " ****** ";
            card += userName.substring(userName.length() - 1);
        }
        return card;
    }

    /**
     * 用户名  显示首尾 中间三个星
     *
     * @param userName
     * @return
     */
    public static String formatUserNameThree(String userName) {
        String card = "";
        if (!TextUtils.isEmpty(userName)) {
            card = userName.substring(0, 1) + "***";
            card += userName.substring(userName.length() - 1);
        }
        return card;
    }

    /**
     * 用户名  显示首尾 中间三个星
     *
     * @param userName
     * @return
     */
    public static String formatUserNameFour(String userName) {
        String card = "";
        if (!TextUtils.isEmpty(userName)) {

            card=userName.replace(userName.substring(3,7) ,"****");

        }
        return card;
    }

//    public static String formatUserName(String userName) {
//
//        String tempUserName = "";
//        if (!TextUtils.isEmpty(userName)) {
//
//            if (userName.length() > 2) {
//                tempUserName = String.valueOf(userName.charAt(0)) + "******" + String.valueOf(userName.charAt(userName.length() - 1));
//            } else {
//                tempUserName = String.valueOf(userName.charAt(0)) + "******";
//            }
//        }
//        return tempUserName;
//    }

    /**
     * 格式数据(隐藏尾数后面所有的数据)
     *
     * @param data     数据
     * @param mantissa 尾数
     * @return
     */
    public static String formatData(String data, int mantissa) {
        String value = "";
        if (!TextUtils.isEmpty(data)) {
            value = data.substring(0, mantissa);
            for (int i = value.length(); i < data.length(); i++) {

                value += "*";

            }
        }
        return value;
    }

    /**
     * 钱数格式化
     *
     * @param money
     * @return
     */
    public static String fomatMoney(double money) {
        if (myformat == null) {
            myformat = new DecimalFormat();
        }
        if (money == 0) {
            return "0.00";
        } else {
            myformat.applyPattern("##,##0.00");
            return myformat.format(money);
        }
    }

    /**
     * 钱数格式化
     *
     * @param money 资金数
     * @return
     */
    public static String fomatMoneyTwo(double money) {
        if (myformat == null) {
            myformat = new DecimalFormat();
        }
        if (money == 0) {
            return "0";
        } else {
            myformat.applyPattern("#");
            return myformat.format(money);
        }
    }

    /**
     * 钱数格式化
     *
     * @param money
     * @return
     */
    public static String fomatMoneyThree(double money) {
        if (myformat == null) {
            myformat = new DecimalFormat();
        }
        if (money == 0) {
            return "0.00";
        } else {
            myformat.applyPattern("##,##0");
            return myformat.format(money);
        }
    }

    /**
     * 钱数格式化
     *
     * @param money
     * @return
     */
    public static String fomatMoneyFour(double money) {
        if (myformat == null) {
            myformat = new DecimalFormat();
        }
        if (money == 0) {
            return "0.00";
        } else {
            myformat.applyPattern("####0.00");
            return myformat.format(money);
        }
    }

    /**
     * 钱数格式化
     *
     * @param money
     * @return
     */
    public static String fomatMoneyFive(BigDecimal money) {
        try {
            if (myformat == null) {
                myformat = new DecimalFormat();
            }
            myformat.applyPattern("##,##0.00");
            return myformat.format(money);
        } catch (Exception e) {
            return "0.00";
        }
    }

    /**
     * 钱数格式化
     *
     * @param money
     * @return
     */
    public static String fomatMoneySix(String money) {
        try {
            if (TextUtils.isEmpty(money)) {
                return "0.00";
            }
            if (myformat == null) {
                myformat = new DecimalFormat();
            }
            myformat.applyPattern("##,##0.00");
            return myformat.format(money);
        } catch (Exception e) {
            return "0.00";
        }
    }

    /**
     * 使用java正则表达式去掉多余的.与0(不取小数)
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 不进行四舍五入
     *
     * @param value
     * @return
     */
    public static int zeroAndDotFloor(double value) {

        double tempValue = Math.floor(value);
        return Integer.parseInt(subZeroAndDot(String.valueOf(tempValue)));

    }

    /**
     * 四舍五入,保留两位有效数字
     *
     * @param value
     * @return
     */
    public static BigDecimal formatBank(double value) {

        return new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 四舍五入,保留两位有效数字
     *
     * @param value
     * @return
     */
    public static BigDecimal formatBank(String value) {

        return new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 四舍五入,保留两位有效数字
     *
     * @param bigDecimal
     * @return
     */
    public static BigDecimal formatBank(BigDecimal bigDecimal) {

        bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bigDecimal;
    }

    /**
     * 判断数据是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.length() == 0 || str.trim().isEmpty() == Boolean.TRUE)
            return true;
        else
            return false;
    }

    /**
     * 只针对增值劵
     *
     * @param doubleValue
     * @return
     */
    public static String doubleTransferString(double doubleValue) {

        String strValue = "0";
        try {
            strValue = String.valueOf(doubleValue);
            if (strValue.contains(".00")) {
                strValue = strValue.replace(".00", "");
            } else if (strValue.equals("0.0")) {
                strValue = "0";
            } else if (strValue.substring(strValue.indexOf("."), strValue.length()).equals(".0")) {
                strValue = strValue.replace(".0", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strValue;
    }


    /**
     * 将数据保留两位小数
     */
    public static String getThreeDecimal(double doubleValue) {

        BigDecimal b = new BigDecimal(doubleValue);
        double df = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        String value = String.valueOf(df);
        if (!TextUtils.isEmpty(value)) {
            String tempValue = value.substring(value.indexOf("."), value.length());
            if (tempValue.equals(".0")) {
                value = value.replace(".0", ".00");
            }
        } else {
            value = "0.00";
        }
        return value;
    }


}
