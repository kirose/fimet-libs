package com.fimet.test;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fimet.exe.SocketResult;
import com.fimet.parser.MLI;
import com.fimet.simulator.ISimulator;
import com.fimet.stress.IStress;
import com.fimet.stress.IStressMonitor;
import com.fimet.stress.creator.CartesianCreator;
import com.fimet.stress.creator.FieldVariator;
import com.fimet.stress.creator.PanVariator;
import com.fimet.usecase.IUseCase;
import com.fimet.utils.MessageBuilder;
import com.fimet.utils.SimulatorBuilder;
import com.fimet.utils.StressBuilder;
import com.fimet.utils.UseCaseUtils;
/**
 * A simple example for create and execute use cases 
 * @author Marco A. Salazar
 *
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class StressTest implements IStressMonitor {
	public static void main(String[] args) throws Exception {
		new StressTest().test();
	}
	@Test
	public void test() {
		// Simulator Parameters
		ISimulator acquirer = new SimulatorBuilder()
			.name("SAcq")
			.model("National")
			.parser("National")
			.address("127.0.0.1")
			.port(8583)
			.server(false)
			.adapter(MLI.EXCLUSIVE)
			.build();
		ISimulator issuer = new SimulatorBuilder()
			.name("SIss")
			.model("National")
			.parser("National")
			.address("127.0.0.1")
			.port(8583)
			.server(true)
			.adapter(MLI.EXCLUSIVE)
			.build();
    	
    	// Create a stress file with a Cartesian Creator
    	File stressFile0 = new CartesianCreator("stress/stress-0.txt")
	    	.simulatorModel(acquirer.getModel())
			.setMessage(
				new MessageBuilder()
				.parser("National")
				.adapter(MLI.EXCLUSIVE)
		    	.mti("0200")
		    	.header("ISO858300000")
		    	.value(2, "1234567890123456")
		    	.value(3, "000000")
		    	.value(4, "000000012300")
		    	.value(11, "123456")
		    	.build()
			)
			.variations("mti", new String[]{"0200","0420","0100"})
			.variations("2", new String[]{"1234567890123456","1234567890123451","1234567890123452","1234567890123453","1234567890123454"})
			.variations("3", new String[]{"000000","010000","020000","030000","040000"})
			.variations("4", new String[]{"000000010000","000000020000","000000030000","000000040000","000000050000"})
			.variations("11", new String[]{"123456","123457","123458","123459","123450"})
			.create();
    	
    	// Create a stress file with a Cartesian Creator and use case
    	IUseCase uc1 = UseCaseUtils.fromPath("usecases/purchase-1.uc");
    	
    	File stressFile1 = new CartesianCreator("stress/stress-1.txt")
			.setMessage(uc1.getMessage())
			.simulatorModel(acquirer.getModel())
			.adapter(uc1.getAcquirer().getSocket().getAdapter())
			.variations("2", new PanVariator("123456").setRange(0, 10).variate())
			.variations("3", new FieldVariator(2,'0').setRange(0, 4).append("0000").variate())
			.variations("4", new FieldVariator(10,'0').setRange(0, 4).append("00").variate())
			.variations("11", new FieldVariator(6,'0').setRange(0, 4).variate())
			.create();
    	
    	new StressBuilder("StressTest")
	    	// cycle time until the file is fully read
	    	.cycleTime(1000)
	    	// number of messages injected per cycle
	    	.messagesPerCycle(10)
	    	.monitor(this)
	    	// For each stress file a thread-injector will be instantiated
	    	.addStressFile(acquirer, stressFile0) 
	    	.addStressFile(uc1.getAcquirer(), stressFile1)
	    	// if need connect some simulator or socket
	    	.connect(issuer)
	    	.connect(uc1)
	    	.execute();

	}

	@Override
	public void onInjectorFinishCycle(SocketResult result) {
		System.out.println(result.getCycleStats());
	}

	@Override
	public void onInjectorStart(SocketResult result) {
		System.out.println(SocketResult.getHeadersCycleStats());
	}

	@Override
	public void onInjectorFinish(SocketResult result) {
		System.out.println(SocketResult.getHeadersGlobalStats());
		System.out.println(result.getGlobalStats());
	}

	@Override
	public void onStressStart(IStress stress) {
		System.out.println("***********Start stress execution************");
	}

	@Override
	public void onStressFinish(IStress stress, List<SocketResult> results) {
		System.out.println("***********Finish stress execution************");
	}
}
