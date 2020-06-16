package com.hades.example.java.lib;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateUtil {

    /**
     * System.currentTimeMillis()
     * https://blog.csdn.net/weixin_41926301/article/details/80319375
     * https://www.cnblogs.com/denyungap/p/7525449/html
     * https://www.cnblogs.com/dengyungao/p/7525449.html
     */
    /**
     * @param timeStart yyyy-MM-dd HH:mm:ss:SSS e.g., "2019-03-07 10:08:00:316"
     * @param timeEnd   yyyy-MM-dd HH:mm:ss:SSS e.g., "2019-03-07 10:18:47:081"
     * @return Dif value is HH:MM:SS:MS format. e.g., 0h:10m:46s:765ms
     */
    public String compare(String timeStart, String timeEnd) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS", Locale.US);
            long t1 = sdf.parse(timeStart).getTime();
            long t2 = sdf.parse(timeEnd).getTime();
            return compare(t1, t2);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new UnsupportedOperationException("time is unsupported format, should use yyyy-MM-dd HH:mm:ss:SSS");
        }
    }

    /**
     * @param ts1 System.currentTimeMillis()
     * @param ts2 System.currentTimeMillis()
     * @return Dif value is HH:MM:SS:MS format
     */
    public String compare(long ts1, long ts2) {
        if (ts1 > ts2) {
            throw new UnsupportedOperationException("ts1 should <= ts2");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS" , Locale.US);
        System.out.println(sdf.format(ts1) + "," + sdf.format(ts2));

        long t3 = ts2 - ts1;
        long ms_hour = 60 * 60 * 1000;
        long ms_minute = 60 * 1000;
        long ms_s = 1000;

        int h = (int) (t3 / ms_hour);
        t3 = t3 - h * ms_hour;

        int m = (int) (t3 / ms_minute);
        t3 = t3 - m * ms_minute;

        int s = (int) (t3 / ms_s);
        t3 = t3 - s * ms_s;

        String duringTime = h + "h:" + m + "m:" + s + "s:" + t3 + "ms";
        System.out.println(duringTime);
        return duringTime;
    }
}
