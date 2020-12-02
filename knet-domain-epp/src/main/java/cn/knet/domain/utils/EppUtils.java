package cn.knet.domain.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;

public class EppUtils {

    public static Vector<String> getVector(String str) {
        return getVector(str.split(","));
    }

    public static <T> Vector<T> getVector(@SuppressWarnings("unchecked") T... s) {
        Vector<T> vec = new Vector<T>();
        if (s != null) {
            for (T t : s) {
                vec.addElement(t);
            }
        }
        return vec;
    }

    public static <T> Vector<T> getVector(Class<T> c, String... s) throws Exception {
        Vector<T> vec = new Vector<T>();
        if (s != null) {
            for (String t : s) {
                vec.addElement(c.getConstructor(String.class).newInstance(t));
            }
        }
        return vec;
    }


    public static Date utcDateFormart(String date) throws ParseException {

        date = date.replace("T", " ");
        date = date.substring(0, 19);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeZone tz = TimeZone.getTimeZone("UTC");

        formatter.setLenient(false);
        formatter.setTimeZone(tz);
        Date utcDate = formatter.parse(date);
        return utcDate;
    }
}
