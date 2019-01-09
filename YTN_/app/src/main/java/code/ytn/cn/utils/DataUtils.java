package code.ytn.cn.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by dell on 2018/5/30
 */

public class DataUtils {
    /**
     * 判断给定字符串时间是否为今日(效率不是很高，不过也是一种方法)
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate){
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if(time != null){
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if(nowDate.equals(timeDate)){
                b = true;
            }
        }
        return b;
    }

    /**
     * 将字符串转位日期类型
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };
    private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();
    /**
     * 判断是否为今天(效率比较高)
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean IsToday(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为昨天(效率比较高)
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean IsYesterday(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == -1) {
                return true;
            }
        }
        return false;
    }

    public static SimpleDateFormat getDateFormat() {
        if (null == DateLocal.get()) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return DateLocal.get();
    }

    /**
     * 获取指定年份日历
     * @param year
     * @return
     */
    private static Calendar getCalendarFormYear(int year){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.YEAR, year);
        return cal;
    }
    /**
     * 获取指定年指定周的开始日期
     * @param year
     * @param weekNo
     * @return
     */
    public static String getStartDayOfWeekNo(int year,int weekNo){
        Calendar cal = getCalendarFormYear(year);
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String monthStr = null;
        String dayStr = null;
        StringBuilder monthBuilder = new StringBuilder();
        StringBuilder dayBuilder = new StringBuilder();
        if(month < 10){
            monthStr = monthBuilder.append("0").append(month).toString();
        } else {
            monthStr = monthBuilder.append(month).toString();
        }
        if(day < 10) {
            dayStr = dayBuilder.append("0").append(day).toString();
        } else {
            dayStr = dayBuilder.append(day).toString();
        }
//        return cal.get(Calendar.YEAR) + "-" + monthStr + "-" + dayStr;
        return monthStr + "." + dayStr;
    }

    /**
     * 获取指定年指定周的结束日期
     * @param year
     * @param weekNo
     * @return
     */
    public static String getEndDayOfWeekNo(int year,int weekNo){
        Calendar cal = getCalendarFormYear(year);
        cal.set(Calendar.WEEK_OF_YEAR, weekNo);
        cal.add(Calendar.DAY_OF_WEEK, 6);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String monthStr = null;
        String dayStr = null;
        StringBuilder monthBuilder = new StringBuilder();
        StringBuilder dayBuilder = new StringBuilder();
        if(month < 10){
            monthStr = monthBuilder.append("0").append(month).toString();
        } else {
            monthStr = monthBuilder.append(month).toString();
        }
        if(day < 10) {
            dayStr = dayBuilder.append("0").append(day).toString();
        } else {
            dayStr = dayBuilder.append(day).toString();
        }
//        return cal.get(Calendar.YEAR) + "-" + monthStr + "-" +  dayStr;
        return monthStr + "." +  dayStr;
    }


    //获取 某日是某年的第几周
    public static int getWeekByDay(String day) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        calendar.setTime(format.parse(day));
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    //获取 指定开始日期和结束日期，包含日期列表。
    public static List<String> getDays(String beginDate, String endDate) throws ParseException {
        List<String> result = new ArrayList<String>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(format.parse(beginDate));

        for (long  time= calendar.getTimeInMillis(); time <= format.parse(endDate).getTime(); time = getNextDay(calendar)) {
            result.add(format.format(time));
        }
        return result;
    }

    public static long getNextDay(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        return calendar.getTimeInMillis();
    }

    public static String getWeekDays(int year, int week, int targetNum) {

        // 计算目标周数
        if (week + targetNum > 52) {
            year++;
            week += targetNum - 52;
        } else if (week + targetNum <= 0) {
            year--;
            week += targetNum + 52;
        } else {
            week += targetNum;
        }

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal=Calendar.getInstance();

        // 设置每周的开始日期
        cal.setFirstDayOfWeek(Calendar.SUNDAY);

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.WEEK_OF_YEAR, week);

        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        String beginDate = sdf.format(cal.getTime());

        cal.add(Calendar.DAY_OF_WEEK, 6);
        String endDate = sdf.format(cal.getTime());

        return beginDate + "~" + endDate;
    }
}
