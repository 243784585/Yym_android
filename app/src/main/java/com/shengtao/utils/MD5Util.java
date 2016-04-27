package com.shengtao.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Title: MD5Util.java
 * @Package com.framework.util
 * @Description: md5工具
 * @author cjl
 * @date 2014年12月25日 下午4:57:12
 * @version V1.0
 */

public class MD5Util {

//	public final static String MD5(String s) {
//		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
//				'a', 'b', 'c', 'd', 'e', 'f' };
//		try {
//			byte[] btInput = s.getBytes();
//			// 获得MD5摘要算法的 MessageDigest 对象
//			MessageDigest mdInst = MessageDigest.getInstance("MD5");
//			// 使用指定的字节更新摘要
//			mdInst.update(btInput);
//			// 获得密文
//			byte[] md = mdInst.digest();
//			// 把密文转换成十六进制的字符串形式
//			int j = md.length;
//			char str[] = new char[j * 2];
//			int k = 0;
//			for (int i = 0; i < j; i++) {
//				byte byte0 = md[i];
//				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
//				str[k++] = hexDigits[byte0 & 0xf];
//			}
//			return new String(str);
//		} catch (Exception e) {
//			return null;
//		}
//	}

	/**
	 * 后台给的md5加密方式
	 */
		/* md5加密方法
         */
		public static String EncoderByMd5(String s) {
			char hexDigits[] = { '0', '1', '2', '3', '4',
					'5', '6', '7', '8', '9',
					'A', 'B', 'C', 'D', 'E', 'F' };
			try {
				byte[] btInput = s.getBytes();
				//获得MD5摘要算法的 MessageDigest 对象
				MessageDigest mdInst = MessageDigest.getInstance("MD5");
				//使用指定的字节更新摘要
				mdInst.update(btInput);
				//获得密文
				byte[] md = mdInst.digest();
				//把密文转换成十六进制的字符串形式
				int j = md.length;
				char str[] = new char[j * 2];
				int k = 0;
				for (int i = 0; i < j; i++) {
					byte byte0 = md[i];
					str[k++] = hexDigits[byte0 >>> 4 & 0xf];
					str[k++] = hexDigits[byte0 & 0xf];
				}
				return new String(str);
			}
			catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}


	// MD5変換
	public static String Md5(String str) {
		if (str != null && !str.equals("")) {
			try {
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
						'9', 'a', 'b', 'c', 'd', 'e', 'f' };
				byte[] md5Byte = md5.digest(str.getBytes("UTF8"));
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < md5Byte.length; i++) {
					sb.append(HEX[(int) (md5Byte[i] & 0xff) / 16]);
					sb.append(HEX[(int) (md5Byte[i] & 0xff) % 16]);
				}
				str = sb.toString();
			} catch (NoSuchAlgorithmException e) {
			} catch (Exception e) {
			}
		}
		return str;
	}

	public static String getMd5Value(String sSecret) {
		try {
			MessageDigest bmd5 = MessageDigest.getInstance("MD5");
			bmd5.update(sSecret.getBytes());
			int i;
			StringBuffer buf = new StringBuffer();
			byte[] b = bmd5.digest();
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取MD5
	 * 
	 * @param input
	 *            字符串
	 * @return
	 */
	@Deprecated
	public static String getMd5(String input) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(input.getBytes());
			return toHexString(md5.digest());
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 获取MD5
	 * 
	 * @param input
	 *            字符串
	 * @param encode
	 *            编码
	 * @return
	 */
	public static String getMd5(String input, String encode) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(input.getBytes(encode));
			return toHexString(md5.digest());
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * byte数组转成十六进制字符串
	 * 
	 * @param b
	 * @return
	 * @throws Exception
	 */
	public static String toHexString(byte[] b) throws Exception {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
			sb.append(hexChar[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	private static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private static final String HEX_NUMS_STR = "0123456789ABCDEF";

	/**
	 * 将16进制字符串转换成字节数组
	 * 
	 * @param hex
	 * @return
	 */
	public static byte[] hexStringToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] hexChars = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (HEX_NUMS_STR.indexOf(hexChars[pos]) << 4 | HEX_NUMS_STR
					.indexOf(hexChars[pos + 1]));
		}
		return result;
	}

}
