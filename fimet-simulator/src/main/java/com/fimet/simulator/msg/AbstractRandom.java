package com.fimet.simulator.msg;

import java.util.Random;

import com.fimet.commons.exception.ConverterException;

public class AbstractRandom {
	private Random random = new Random();
	protected String random(int digits) {
		if (digits > 0) {
			if (digits < 7) {
				return randomInt(digits);
			} else if (digits < 18) {
				return randomLong(digits);
			} else {
				return randomNumeric(digits);
			}
		} else {
			return "";
		}
	}
	protected String randomInt(int digits) {
		return String.format("%0"+digits+"d",random.nextInt(999999));
	}
	protected String randomLong(int digits) {
		String fmt = String.format("%0"+digits+"d", Math.abs((long)random.nextLong()));
		return fmt.length() > digits ? fmt.substring(0,digits) : fmt;
	}
	protected String randomAlfaNumeric(int digits) {
		if (digits <= 0) {
			return "";
		}
		StringBuilder s = new StringBuilder();
		while (digits > 0) {
			s.append(intToAsciiAlfaNumeric(random.nextInt(72)));
			digits--;
		}
		return s.toString();
	}
	protected String randomAlfa(int digits) {
		if (digits <= 0) {
			return "";
		}
		StringBuilder s = new StringBuilder();
		while (digits > 0) {
			s.append(intToAsciiAlfa(random.nextInt(62)));
			digits--;
		}
		return s.toString();
	}
	protected String randomNumeric(int digits) {
		if (digits <= 0) {
			return "";
		}
		StringBuilder s = new StringBuilder();
		while (digits > 0) {
			s.append(intToAsciiNumeric(random.nextInt(10)));
			digits--;
		}
		return s.toString();
	}
	static byte intToAsciiAlfaNumeric(int b) {
		if (b >= 0 && b <= 9) {// b >= '0' && b <= '9'
			return (byte)(b+48);
		}
		if (b >= 10 && b < 36) {//b >= 'A' b <= 'Z'
			return (byte)(b+55);
		}
		if (b >= 36 && b < 72) {//b >= 'a' b <= 'z'
			return (byte)(b+61);
		}
		throw new ConverterException("Integer '"+b+"' exceed range [0,72)");
	}
	static byte intToAsciiAlfa(int b) {
		if (b >= 0 && b < 26) {//b >= 'A' b <= 'Z'
			return (byte)(b+55);
		}
		if (b >= 26 && b < 62) {//b >= 'a' b <= 'z'
			return (byte)(b+61);
		}
		throw new ConverterException("Byte '"+((char)b)+"' ("+b+") is not hex");
	}
	static byte intToAsciiNumeric(int b) {
		if (b >= 0 && b <= 9) {// b >= '0' && b <= '9'
			return (byte)(b+48);
		}
		throw new ConverterException("Byte '"+((char)b)+"' ("+b+") is not hex");
	}
}
