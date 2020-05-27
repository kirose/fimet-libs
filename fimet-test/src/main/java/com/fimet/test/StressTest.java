package com.fimet.test;

import java.io.File;
import java.util.List;

import com.fimet.exe.InjectorResult;
import com.fimet.parser.MLI;
import com.fimet.simulator.PSimulator;
import com.fimet.stress.IStress;
import com.fimet.stress.IStressExecutorListener;
import com.fimet.stress.creator.CartesianCreator;
import com.fimet.stress.creator.FieldVariator;
import com.fimet.stress.creator.PanVariator;
import com.fimet.usecase.UseCase;
import com.fimet.utils.MessageBuilder;
import com.fimet.utils.StressBuilder;
import com.fimet.utils.UseCaseUtils;
/**
 * A simple example for create and execute use cases 
 * @author Marco A. Salazar
 *
 */
public class StressTest implements IStressExecutorListener {
	public static void main(String[] args) throws Exception {
		new StressTest().test();
	}

	private void test() {
		// Simulator Parameters 
		PSimulator socketAcq = new PSimulator("National", "National", "127.0.0.1", 8583, false, MLI.EXCLUSIVE);
    	PSimulator socketIss = new PSimulator("National", "National", "127.0.0.1", 8583, true, MLI.EXCLUSIVE);
    	
    	// Create a stress file with a Cartesian Creator
    	File stressFile0 = new CartesianCreator("stress/stress-0.txt")
		.setMessage(
			new MessageBuilder()
			.setParser("National")
			.setAdapter(MLI.EXCLUSIVE)
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
    	
    	// Create a stress file with a Cartesian Creator and use case
    	UseCase uc1 = UseCaseUtils.parseForExecutionFromPath("usecases/purchase-1.uc");
    	
    	File stressFile1 = new CartesianCreator("stress/stress-1.txt")
		.setMessage(uc1.getMessage())
		.addVariation("2", new PanVariator("123456").setRange(0, 10).variate())
		.addVariation("3", new FieldVariator(2,'0').setRange(0, 4).append("0000").variate())
		.addVariation("4", new FieldVariator(10,'0').setRange(0, 4).append("00").variate())
		.addVariation("11", new FieldVariator(6,'0').setRange(0, 4).variate())
		.create();
    	
    	new StressBuilder("StressTest")
    	// cycle time until the file is fully read
    	.setCycleTime(1000)
    	// number of messages injected per cycle
    	.setMessagesPerCycle(10)
    	.setExecutorListener(this)
    	// For each stress file a thread-injector will be instantiated
    	.addStressFile(socketAcq, stressFile0) 
    	.addStressFile(uc1.getAcquirer(), stressFile1)
    	// if need connect some simulator or socket
    	.connect(socketIss)
    	.connect(uc1)
    	.execute();		
	}

	@Override
	public void onInjectorFinishCycle(InjectorResult result) {
		System.out.println(result.getCycleStats());
	}

	@Override
	public void onInjectorStart(InjectorResult result) {
		System.out.println(InjectorResult.getHeadersCycleStats());
	}

	@Override
	public void onInjectorFinish(InjectorResult result) {
		System.out.println(InjectorResult.getHeadersGlobalStats());
		System.out.println(result.getGlobalStats());
	}

	@Override
	public void onStressStart(IStress stress) {
		System.out.println("***********Start stress execution************");
	}

	@Override
	public void onStressFinish(IStress stress, List<InjectorResult> results) {
		System.out.println("***********Finish stress execution************");
	}
}
