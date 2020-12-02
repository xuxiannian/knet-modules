package cn.knet.domain.utils;

import java.util.Random;

public class StringUtil {
    public static String randomString(int length, String reserve) {
        Random ran = new Random();
        String str = "";
        while (str.length() < length) {
            int value = ran.nextInt(36);
            char a = '0';
            boolean isreserved = false;
            if (value < 10) {
                a = (char) (value + 48);
            } else {
                a = (char) (value + 97 - 10);
            }
            for (int i = 0; i < reserve.length(); i++) {
                if (a == reserve.charAt(i)) {
                    isreserved = true;
                    break;
                }
            }
            if (!isreserved) {
                str += a;
            }
        }
        return str;
    }
}
