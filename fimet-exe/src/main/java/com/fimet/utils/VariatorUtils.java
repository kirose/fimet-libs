package com.fimet.utils;

import java.util.ArrayList;
import java.util.List;

import com.fimet.FimetException;

public class VariatorUtils {
	private static final String CHARSET_A_TO_Z_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String CHARSET_A_TO_Z_LOWER = "abcdefghijklmnopqrstuvwxyz";
	private static final String CHARSET_0_TO_9 = "0123456789";
	private static final String RANGE_A_TO_Z_UPPER = "A-Z";
	private static final String RANGE_A_TO_Z_LOWER = "a-z";
	private static final String RANGE_0_TO_9 = "0-9";
	public static void main(String[] args) {
		String[] vals = generateRange(100L, 120L, 12L, '0', "A-Za-z0-9", null,null);
		for (String v : vals) {
			System.out.println(v);
		}
		vals = generateRange(5L, 40L, 6L, '.', "0-9", "xx", "ZZ");
		for (String v : vals) {
			System.out.println(v);
		}
	}
	public static String[] generateRange(Long start, Long end, Long length, char pad, String charset, String preappend, String append) {
		Args.notNull("length", length);
		Args.notNull("start", start);
		Args.notNull("end", end);
		charset = parseCharset(charset);
		if (preappend==null) {
			preappend = "";
		}
		int lnp = preappend.length();
		if (append==null) {
			append = "";
		}
		int lna = append.length();
		if (lnp + lna > length) {
			throw new FimetException("Invalid range, '"+preappend+"' + '"+append+"' excceds length "+length);
		}
		List<String> values = new ArrayList<>();
		if (lnp + lna == length) {
			values.add(preappend+append);
			return values.toArray(new String[values.size()]);
		}
		if (end > Math.pow(charset.length(), length)) {
			end = (long)Math.pow(charset.length(), length);
		}
		int valueLn = (int)(length - lnp - lna);
		int index;
		int module = charset.length();
		long seq;
		char[] value;
		for (long j = start; j < end; j++) {
			value = new char[valueLn];
			seq = j;
			int i = valueLn-1;
			do {
				index = (int)(seq%module);
				value[i--] = charset.charAt(index);
				seq = seq/module;
			} while (seq!=0 && i!=-1);
			for (; i > -1 ; i--) {
				value[i] = pad;	
			}
			values.add(preappend+new String(value)+append);
		}
		return values.toArray(new String[values.size()]);
	}
	public static String parseCharset(String charset) {
		if (charset==null||RANGE_0_TO_9.equals(charset)) {
			return CHARSET_0_TO_9;
		}
		if (RANGE_A_TO_Z_UPPER.equals(charset)) {
			return CHARSET_A_TO_Z_UPPER;
		}
		if (RANGE_A_TO_Z_LOWER.equals(charset)) {
			return CHARSET_A_TO_Z_LOWER;
		}
		int i = charset.indexOf('-');
		if (i==-1) {
			return charset; 
		}
		StringBuilder s = new StringBuilder();
		int curr = 0;
		do {
			if (i==-1) {
				s.append(charset.substring(curr));
				break;
			} else {
				if (curr+1 != i) {
					s.append(charset.substring(curr, i-1));
					curr = i-1;
				}
				int start = (int)charset.charAt(curr);
				int end = (int)charset.charAt(curr+2);
				if (start > end) {
					throw new RuntimeException("Invalid range:"+charset.substring(curr,curr+3));
				}
				for (int j = start; j <= end; j++) {
					s.append((char)j);
				}
				curr += 3;
				i = curr;
			}
			i=charset.indexOf('-', i);
		} while(curr<charset.length());
		return s.toString();
	}
}
