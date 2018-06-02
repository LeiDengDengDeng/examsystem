package com.exam.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by deng on 2017/11/13.
 */
public class TimeUtil {

    private TimeUtil() {
    }

    /**
     * @param time 形如2017-01-01 00:00:00
     * @return
     */
    public static boolean isEarlierThanCurrentTime(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date timeDate = sdf.parse(time);
        return timeDate.before(new Date());
    }
}
