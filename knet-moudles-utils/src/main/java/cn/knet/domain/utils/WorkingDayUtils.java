package cn.knet.domain.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WorkingDayUtils {

    public static int getWorkingDayThisMonth() {
        Calendar cal = Calendar.getInstance();
        int lastDay = cal.getActualMaximum(Calendar.DATE);
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        return getWorkingDayNum(new Date(), cal.getTime());
    }

    public static int getWorkingDayThisSeason() {
        Calendar cal = Calendar.getInstance();
        int m = cal.get(Calendar.MONTH);
        int lm;
        switch (m) {
            case 0:
            case 1:
            case 2:
                lm = 2;
                break;
            case 3:
            case 4:
            case 5:
                lm = 5;
                break;
            case 6:
            case 7:
            case 8:
                lm = 8;
                break;
            default:
                lm = 11;

        }
        cal.set(Calendar.MONTH, lm);
        int lastDay = cal.getActualMaximum(Calendar.DATE);
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        return getWorkingDayNum(new Date(), cal.getTime());
    }

    public static int getWorkingDayThisYear() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 31);
        cal.set(Calendar.MONTH, 11);
        return getWorkingDayNum(new Date(), cal.getTime());
    }


    public static int getWorkingDayNum(String sd, String ed) {
        try {
            return getWorkingDayNum(DateUtil.convert2Date(sd, "yyyy-MM-dd"), DateUtil.convert2Date(ed, "yyyy-MM-dd"));

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }


    public static int getWorkingDayNum(Date sd, Date ed) {
        int num = 0;
        Calendar s = Calendar.getInstance();
        s.setTime(sd);
        s.set(Calendar.HOUR_OF_DAY, 0);
        s.set(Calendar.MINUTE, 0);
        s.set(Calendar.SECOND, 0);
        s.set(Calendar.MILLISECOND, 0);

        Calendar e = Calendar.getInstance();
        e.setTime(ed);
        s.set(Calendar.HOUR_OF_DAY, 0);
        s.set(Calendar.MINUTE, 0);
        s.set(Calendar.SECOND, 0);
        s.set(Calendar.MILLISECOND, 0);

        while (s.compareTo(e) <= 0) {
            if (isWorkDay(s)) num++;
            s.add(Calendar.DAY_OF_MONTH, 1);
        }
        return num;
    }

    public static boolean isWorkDay(Calendar cal) {
        Map<String, Boolean> m = getSpecialDays();
        Boolean f = m.get(DateUtil.convert2String(cal.getTime(), "yyyy-MM-dd"));
        if (f != null) return f;
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return false;
        } else {
            return true;
        }
    }

    public static Map<String, Boolean> getSpecialDays() {
        return new HashMap<String, Boolean>() {{
            put("2020-10-01", false);
            put("2020-10-02", false);
        }};

    }


    public static void main(String[] args) {
        new WorkingDayUtils().getWorkingDayThisYear();
    }
};
