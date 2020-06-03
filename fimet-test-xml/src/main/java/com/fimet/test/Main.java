package com.fimet.test;

import java.util.Base64;

import com.fimet.utils.converter.Converter;

public class Main {
	public static void main(String[] args) {
		encode("H");
		encode("HO");
		encode("HOL");
		encode("HOLA");
		encode("HOLA M");
		encode("HOLA MU");
		encode("HOLA MUN");
		encode("HOLA MUND");
		encode("HOLA MUNDO");
	}
	public static void encode(String text) {
		byte[] encode = Base64.getEncoder().encode(text.getBytes());
		//byte[] decode = Base64.getDecoder().decode(encode);
		System.out.println("//"+text+"="+new String(Converter.asciiToHex(text.getBytes()))+"="+new String(Converter.asciiToHex(encode))+"="+new String(encode));		
	}
}
