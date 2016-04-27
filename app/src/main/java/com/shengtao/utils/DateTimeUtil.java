package com.shengtao.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author cjl
 * @version V1.0
 * @Title: DateTimeUtil.java
 * @Package com.framework.util
 * @Description: 日期时间格式转换工具
 * @date 2014年12月26日 下午11:16:34
 */

@SuppressLint("SimpleDateFormat")
public class DateTimeUtil {

    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat(
            "yyyy-MM-dd");

    public static final SimpleDateFormat TIME_FORMAT_DATE = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    public static final SimpleDateFormat YMDH_FORMAT_DATE = new SimpleDateFormat(
            "yyyyMMddHH");

    public static final SimpleDateFormat MINUTE_FORMAT_DATE = new SimpleDateFormat(
            "yyyyMMddHHmm");

    /**
     * @return String
     * @Title: getMinute
     * @Description: 获取到分的时间
     */
    public static String getMinute() {
        return MINUTE_FORMAT_DATE.format(new Date(System.currentTimeMillis()));
    }

    public static String getTime(long timeInMillis) {
        if (timeInMillis == 0)
            return "";
        return TIME_FORMAT_DATE.format(new Date(timeInMillis));
    }

    /**
     * long time to string
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @param timeInMillis
     * @return
     */
    public static String getDate(long timeInMillis) {
        if (timeInMillis == 0)
            return "";
        return getTime(timeInMillis, DATE_FORMAT_DATE);
    }

    /**
     * @return int
     * @Title: getCurrentYear
     * @Description: 获取当前的年份
     */
    public static int getCurrentYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR); // 获取当前年份
    }

    /**
     * @return String
     * @Title: getYearMonthDayHour
     * @Description: 获取年月日时
     */
    public static String getYearMonthDayHour() {
        return getTime(System.currentTimeMillis(), YMDH_FORMAT_DATE);
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getDate(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    /**
     * 格式化日期 从左至右分别为-年-月-日 时:分:秒.毫秒 {0:yyyy-MM-dd HH:mm:ss.fff}:使用24小时制格式化日期
     * {0:yyyy-MM-dd hh:mm:ss.fff}:使用12小时制格式化日期
     *
     * @param datetime
     * @param format
     * @return
     */
    public static String formatDate(String datetime, String format) {
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            Date date = formater.parse(datetime);
            return formater.format(date);
        } catch (ParseException e) {
            return datetime;
        }
    }

    // *************************************************************************

    /**
     * 【】(获取格式化的时和分)
     *
     * @param timeInMillis
     * @return
     */
    // *************************************************************************
    public static String getFormatHourMinute(long timeInMillis) {
        try {
            String formatTime = "";
            DateFormat df = new SimpleDateFormat("HH:mm");
            Date date = new Date(timeInMillis);
            String time = df.format(date);
            int hour = date.getHours();
            if (hour < 11) {
                formatTime = "上午" + time;
            } else if (hour > 13) {
                formatTime = "下午" + time;
            } else {
                formatTime = "中午" + time;
            }
            return formatTime;
        } catch (Exception e) {
            // TODO: handle exception
            return timeInMillis + "";
        }
    }

    // *************************************************************************

    /**
     * 【】(获取倒计时的时间)
     *
     * @param second
     * @return
     */
    // *************************************************************************
    public static String getCountDownTime(long second) {
        int hour = (int) (second / 3600);
        int minute = (int) ((second - 3600 * hour) / 60);
        int sec = (int) (second % 60);
        String shour = hour >= 10 ? hour + "" : "0" + hour;
        String sminute = minute >= 10 ? minute + "" : "0" + minute;
        String sSec = sec >= 10 ? sec + "" : "0" + sec;
        String time = shour + ":" + sminute + ":" + sSec;
        return time;
    }

    /**
     * 解析时间，获取秒数
     */
    public static long getSecond(String time) {
        long seconds = 0;
        Date before;
        Date now = new Date();
        try {
            before = DATE_FORMAT_DATE.parse(time);
            seconds = now.getTime() - before.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return seconds;
    }

    /**
     * 跟当前时间相差多少时间
     *
     * @param starttime 开始日期
     * @return
     */
    public static String dateDistance(long starttime) {

        Date endDate = new Date();// 获取当前日期对象
        Date startDate = new Date(starttime);// 获取当前日期对象

        if (startDate == null || endDate == null) {
            return null;
        }
        long timeLong = (endDate.getTime() - startDate.getTime()) / 1000;
        // 之后
        if (timeLong < 0) {
            timeLong = (startDate.getTime() - endDate.getTime()) / 1000;

            if (timeLong < 60 * 60) {
                timeLong = timeLong / 60;
                if (timeLong < 10)
                    return "0" + timeLong + "分后";
                else
                    return timeLong + "分后";
            } else if (timeLong < 60 * 60 * 24) {
                timeLong = timeLong / 60 / 60;

                if (timeLong < 10)
                    return "0" + timeLong + "时后";
                else
                    return timeLong + "时后";
            }
            // 超过24小时的直接显示时间
            else if (timeLong < 60 * 60 * 24 * 7) {
                timeLong = timeLong / 60 / 60 / 24;
                return timeLong + "天后";
            } else {
                timeLong = timeLong / 60 / 60 / 24;
                return timeLong + "天后";
            }
        }
        // 之前
        else {
            if (timeLong < 60 * 60) {
                http:
                // tieba.baidu.com/f?kw=Arrays.sort&fr=wwwt timeLong =
                // timeLong / 60;
                if (timeLong < 10)
                    return "0" + timeLong + "分前";
                else
                    return timeLong + "分前";
            } else if (timeLong < 60 * 60 * 24) {
                timeLong = timeLong / 60 / 60;

                if (timeLong < 10)
                    return "0" + timeLong + "时前";
                else
                    return timeLong + "时前";
            }
            // 超过24小时的直接显示时间
            else if (timeLong < 60 * 60 * 24 * 7) {
                timeLong = timeLong / 60 / 60 / 24;
                return timeLong + "天前";
            } else {
                timeLong = timeLong / 60 / 60 / 24 / 7;
                return timeLong + "星期前";
            }
        }

    }

    /**
     * @param time           long型时间
     * @param dateTimeFormat 时间格式 如 yyyy年MM月dd日
     * @return 字符串类型时间
     */
    public static String getDateTimeByForMat(String time, String dateTimeFormat) {
        if (time != null && !"null".equals(time)) {
            SimpleDateFormat format = new SimpleDateFormat(dateTimeFormat);
            return format.format(new Date(Long.parseLong(time) * 1000));
        }
        return "";
    }

    /**
     * @param year      年 2015
     * @param month     月
     * @param day       日
     * @param hourOfDay 小时
     * @param minute    分
     * @return 毫秒数
     */
    public static long getTimeInMillis(int year, int month, int day,
                                       int hourOfDay, int minute) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(year, month - 1, day, hourOfDay, minute);
        return mCalendar.getTimeInMillis();
    }

    /**
     * @param date 格式为2015-06-11的字符串
     * @return 字符串数组 下表为0是年 1是月 2为日
     */
    public static String[] getYearMonthDay(String date) {
        // mMiData.setValue("2015-06-11");
        // mMiTime.setValue("11:00-13:00");
        return date.split("-");
    }

    /**
     * @param time 格式为11:00-13:00的时间段
     * @return 字符串数组 下标为零的是第一个小时数 下表为1的是第二个小时数
     */
    public static String[] getHourMinute(String time) {
        String[] hm = time.split("-");
        String[] hour1 = hm[0].split(":");
        String[] hour2 = hm[1].split(":");
        String[] hours = new String[]{hour1[0], hour2[0]};
        return hours;
    }

    /**
     * @param fromTime 游记发布的时间
     * @return 日期，几天前，几小时前，几分钟前，几秒前
     */
    public static String dateTimeDistance(String fromTime) {
        long fromTimel = Long.parseLong(fromTime) * 1000;
        long middleTime = System.currentTimeMillis() - Long.parseLong(fromTime);
        Calendar middleCalendar = Calendar.getInstance();
        middleCalendar.setTime(new Date(middleTime));
        // 过去时间的几年，几月，几日，几时，几分，几秒
        int fromY = middleCalendar.get(Calendar.YEAR) - 1970;
        int fromM = middleCalendar.get(Calendar.MONTH);
        int fromD = middleCalendar.get(Calendar.DAY_OF_MONTH) - 1;
        int fromH = middleCalendar.get(Calendar.HOUR_OF_DAY) - 8;
        int fromm = middleCalendar.get(Calendar.MINUTE);
        int fromS = middleCalendar.get(Calendar.SECOND);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (fromY != 0) {
            return format.format(new Date(fromTimel));
        }
        if (fromM != 0) {
            return format.format(new Date(fromTimel));
        }
        if (fromD != 0) {
            switch (fromD) {
                case 1:
                    return "昨天";
                case 2:
                    return "前天";
                default:
                    return fromD + "天前";
            }
        }
        if (fromH != 0) {
            return fromH + "小时前";
        }
        if (fromm != 0) {
            return fromm + "分钟前";
        }
        if (fromS != 0) {
            return fromS + "秒前";
        } else
            return "刚刚";
        // Calendar curCalendar = Calendar.getInstance();
        // Date fromDate = new Date(Long.parseLong(fromTime));
        //
        // Calendar fromCalendar = Calendar.getInstance();
        // fromCalendar.setTime(fromDate);
        //
        // // 当前时间的几年，几月，几日，几时，几分，几秒
        // int curY = curCalendar.get(Calendar.YEAR);
        // int curM = curCalendar.get(Calendar.MONTH)+1;
        // int curD = curCalendar.get(Calendar.DATE);
        // int curH = curCalendar.get(Calendar.HOUR_OF_DAY);
        // int curm = curCalendar.get(Calendar.MINUTE);
        // int curS = curCalendar.get(Calendar.SECOND);
        //
        // // 过去时间的几年，几月，几日，几时，几分，几秒
        // int fromY = fromCalendar.get(Calendar.YEAR);
        // int fromM = fromCalendar.get(Calendar.MONTH)+1;
        // int fromD = fromCalendar.get(Calendar.DATE);
        // int fromH = fromCalendar.get(Calendar.HOUR_OF_DAY);
        // int fromm = fromCalendar.get(Calendar.MINUTE);
        // int fromS = fromCalendar.get(Calendar.SECOND);
        // String date = fromY + "-" + fromM + "-" + fromD;
        // if (curY != fromY) {
        // return date;
        // }
        // if (curM != fromM) {
        // return date;
        // }
        // if (curD != fromD) {
        // switch (curD - fromD) {
        // case 1:
        // return "昨天";
        // case 2:
        // return "前天";
        // default:
        // return (curD - fromD) + "天前";
        // }
        // }
        // if (curH != fromH) {
        // return (curH - fromH) + "小时前";
        // }
        // if (curm != fromm) {
        // return (curm - fromm) + "分钟前";
        // }
        // if (curS != fromS) {
        // return (curS - fromS) + "秒前";
        // }
        // return null;

    }

    public static String getInterval(String createtime) { //传入的时间格式必须类似于2012-8-21 17:53:20这样的格式
        String interval = null;
        createtime = getDateTimeByForMat(createtime, "yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date d1 = (Date) sd.parse(createtime, pos);

        //用现在距离1970年的时间间隔new Date().getTime()减去以前的时间距离1970年的时间间隔d1.getTime()得出的就是以前的时间与现在时间的时间间隔
        long time = new Date().getTime() - d1.getTime();// 得出的时间间隔是毫秒

        if (time / 1000 < 10 && time / 1000 >= 0) {
            //如果时间间隔小于10秒则显示“刚刚”time/10得出的时间间隔的单位是秒
            interval = "刚刚";

        } else if (time / 3600000 < 24 && time / 3600000 >= 0) {
            //如果时间间隔小于24小时则显示多少小时前
            int h = (int) (time / 3600000);//得出的时间间隔的单位是小时
            interval = h + "小时前";

        } else if (time / 60000 < 60 && time / 60000 > 0) {
            //如果时间间隔小于60分钟则显示多少分钟前
            int m = (int) ((time % 3600000) / 60000);//得出的时间间隔的单位是分钟
            interval = m + "分钟前";

        } else if (time / 1000 < 60 && time / 1000 > 0) {
            //如果时间间隔小于60秒则显示多少秒前
            int se = (int) ((time % 60000) / 1000);
            interval = se + "秒前";

        } else {
            //大于24小时，则显示正常的时间，但是不显示秒
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            ParsePosition pos2 = new ParsePosition(0);
            Date d2 = (Date) sdf.parse(createtime, pos2);

            interval = sdf.format(d2);
        }
        return interval;
    }

    public static String formatTime(String timestamp, String format) {
        String time = timestamp2Time(timestamp);//yyyy-MM-dd HH:mm:ss
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
            String formatTime = new SimpleDateFormat(format).format(date);
            return formatTime;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将时间戳转给yyyy-MM-dd HH:mm:ss格式
     *
     * @param timestamp
     * @return
     */
    public static String timestamp2Time(String timestamp) {
        try {

            if (timestamp != null) {
                long time = Long.valueOf(timestamp);
                Date d = new Date(time * 1000);
                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);
                return date;
            }
        } catch (Exception e) {
            //
            e.printStackTrace();
        }

        return "";
    }


    /**
     * 将时间戳转给yyyy-MM-dd格式
     *
     * @param timestamp
     * @return
     */
    public static String timestamp3Time(String timestamp) {
        try {

            if (timestamp != null) {
                long time = Long.valueOf(timestamp);
                Date d = new Date(time );
                String date = new SimpleDateFormat("yyyy-MM-dd").format(d);
                return date;
            }
        } catch (Exception e) {
            //
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 将时间戳转给yyyy-MM-dd-HH格式
     *
     * @param timestamp
     * type:
     * 1 yyyy年MM月dd日HH时
     * 2 yyyy年MM月dd日
     * @return
     */
    public static String timestamp4Time(String timestamp,String type) {
        try {

            if (timestamp != null) {
                long time = Long.valueOf(timestamp);
                Date d = new Date(time );
                String date = "1".equals(type)?new SimpleDateFormat("yyyy年MM月dd日HH时").format(d):new SimpleDateFormat("yyyy年MM月dd日").format(d);
                return date;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String timestamp5Time(String timestamp){
        try {

            if (timestamp != null) {
                long time = Long.valueOf(timestamp);
                Date d = new Date(time );
                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(d);
                return date;
            }
        } catch (Exception e) {
            //
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 计算年龄
     *
     * @param birthday timestamp
     * @return
     */
    public static int getAge(String birthday) {

        String birth = DateTimeUtil.formatDate(timestamp2Time(birthday), "yyyy");
        String current = new SimpleDateFormat("yyyy").format(new Date());
        int age = 0;
        if (!"".equals(birth))
            age = Integer.valueOf(current) - Integer.valueOf(birth);

        return age;
    }

    public static long getTimeStamp(String date, String format) {
        try {
            Date d = new SimpleDateFormat(format).parse(date);
            return d.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    private final static long minute = 60;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天


    /**
     * 返回文字描述的日期(毫秒值转刚刚)
     *
     * @param date
     * @return
     */
    public static String getTimeFormatText(long date) {

        long currentDate = new Date().getTime();
        long diff = currentDate / 1000 - date;
        long r = 0;

        if (diff > day) {
            r = (diff / day);
            if (r >= 30) {
                return "以前";
            }
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }
}