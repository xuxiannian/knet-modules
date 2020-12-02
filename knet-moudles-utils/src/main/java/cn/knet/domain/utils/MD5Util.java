package cn.knet.domain.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	/*
	 * public static byte[] encryptMD5(byte[] data) throws Exception {
	 * MessageDigest md5 = MessageDigest.getInstance("MD5"); md5.update(data);
	 * return md5.digest(); }
	 */

	public static String enCryptMD532(String Str) throws Exception {
		return code(Str, 32, "UTF-8");
	}

	public static String enCryptMD532(String Str, String charsetName) throws Exception {
		return code(Str, 32, charsetName);
	}

	public static String enCryptMD516(String Str) throws Exception {
		return code(Str, 16, "UTF-8");
	}

	public static String enCryptMD516(String Str, String charsetName) throws Exception {
		return code(Str, 16, charsetName);
	}

	private final static char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E','F' };

	private static String bytesToHex(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		int t;
		for (int i = 0; i < 16; i++) {
			t = bytes[i];
			if (t < 0)
				t += 256;
			sb.append(hexDigits[(t >>> 4)]);
			sb.append(hexDigits[(t % 16)]);
		}
		return sb.toString();
	}

	private static String code(String input, int bit, String charsetName) throws Exception {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (bit == 16)
				return bytesToHex(md.digest(input.getBytes(charsetName))).substring(8, 24);
			return bytesToHex(md.digest(input.getBytes(charsetName)));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new Exception("Could not found MD5 algorithm.", e);
		}
	}
}
