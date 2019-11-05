package yun.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by peng on 2018-3-26.
 */
public class DateUtils {


    /**
     * 得到本月第一天的日期
     * @Methods Name getFirstDayOfCurrentMonth
     * @return Date
     */
    public static Date getFirstDayOfCurrentMonth() {
        Calendar c = Calendar.getInstance();   // this takes current date
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        return c.getTime();
    }

    /**
     * 得到本月最后一天的日期
     * @Methods Name getLastDayOfMonth
     * @return Date
     */
    public static Date getLastDayOfCurrentMonth()   {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE,59);
        c.set(Calendar.SECOND, 59);
        return c.getTime();
    }
}
