package com.fimet.stress.creator;

import com.fimet.parser.FormatException;
import com.fimet.utils.PanUtils;

public class PanVariator {
	private int length = 16;
	private String bin;
	private int start;
	private int end;
	private String append = "";
	public PanVariator(String bin) {
		this.bin = bin;
	}
	public PanVariator setRange(int start, int end) {
		this.start = start;
		this.end = end;
		return this;
	}
	public PanVariator setPanLength(int length) {
		if (length < 15 || length > 16) {
			throw new FormatException("Invalida length "+length+", expected 15 or 16");
		}
		this.length = length;
		return this;
	}
	public PanVariator append(String end) {
		append = end;
		return this;
	}
	public String[] variate() {
		String[] out = new String[end-start];
		String pan = null;
		String format = "%0"+(length-7)+"d";
		for (int i = 0, index = start; index < end; i++, index++) {
			 pan = bin + String.format(format, index)+"0";
			 char lastDigit = PanUtils.calculateLastDigit(pan);
			 pan = pan.substring(0,pan.length()-1)+lastDigit+pan.substring(pan.length())+append;
			 out[i] = pan;
		}
		return out;
	}
	public static void main(String[] args) {
		String[] pans = new PanVariator("400195").setRange(0,10).append("=2210").variate();
		for (String pan : pans) {
			System.out.println(pan);
		}
	}
}
