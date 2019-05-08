package com.tenfine.napoleon.framework.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ByteUtil {
	
	/**
	 * 转换经纬度字符串为10位ASCII
	 */
	public static byte[] convertAddressString(String address) {
		if(address == null) {
			return "\0\0\0\0\0\0\0\0\0\0".getBytes();
		}
		//监控地址长度为10位长度
		byte[] addressByte = address.getBytes();
		if(addressByte.length > 10) {
			addressByte = subBytes(addressByte, 0, 10);
		}
		if(addressByte.length < 10) {
			int fillLength = 10 - addressByte.length;
			for (int i = 0; i < fillLength; i++) {
				addressByte = concatAll(addressByte, "\0".getBytes());
			}
		}
		return addressByte;
	}
	
	public static byte[] intTo1Bytes(int data) {
		byte[] bytes = new byte[1];
		bytes[0] = (byte) (data & 0xff);
		return bytes;
	}
	public static int byteTo1Int(byte[] bytes) {
		return (0xff & bytes[0]);
	}

	public static byte[] intTo2Bytes(int data) {
		byte[] bytes = new byte[2];
		bytes[0] = (byte) (data & 0xff);
		bytes[1] = (byte) ((data & 0xff00) >> 8);
		return bytes;
	}
	public static int byteTo2Int(byte[] bytes) {
		return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8));
	}

	public static byte[] intTo4Bytes(int data) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (data & 0xff);
		bytes[1] = (byte) ((data & 0xff00) >> 8);
		bytes[2] = (byte) ((data & 0xff0000) >> 16);
		bytes[3] = (byte) ((data & 0xff000000) >> 24);
		return bytes;
	}
	public static int byteTo4Int(byte[] bytes) {
		return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)) | (0xff0000 & (bytes[2] << 16)) | (0xff000000 & (bytes[3] << 24));
	}

    public static String stringToAscii(String value){  
        StringBuffer sbu = new StringBuffer();  
        char[] chars = value.toCharArray();   
        for (int i = 0; i < chars.length; i++) {  
                sbu.append((int)chars[i]);  
        }  
        return sbu.toString();  
    }

	public static byte[] file2byte(File file) {
		byte[] buffer = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	public static void byte2File(byte[] buf, String filePath, String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			File dir = new File(filePath);
			if (!dir.exists() && dir.isDirectory()) {
				dir.mkdirs();
			}
			file = new File(filePath + File.separator + fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(buf);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	

	public static byte[] subBytes(byte[] src, int begin, int count) {
		byte[] bs = new byte[count];
		System.arraycopy(src, begin, bs, 0, count);
		return bs;
	}

	public static byte[] concatAll(byte[] thsnB, byte[]... secB) {
		int totalLength = thsnB.length;
		for (byte[] array : secB) {
			totalLength += array.length;
		}
		byte[] result = Arrays.copyOf(thsnB, totalLength);
		int offset = thsnB.length;
		for (byte[] array : secB) {
			System.arraycopy(array, 0, result, offset, array.length);
			offset += array.length;
		}
		return result;
	}

	public static byte[] xor(byte[] body) {
		byte xor = body[0];
		for (int i = 1; i < body.length; i++) {
			xor = (byte) (xor ^ body[i]);
		}
		body[body.length - 1] = xor;
		return body;
	}

	/**
	 * bytes字符串转换为Byte值
	 * 
	 * @param String
	 *            src Byte字符串，每个Byte之间没有分隔符
	 * @return byte[]
	 */
	public static byte[] hexStr2Bytes(String src) {
		int m = 0, n = 0;
		int l = src.length() / 2;
		byte[] ret = new byte[l];
		for (int i = 0; i < l; i++) {
			m = i * 2 + 1;
			n = m + 1;
			ret[i] = Byte.decode("0x" + src.substring(i * 2, m) + src.substring(m, n));
		}
		return ret;
	}

	/**
	 * bytes转换成十六进制字符串
	 * 
	 * @param byte[]
	 *            b byte数组
	 * @return String 每个Byte值之间空格分隔
	 */
	public static String byte2HexStr(byte[] b) {
		String stmp = "";
		StringBuilder sb = new StringBuilder("");
		for (int n = 0; n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0xFF);
			sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
			// sb.append(" ");
		}
		return sb.toString().toUpperCase().trim();
	}
	
	public static void main(String[] args) {
		System.out.println(ByteUtil.stringToAscii("0"));
	}
}
