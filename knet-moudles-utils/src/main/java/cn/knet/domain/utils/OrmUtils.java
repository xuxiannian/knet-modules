package cn.knet.domain.utils;

import org.apache.commons.lang.StringUtils;

import java.util.Date;

public class OrmUtils {


    public static Date[] sToDr(String str, String split, String parttern) {
        if (StringUtils.isBlank(str)) return null;
        Date[] dates = new Date[2];
        String[] ds = str.split(split);
        dates[0] = (StringUtils.isNotBlank(ds[0])) ? DateUtil.StringToDate(ds[0], parttern) : null;
        if (ds.length == 2 && StringUtils.isNotBlank(ds[1]))
            dates[1] = DateUtil.StringToDate(ds[1], parttern);
        return dates;
    }

    public static Date[] sToDr(String str, String split) {
        return sToDr(str, split, "yyyy-MM-dd");
    }

    public static Date[] sToDr(String str) {
        return sToDr(str, " - ", "yyyy-MM-dd");
    }

    public static void main(String[] args) {
        Date[] a = OrmUtils.sToDr("20200700 - 2020-07-15", " - ", "yyyy-MM-dd");
        new Date();
    }

}
