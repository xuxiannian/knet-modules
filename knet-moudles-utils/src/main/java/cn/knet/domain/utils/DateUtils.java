/**
 * $Id: DateUtil.java 3163 2011-02-11 06:56:08Z xin.zhao $
 * Copyright(C) 2010-2016 happyelements.com. All rights reserved.
 */
package cn.knet.domain.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期工具类
 * @author <a href="mailto:zhaoxin@knet.cn">xin.zhao</a>
 * @version 1.0
 * @since 1.0
 */
public class DateUtils {
    /** 日期格式 */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    
    public static final String DEFAULT_DATE_FORMAT_S = "yyyyMMdd";
    /** 日期时间格式 */
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /** 时间格式 */
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    /** 每天小时数 */
    private static final long HOURS_PER_DAY = 24;
    /** 每小时分钟数 */
    private static final long MINUTES_PER_HOUR = 60;
    /** 每分钟秒数 */
    private static final long SECONDS_PER_MINUTE = 60;
    /** 每秒的毫秒数 */
    private static final long MILLIONSECONDS_PER_SECOND = 1000;
    /** 每分钟毫秒数 */
    private static final long MILLIONSECONDS_PER_MINUTE = MILLIONSECONDS_PER_SECOND * SECONDS_PER_MINUTE;
    /** 每天毫秒数 */
    private static final long MILLIONSECONDS_SECOND_PER_DAY = HOURS_PER_DAY * MINUTES_PER_HOUR * SECONDS_PER_MINUTE * MILLIONSECONDS_PER_SECOND;
    
    public static TimeZone TIMEZONE_UTC = TimeZone.getTimeZone("UTC");
    
    private DateUtils() {}
    
    /**
     * 将yyyy-MM-dd格式的字符串转换为日期对象
     * @param date 待转换字符串
     * @return 转换后日期对象
     * @see #getDate(String, String, Date)
     */
    public static Date getDate(String date) {
        return getDate(date, DEFAULT_DATE_FORMAT, null);
    }
    /**
     * 将yyyy-MM-dd HH:mm:ss格式的字符串转换为日期对象
     * @param date 待转换字符串
     * @return 转换后日期对象
     * @see #getDate(String, String, Date)
     */
    public static Date getDateTime(String date) {
        return getDate(date, DEFAULT_DATETIME_FORMAT, null);
    }
    /**
     * 将指定格式的字符串转换为日期对象
     * @param date 待转换字符串
     * @param format 日期格式
     * @return 转换后日期对象
     * @see #getDate(String, String, Date)
     */
    public static Date getDate(String date, String format) {
        return getDate(date, format, null);
    }
    /**
     * 将指定格式的字符串转换为日期对象
     * @param date 日期对象
     * @param format 日期格式
     * @param defVal 转换失败时的默认返回值
     * @return 转换后的日期对象
     */
    public static Date getDate(String date, String format, Date defVal) {
        Date d;
        try {
            d = new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            d = defVal;
        }
        return d;
    }

    /**
     * 将日期对象格式化成yyyy-MM-dd格式的字符串
     * @param date 待格式化日期对象
     * @return 格式化后的字符串
     * @see #formatDate(Date, String, String)
     */
    public static String formatDate(Date date) {
        return formatDate(date, DEFAULT_DATE_FORMAT, null);
    }
    /**
     * 将日期对象格式化成yyyy-MM-dd HH:mm:ss格式的字符串
     * @param date 待格式化日期对象
     * @return 格式化后的字符串
     * @see #formatDate(Date, String, String)
     */
    public static String forDatetime(Date date) {
        return formatDate(date, DEFAULT_DATETIME_FORMAT, null);
    }
    /**
     * 将日期对象格式化成HH:mm:ss格式的字符串
     * @param date 待格式化日期对象
     * @return 格式化后的字符串
     * @see #formatDate(Date, String, String)
     */
    public static String formatTime(Date date) {
        return formatDate(date, DEFAULT_TIME_FORMAT, null);
    }
    /**
     * 将日期对象格式化成指定类型的字符串
     * @param date 待格式化日期对象
     * @param format 格式化格式
     * @return 格式化后的字符串
     * @see #formatDate(Date, String, String)
     */
    public static String formatDate(Date date, String format) {
        return formatDate(date, format, null);
    }
    
    /**
     * 带时区的格式化时间
     * @param date
     * @param format
     * @param timeZone
     * @return
     */
    public static String formatDateTimeZone(Date date, String format, TimeZone timeZone) {
    	String ret = null;
        try {
        	SimpleDateFormat sdf = new SimpleDateFormat(format);
        	sdf.setTimeZone(timeZone);
            ret = sdf.format(date);
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return ret;
    }
    /**
     * 将日期对象格式化成指定类型的字符串
     * @param date 待格式化日期对象
     * @param format 格式化格式
     * @param defVal 格式化失败时的默认返回空
     * @return 格式化后的字符串
     */
    public static String formatDate(Date date, String format, String defVal) {
        String ret;
        try {
            ret = new SimpleDateFormat(format).format(date);
        } catch (Exception e) {
            ret = defVal;
        }
        return ret;
    }
    
    
    /**
     * 返回指定日期加上days天后的日期
     * @param date
     * @param days
     * @return
     */
    public static Date plusDays(Date date,int days){
    	return changeDays(date,days);
    }
    
    public static Date plusDaysToday(int days){
    	return plusDays(getToday(),days);
    }
    
    public static Date plusDaysTodayByYear(int year){
    	return changeDaysByYear(new Date(),year);
    }
    
    public static Date minusDaysToday(int days){
    	return minusDays(getToday(),days);
    }
    
    /**
     * 返回指定日期减去days天后的日期
     * @param date
     * @param days
     * @return
     */
    public static Date minusDays(Date date,int days){
    	return changeDays(date,-days);
    }
    
    private static Date changeDays(Date date,int days){
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	cal.add(Calendar.DAY_OF_YEAR, days);
    	return cal.getTime();
    }
    
    private static Date changeDaysByYear(Date date,int year){
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	cal.add(Calendar.YEAR, year);
    	return cal.getTime();
    }
    
    /**
     * 获取当前日期加时间
     * @return
     */
    public static Date getToday(){
    	return new Date();
    }
    
    public static Date now() {
    	return getToday();
    }
    
    public static long currentTimeMillis() {
    	return new Date().getTime();
    }

    /**
     * 获得当前时间sql.date
     */
    public static java.sql.Date getTodaySqlDate(){
    	return new java.sql.Date(getToday().getTime());
    }
    /**
     * 获取今天日期, 格式: YYYY-MM-DD
     * @return
     */
    public static String getTodayStr(){
    	return formatDate(getToday(), DEFAULT_DATE_FORMAT);
    }
    
    /**
     * 获取今天日期, 格式: YYYYMMDD
     * @return
     */
    public static String getTodaystr1(){
    	return formatDate(getToday(), DEFAULT_DATE_FORMAT_S);
    }
    
    /**
     * 比较传入日期与当前日期相差的天数
     * @param d
     * @return
     */
    public static int intervalDay(Date d){
    	return intervalDay(getToday(),d);
    }
    /**
     * 比较两个日期相差的天数
     * @param d1
     * @param d2
     * @return
     */
    public static int intervalDay(Date d1,Date d2){
    	  long intervalMillSecond = setToDayStartTime(d1).getTime() - setToDayStartTime(d2).getTime();
          //相差的天数 = 相差的毫秒数 / 每天的毫秒数 (小数位采用去尾制)
          return (int) (intervalMillSecond / MILLIONSECONDS_SECOND_PER_DAY);
    }
    
    /**
     * 将时间调整到当天0:0:0
     * @param date
     * @return
     */
    private static Date setToDayStartTime(Date date) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(date.getTime());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }
    
    
    /**
     * 判断当前时间
     * @return
     */
	public static String getDateStatus() {
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		if (hour >= 6 && hour < 12) {
			return "morning";
		} else if (hour >= 12 && hour < 18) {
			return "noon";
		} else if (hour >= 18 && hour < 24) {
			return "evning";
		} else {
			return "midnight";
		}
    }
    
    /**
     * 获得两个日期之间相差的分钟数。（date1 - date2）
     *
     * @param date1
     * @param date2
     * @return 返回两个日期之间相差的分钟数值
     */
    public static int intervalMinutes(Date date1, Date date2) {
        long intervalMillSecond = date1.getTime() - date2.getTime();

        //相差的分钟数 = 相差的毫秒数 / 每分钟的毫秒数 (小数位采用进位制处理，即大于0则加1)
        return (int) (intervalMillSecond / MILLIONSECONDS_PER_MINUTE
                + (intervalMillSecond % MILLIONSECONDS_PER_MINUTE > 0 ? 1 : 0));
    }

    /**
     * 获得两个日期之间相差的秒数差（date1 - date2）
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int intervalSeconds(Date date1, Date date2) {
        long intervalMillSecond = date1.getTime() - date2.getTime();

        return (int) (intervalMillSecond / MILLIONSECONDS_PER_SECOND
                + (intervalMillSecond % MILLIONSECONDS_PER_SECOND > 0 ? 1 : 0));
    }
    
    public static int getAge(Date birthday) {
    	Calendar now = Calendar.getInstance();
    	Calendar birth = Calendar.getInstance();
    	birth.setTime(birthday);
    	//取得生日年份
    	int year = birth.get(Calendar.YEAR);
    	//年龄
    	int age = now.get(Calendar.YEAR) - year;
    	//修正
    	now.set(Calendar.YEAR, year);
    	age = (now.before(birth)) ? age - 1 : age;
    	return age;
    }
    
    /**
     * d1 和 d2 是同一天
     * @param d1
     * @param d2
     * @return
     */
    public static boolean isSameDate(Date d1, Date d2){
    	if(d1==null || d2==null)
    		return false;
    	Calendar c1 = Calendar.getInstance();
    	c1.setTimeInMillis(d1.getTime());
    	Calendar c2 = Calendar.getInstance();
    	c2.setTimeInMillis(d2.getTime());
    	
    	return c1.get(Calendar.YEAR)==c2.get(Calendar.YEAR)
    		&& c1.get(Calendar.MONTH)==c2.get(Calendar.MONTH)
    		&& c1.get(Calendar.DAY_OF_MONTH)==c2.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 判断是否d2是d1的后一天
     * @param d1
     * @param d2
     * @return
     */
	public static boolean isContinueDay(Date d1, Date d2) {
		if(d1==null || d2==null)
			return false;
    	if(intervalDay(d1, d2)==1)
    		return true;
    	return false;
	}
	
	/**
	 * 得到没有时间的日期
	 * @param date
	 * @return
	 */
	public static Date truncDate(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}


    public static Date getDateEnd(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        return c.getTime();
    }
	
	/**
	 * 
	 * 得到旬.
	 *
	 * @param input
	 * @return
	 * @author <a href="mailto:wangxin@knet.cn">北京王欣</a>
	 */
	public static String getCnDecade(Date input){
		String day = formatDate(input);
		String decade = day.replaceAll("01日", "上旬").replaceAll("11日", "中旬").replaceAll("21日", "下旬");
		return decade;
	}
	
	public static Date getTodayZero(){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	
	public static Date getTheDayBefore(Date date){
		return new Date(date.getTime()-(long)24*(long)60*(long)60*(long)1000);
	}
	
	public static Date[] getTenDayBefore(){//计算之前一旬的起止时间
		Date[] ret = new Date[2];
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);//0点0分0秒
		int day = c.get(Calendar.DAY_OF_MONTH);
		if(day < 10){//今天处在某月的上旬，起始时间是前一个月的21号，终止时间是本月的1号
			c.set(Calendar.DAY_OF_MONTH, 1);//本月的1号
			ret[1] = new Date(c.getTime().getTime());
			c.setTime(getTheDayBefore(c.getTime()));//往前翻一天，到上一个月
			c.set(Calendar.DAY_OF_MONTH, 21);
			ret[0] = new Date(c.getTime().getTime());
		}else{//
			
			if(10<day && day<=20){//今天处在某月的中旬，起始时间是本月的1号，终止时间是本月的11号
				c.set(Calendar.DAY_OF_MONTH, 1);
				ret[0] = new Date(c.getTime().getTime());
				c.set(Calendar.DAY_OF_MONTH, 11);
				ret[1] = new Date(c.getTime().getTime());
			}else{//今天处在某月的下旬，起始时间是本月的11号，终止时间是本月的21号
				c.set(Calendar.DAY_OF_MONTH, 11);
				ret[0] = new Date(c.getTime().getTime());
				c.set(Calendar.DAY_OF_MONTH, 21);
				ret[1] = new Date(c.getTime().getTime());
			}
		}
		return ret;
	}
	
	public static Date[] getCurrentTenDay(Date input){//计算某个输入时间的当前旬起止时间
		Date[] ret = new Date[2];
		Calendar c = Calendar.getInstance();
		c.setTime(input);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);//0点0分0秒
		int day = c.get(Calendar.DAY_OF_MONTH);
		if(day < 10){//今天处在某月的上旬，起始时间是本月的1号，终止时间是本月的11号
			c.set(Calendar.DAY_OF_MONTH, 1);//本月的1号
			ret[0] = new Date(c.getTime().getTime());			
			c.set(Calendar.DAY_OF_MONTH, 11);
			ret[1] = new Date(c.getTime().getTime());
		}else{//
			
			if(10<day && day<=20){//今天处在某月的中旬，起始时间是本月的11号，终止时间是本月的21号
				c.set(Calendar.DAY_OF_MONTH, 11);
				ret[0] = new Date(c.getTime().getTime());
				c.set(Calendar.DAY_OF_MONTH, 21);
				ret[1] = new Date(c.getTime().getTime());
			}else{//今天处在某月的下旬，起始时间是本月的21号，终止时间是下个月的1号
				c.set(Calendar.DAY_OF_MONTH, 21);
				ret[0] = new Date(c.getTime().getTime());				
				ret[1] = getNextMonthFirst(c.getTime());
			}
		}
		return ret;
	}
	
	public static Date getNextMonthFirst(Date date){		
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);//0点0分0秒
        c.add(Calendar.MONTH,1);//加一个月
        c.set(Calendar.DATE, 1);//把日期设置为当月第一天
        return c.getTime();
    }   
	
	public static Date[] getTheMonthBefore(Date date){//计算之前一旬的起止时间
		Date[] ret = new Date[2];
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);//0点0分0秒
		c.set(Calendar.DAY_OF_MONTH, 1);//本月的1号
		ret[1] = new Date(c.getTime().getTime());
		c.setTime(getTheDayBefore(c.getTime()));//往前翻一天，到上一个月
		c.set(Calendar.DAY_OF_MONTH, 1);//上月的1号
		ret[0] = new Date(c.getTime().getTime());
		return ret;
	}

    //读取两个timestamp之间的时差，月1，日2两种
    //返回endDay - startDay
    public static int compareDate(Date startDay, Date endDay, int stype) {

        if (startDay == null) {
            startDay = DateUtils.now();
        }

        int n = 0;
        String[] u = {"天", "月", "年"};
//        String formatStyle = stype == 1 ? "yyyy-MM" : "yyyy-MM-dd";

        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        try {
            start.setTimeInMillis(startDay.getTime());
            end.setTimeInMillis(endDay.getTime());
        } catch (Exception e3) {
            System.out.println("wrong occured");
        }
        //List list = new ArrayList();
        while (start.getTimeInMillis() < end.getTimeInMillis() || start.getTimeInMillis() < end.getTimeInMillis() + 86400000) { //循环对比，直到相等，n 就是所要的结果
            //list.add(df.format(c1.getTime()));    // 这里可以把间隔的日期存到数组中 打印出来
            n++;
            if (stype == 1) {
                start.add(Calendar.MONTH, 1);          // 比较月份，月份+1
            } else {
                start.add(Calendar.DATE, 1);           // 比较天数，日期+1
            }
        }
        n = n - 1;
        if (stype == 2) {
            n = (int) n / 365;
        }
        System.out.println(startDay + " -- " + endDay + " 相差多少" + u[stype] + ":" + n);
        return n;
    }
}
