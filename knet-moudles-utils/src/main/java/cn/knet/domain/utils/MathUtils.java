package cn.knet.domain.utils;

import java.math.BigDecimal;

public class MathUtils {

    public static double scale(double d) {
        return new BigDecimal(d).setScale(2, 4).doubleValue();
    }

    public static float scale(float d) {
        return new BigDecimal(d).setScale(2, 4).floatValue();
    }
}
