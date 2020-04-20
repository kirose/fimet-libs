package com.fimet.test;


import com.fimet.core.iso8583.parser.Message;
import com.fimet.core.net.ISocket;
import com.fimet.core.validator.IValidator;

public class Validator1 implements IValidator {

	public Validator1() {
	}
	public void onWriteMessage(ISocket socket, Message msg) {
		System.out.println("onWriteMessage: "+msg);		
	}
	public void onReadMessage(ISocket socket, Message msg) {
		System.out.println("onReadMessage: "+msg);		
	}

}
