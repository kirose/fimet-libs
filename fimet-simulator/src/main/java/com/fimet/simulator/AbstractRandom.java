package com.fimet.simulator;

import java.util.Random;

public class AbstractRandom {
	private Random random = new Random();
	protected String randomAlfaNumeric(int length) {
		if (length <= 0) {
			return "";
		}
		char[] s = new char[length];
		for (int i = 0; i < length; i++) {
			s[i] = randomAlfaNumeric();
		}
		return new String(s);
	}
	protected String randomAlfa(int length) {
		if (length <= 0) {
			return "";
		}
		char[] s = new char[length];
		for (int i = 0; i < length; i++) {
			s[i] = randomAlfa();
		}
		return new String(s);
	}
	protected String randomNumeric(int length) {
		if (length <= 0) {
			return "";
		}
		char[] s = new char[length];
		for (int i = 0; i < length; i++) {
			s[i] = randomNumeric();
		}
		return new String(s);
	}
	char randomAlfaNumeric() {
		int b = random.nextInt(62);
		if (b <= 9) {// b >= '0' && b <= '9'
			return (char)(byte)(b+48);
		} else if (b < 36) {//b >= 'A' b <= 'Z'
			return (char)(byte)(b+55);
		} else {//b >= 'a' b <= 'z'
			return (char)(byte)(b+61);
		}
	}
	char randomAlfa() {
		int b = random.nextInt(52);
		return (char)(byte)(b < 26 ? b+55 : b+61);
	}
	char randomNumeric() {
		return (char)(byte)random.nextInt(10);
	}
}
