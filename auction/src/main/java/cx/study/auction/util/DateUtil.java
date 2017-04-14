package cx.study.auction.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * Created by cheng.xiao on 2017/3/25.
 */

public class DateUtil {
    public static Date getDateByString(String str){
        SimpleDateFormat format = new SimpleDateFormat("MMM d, yyyy h:m:s aa", Locale.ENGLISH);
        try {
            return format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDateString(Date date){
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINESE);
        return format.format(date);
    }

    public static String getDateTimeString(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
        return format.format(date);
    }
}
