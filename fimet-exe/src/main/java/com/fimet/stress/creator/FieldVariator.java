package com.fimet.stress.creator;

import com.fimet.parser.FormatException;

public class FieldVariator {
	private int length = 16;
	private int start;
	private int end;
	private char pad = ' ';
	private String append = "";
	public FieldVariator(int length) {
		setLength(length);
	}
	public FieldVariator(int length, char leftpad) {
		setPad(leftpad);
		setLength(length);
	}
	public FieldVariator setPad(char pad) {
		this.pad = pad;
		return this;
	}
	public FieldVariator setRange(int start, int end) {
		this.start = start;
		this.end = end;
		return this;
	}
	public FieldVariator setLength(int length) {
		if (length <= 0) {
			throw new FormatException("Invalida length "+length+", greater than zero");
		}
		this.length = length;
		return this;
	}
	public FieldVariator append(String append) {
		if (append == null) {
			this.append = "";
		} else {
			this.append = append;
		}
		return this;
	}
	public String[] variate() {
		String[] out = new String[end-start];
		String value = null;
		String format = "%"+pad+""+length+"d";
		for (int i = 0, index = start; index < end; i++, index++) {
			 value = String.format(format, index);
			 out[i] = value+append;
		}
		return out;
	}
	public static void main(String[] args) {
		System.out.println("RRN:");
		String[] values = new FieldVariator(12,'0').setRange(0,10).variate();
		for (String value : values) {
			System.out.println(value);
		}
		System.out.println("AMOUNT:");
		values = new FieldVariator(10,'0').setRange(1,10).append("00").variate();
		for (String value : values) {
			System.out.println(value);
		}
	}
}
