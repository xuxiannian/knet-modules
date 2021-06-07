package cn.knet.domain.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class KnetBeanUtils {

    public static Map<String, Object> getChangeList(Object o1, Object o2, String... ex) {
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fs = o1.getClass().getDeclaredFields();
        for (Field f : fs) {
            if (Arrays.asList(ex).contains(f.getName())) continue;
            try {
                Object v1 = null;
                Object v2 = null;
                Field f2 = o2.getClass().getDeclaredField(f.getName());
                f.setAccessible(true);
                f2.setAccessible(true);
                v1 = f.get(o1);
                v2 = f2.get(o2);
                if (v1 == null) {
                    continue;
                }
                if (!v1.equals(v2)) {
                    map.put(f.getName(), v1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public static void setField(Object o, String k, Object v) {

        try {
            Field fs = o.getClass().getDeclaredField(k);
            fs.setAccessible(true);
            fs.set(o, v);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
