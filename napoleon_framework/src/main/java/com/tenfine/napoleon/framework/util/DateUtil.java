package com.tenfine.napoleon.framework.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日期操作工具类
 */
public class DateUtil {

    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    private static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    /**
     * 获取几分钟之前的时间戳，用于截取数据
     * @param minute 分钟差，大于零取当前时间后的时间，小于零取当前时间前的时间
     */
    public static Date getMinuteAround(int minute) {
    	Calendar beforeTime = Calendar.getInstance();
		beforeTime.add(Calendar.MINUTE, minute);// 3分钟之前的时间
		Date beforeD = beforeTime.getTime();
		return beforeD;
    }
    
    /**
     * 时间戳转YYYYMMDDHHMMSS
	 */
    public static String conversionTimestamp(String Timestamp) {
	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	    long ttt = new Long(Timestamp);
		Date date = new Date(ttt);
		String res = simpleDateFormat.format(date);
		return res;
    }
    
    /**
     * 获取某个月份的每一天
     * Calendar 的月份取值范围是 0 - 11 ， 0代表1月 11代表12月份
	 * @param 对应年份，月份
	 * @return Every day of the month
	 */
    public static List<Integer> getDayByMonth(int yearParam, int monthParam) {
		List<Integer> list = new ArrayList<Integer>();
		Calendar aCalendar = Calendar.getInstance();
		aCalendar.set(Calendar.YEAR, yearParam);
		aCalendar.set(Calendar.MONTH, monthParam);
		
		int day = aCalendar.getActualMaximum(Calendar.DATE);
		for (int i = 1; i <= day; i++) {
			list.add(i);
		}
		return list;
	}
    
    /**
	 * 
	 * 1 第一季度 2 第二季度 3 第三季度 4 第四季度
	 * 
	 * @param date
	 * @return quarter of a year
	 */
	public static String getSeason(Date date) {

		String season = "";
 
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int month = c.get(Calendar.MONTH);
		switch (month) {
			case Calendar.JANUARY:
			case Calendar.FEBRUARY:
			case Calendar.MARCH:
				season = "一";
				break;
			case Calendar.APRIL:
			case Calendar.MAY:
			case Calendar.JUNE:
				season = "二";
				break;
			case Calendar.JULY:
			case Calendar.AUGUST:
			case Calendar.SEPTEMBER:
				season = "三";
				break;
			case Calendar.OCTOBER:
			case Calendar.NOVEMBER:
			case Calendar.DECEMBER:
				season = "四";
				break;
			default:
				break;
		}
		return season;
	}

    
    /**
     * 格式化日期与时间
     * 返回格式：yyyy-MM-dd HH:mm:ss
     */
    public static String formatDatetime(long timestamp) {
        return datetimeFormat.format(new Date(timestamp));
    }

    /**
     * 格式化日期与时间
     * 返回格式：yyyy-MM-dd HH:mm:ss
     */
    public static String formatDatetime(Date datetime) {
        return datetimeFormat.format(datetime);
    }

    /**
     * 格式化日期
     * 返回格式：yyyy-MM-dd
     */
    public static String formatDate(long timestamp) {
        return dateFormat.format(new Date(timestamp));
    }

    /**
     * 格式化时间
     * 返回格式：HH:mm:ss
     */
    public static String formatTime(long timestamp) {
        return timeFormat.format(new Date(timestamp));
    }

    /**
     * 获取当前日期与时间
     * 返回格式：yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentDatetime() {
        return datetimeFormat.format(new Date());
    }

    /**
     * 获取当前日期
     * 返回格式：yyyy-MM-dd
     */
    public static String getCurrentDate() {
        return dateFormat.format(new Date());
    }

    /**
     * 获取当前时间
     * 返回格式：HH:mm:ss
     */
    public static String getCurrentTime() {
        return timeFormat.format(new Date());
    }

    /**
     * 解析日期与时间
     *
     * @param str 格式：yyyy-MM-dd HH:mm:ss
     * @return Date对象
     */
    public static Date parseDatetime(String str) {
        Date date = null;
        try {
            date = datetimeFormat.parse(str);
        } catch (ParseException e) {
            logger.error("解析日期字符串出错！格式：yyyy-MM-dd HH:mm:ss", e);
        }
        return date;
    }

    /**
     * 解析日期
     *
     * @param str 格式：yyyy-MM-dd
     * @return Date对象
     */
    public static Date parseDate(String str) {
        Date date = null;
        try {
            date = dateFormat.parse(str);
        } catch (ParseException e) {
            logger.error("解析日期字符串出错！格式：yyyy-MM-dd", e);
        }
        return date;
    }

    /**
     * 解析时间
     *
     * @param str 格式：HH:mm:ss
     * @return Date对象
     */
    public static Date parseTime(String str) {
        Date date = null;
        try {
            date = timeFormat.parse(str);
        } catch (ParseException e) {
            logger.error("解析日期字符串出错！格式：HH:mm:ss", e);
        }
        return date;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     */
    public static int daysBetween(Date smdate, Date bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }
    
}

