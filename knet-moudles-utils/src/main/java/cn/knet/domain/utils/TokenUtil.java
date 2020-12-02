package cn.knet.domain.utils;

import java.util.Date;

public class TokenUtil {

    private static String addByte="daihuibin";

    private static int defaultMin=10;

    public static String getToken(String... args){
        return getToken(defaultMin,args);
    }

    public static String getToken(int minute,String... args){

        String joinStr="";
        for (String str:args) {
            joinStr = joinStr+str;
        }
        try {
            return MD5Util.enCryptMD532(addByte+joinStr+getTime(new Date(),minute));
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isToken(String token,String... args){
        return isToken(defaultMin,token,args);
    }
    public static boolean isToken(int minute,String token,String... args){
        Date date = new Date();
        Long time1 = getTime(date,minute);
        Long time2 = getTime(DateUtil.addMinutes(date,-minute),minute);

        String joinStr="";
        for (String str:args) {
            joinStr = joinStr+str;
        }

        try {
            if(token.equals(MD5Util.enCryptMD532(addByte+joinStr+time1))||token.equals(MD5Util.enCryptMD532(addByte+joinStr+time2))){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }


    public static Long getTime(Date date,int minute){
        Long time=date.getTime()/1000/60;
        int n = Math.toIntExact(time % minute);
        return time-n;
    }


    public static void main(String[] args) {
        String code= RandomUtils.getNumCharRandom(10);
        String token= TokenUtil.getToken("ooxx",code,"knet");
        System.out.println(code+"   "+token);
        for (int i = 0; i <100000 ; i++) {
            try {
                Thread.sleep(10000);

                System.out.println(i+"  "+TokenUtil.isToken(token,"ooxx",code,"knet"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
