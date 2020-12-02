package cn.knet.domain.utils;

import java.util.Random;

/**
 * User: zhangbin
 * Date: 2014/9/25 16:43
 * Function:
 */
public class RandomUtils {
	private static String str = "abcdefghijklmnopqrstuvwxyz";
	private static String str2 = "0123456789abcdefghijklmnopqrstuvwxyz";
	private static char[] chars = null;
	private static Random random = new Random();

	public static String getRandom(int size){
		if(chars==null){
			chars = str.toCharArray();
		}
		StringBuffer buffer = new StringBuffer();
		for(int i=0;i<size;i++){
			buffer.append(chars[random.nextInt(chars.length)]);
		}
		return buffer.toString();
	}

	public static String getNumCharRandom(int size){
		chars = str2.toCharArray();

		StringBuffer buffer = new StringBuffer();
		for(int i=0;i<size;i++){
			buffer.append(chars[random.nextInt(chars.length)]);
		}
		return buffer.toString();
	}
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
