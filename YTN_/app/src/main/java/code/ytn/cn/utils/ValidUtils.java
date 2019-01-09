package code.ytn.cn.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class ValidUtils {

    /**
     * 字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null)
            return true;
        if (str.equals(""))
            return true;
        else
            return false;
    }

    /**
     * 字符串是否为非空
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 是否为纯数字
     *
     * @return
     */
    public static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("^[0-9]*$");
        Matcher invalid = pattern.matcher(str);
        return invalid.matches();
    }



    /**
     * 是否为手机号码
     *
     * @return
     */
    public static boolean isTelephone(String telephone) {
        Pattern pattern = Pattern.compile("[1]\\d{10}");
        Matcher invalid = pattern.matcher(telephone);
        return invalid.matches();
    }


    /**
     * 是否为身份证号
     *
     * @return
     */
    public static boolean isIDCardNum(String idNum) {
        Pattern pattern = Pattern.compile("^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$");
        Matcher invalid = pattern.matcher(idNum);
        return invalid.matches();
    }

    /**
     * 是否为合法中文姓名
     *
     * @return
     */
    public static boolean isChineseName(String name) {
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9\\u4e00-\\u9fa5\\·]{2,15})$$");
        Matcher invalid = pattern.matcher(name);
        return invalid.matches();
    }

    /**
     * 是否为指定格式的合法日期
     *
     * @return
     */
    public static boolean isDateByFormat(String date, String format) {

        DateFormat df = new SimpleDateFormat(format);
        try {
            df.parse(date);
        } catch (Exception e) {
            //如果不能转换,肯定是错误格式
            return false;
        }
        return true;
    }

}
