package com.fimet.test;

import java.io.File;

import com.fimet.core.iso8583.adapter.IAdapterManager;
import com.fimet.core.net.Socket;
import com.fimet.core.stress.TestBuilder;
import com.fimet.core.stress.creator.CartesianCreator;
import com.fimet.core.utils.MessageBuilder;

public class StressTest {
	public static void main(String[] args) throws Exception {

		Socket socketAcq = new Socket("127.0.0.1", 8583, false, "National", "National Acquirer", IAdapterManager.MLI_EXCLISIVE);
    	Socket socketIss = new Socket("127.0.0.1", 8583, true, "National", "National Issuer", IAdapterManager.MLI_EXCLISIVE);

    	Socket socketAcq2 = new Socket("127.0.0.1", 8584, false, "National", "National Acquirer", IAdapterManager.MLI_EXCLISIVE);
    	Socket socketIss2 = new Socket("127.0.0.1", 8584, true, "National", "National Issuer", IAdapterManager.MLI_EXCLISIVE);
    	
    	File stressFile = new CartesianCreator("resources/stress.txt")
		.setMessage(
			new MessageBuilder()
			.setParser("National")
			.setAdapter(IAdapterManager.MLI_EXCLISIVE)
	    	.setMti("0200")
	    	.setHeader("ISO858300000")
	    	.setValue(2, "1234567890123456")
	    	.setValue(3, "000000")
	    	.setValue(4, "000000012300")
	    	.setValue(11, "123456")
	    	.build()
		)
		.addVariation("mti", new String[]{"0200","0420","0100"})
		.addVariation("2", new String[]{"1234567890123456","1234567890123451","1234567890123452","1234567890123453","1234567890123454"})
		.addVariation("3", new String[]{"000000","010000","020000","030000","040000"})
		.addVariation("4", new String[]{"000000010000","000000020000","000000030000","000000040000","000000050000"})
		.addVariation("11", new String[]{"123456","123457","123458","123459","123450"})
		.create();

    	File stressFile2 = new CartesianCreator("resources/stress.txt")
		.setMessage(
			new MessageBuilder()
			.setParser("National")
			.setAdapter(IAdapterManager.MLI_EXCLISIVE)
	    	.setMti("0200")
	    	.setHeader("ISO858300000")
	    	.setValue(2, "1234567890123456")
	    	.setValue(3, "000000")
	    	.setValue(4, "000000012300")
	    	.setValue(11, "123456")
	    	.build()
		)
		.addVariation("mti", new String[]{"0200","0420","0100"})
		.addVariation("2", new String[]{"1234567890123456","1234567890123451","1234567890123452","1234567890123453","1234567890123454"})
		.addVariation("3", new String[]{"000000","010000","020000","030000","040000"})
		.addVariation("4", new String[]{"000000010000","000000020000","000000030000","000000040000","000000050000"})
		.addVariation("11", new String[]{"123456","123457","123458","123459","123450"})
		.create();
    	
    	new TestBuilder()
    	.addSocketStress(socketAcq, stressFile)
    	.addSocketStress(socketAcq2, stressFile2)
    	.addConnection(socketIss)
    	.addConnection(socketIss2)
    	.execute();
	}
}
