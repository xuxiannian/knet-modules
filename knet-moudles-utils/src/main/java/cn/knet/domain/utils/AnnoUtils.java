package cn.knet.domain.utils;


import cn.knet.domain.entity.KnetProductInstanceLog;
import cn.knet.domain.tag.Title;
import cn.knet.domain.vo.Column;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AnnoUtils {
    public AnnoUtils() {
    }

    public static String getTitle(Field field) {
        if (field.isAnnotationPresent(Title.class)) {
            Title ann = (Title)field.getAnnotation(Title.class);
            if (ann != null) {
                return ann.value();
            }
        }

        return null;
    }

    public static String getName(Field field) {
        return field.getName();
    }

    public static Object getValue(Field field, Object o) {
        try {
            field.setAccessible(true);
            return field.get(o);
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static List<Column> getAllColumn(Class<?> c, Object o) {
        List<Column> l = new ArrayList();
        Field[] fs = c.getDeclaredFields();
        Field[] var4 = fs;
        int var5 = fs.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Field f = var4[var6];
            if (!f.getName().equals("serialVersionUID")) {
                Column col = new Column();
                col.setTitle(getTitle(f));
                col.setName(getName(f));
                if (o != null) {
                    col.setValue(getValue(f, o));
                }

                l.add(col);
            }
        }

        return l;
    }

    public static void main(String[] args) {
        Iterator var1 = getAllColumn(KnetProductInstanceLog.class, new KnetProductInstanceLog()).iterator();

        while(var1.hasNext()) {
            Column c = (Column)var1.next();
            System.out.println(c.getName() + "," + c.getTitle() + "," + c.getValue());
        }

    }
}