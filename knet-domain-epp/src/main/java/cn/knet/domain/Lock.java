package cn.knet.domain;

public class Lock {
    private static Object LOCK = new Object();

    public static Object getLOCK() {
        return LOCK;
    }

    public static void setLOCK(Object lOCK) {
        LOCK = lOCK;
    }
}
