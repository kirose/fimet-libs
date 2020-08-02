package com.fimet.test;


import com.fimet.IExecutorManager;
import com.fimet.Manager;
import com.fimet.parser.MLI;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.extension.SimulatorExtension;
import com.fimet.utils.SimulatorBuilder;
import com.fimet.utils.UseCaseBuilder;
/**
 * A simple example for create and execute use cases 
 * @author Marco A. Salazar
 *
 */
public class UseCaseTest {
	static IExecutorManager executorManager = Manager.get(IExecutorManager.class);
	public static void main(String[] args) throws Exception {
		System.out.println("user.dir:"+System.getProperty("user.dir"));
		new UseCaseTest().test();
	}
	private void test() {
		// Example 1
		// Execute all use cases in the folder fimet/usecases/ sequentially
		// See store/task/* for execution results
		executorManager.executeUseCase("fimet/usecases/");

		// Example 2
		// Using UseCaseBuilder and SimulatorBuilder
		ISimulator sAquirer = new SimulatorBuilder()
			.name("Aquirer") // Use a unique name
			.parser("PNational") // fimet/model/parsers.xml
			.model("MNational") // fimet/model/simulatorModels.xml
			.address("127.0.0.1")
			.port(8583)
			.server(false)
			.adapter(MLI.EXCLUSIVE)
			.build();
		ISimulator sIssuer = new SimulatorBuilder()
			.name("Issuer")
			.parser("PNational")
			.model("MNational")
			.address("127.0.0.1")
			.port(8583)
			.server(true)
			.adapter(MLI.EXCLUSIVE)
			.build();

		// Execute a use cases with UseCaseBuilder
    	new UseCaseBuilder("Compra", sAquirer)
	    	.addSimulator(sIssuer)
	    	.mti("0200")
	    	.header("ISO858300000")
	    	.value(2, "1234567890123456")
	    	.value(3, "000000")
	    	.value(4, "000000012300")
	    	.value(11, "123456")
	    	// Simulator extensions are created automatically for files "uc"
	    	.simulatorExtension(new SimulatorExtension())
	    	.execute();

		ISimulator sVisaIssuer = new SimulatorBuilder()
			.name("VisaIssuer")
			.parser("PNational")
			.model("MNational")
			.address("127.0.0.1")
			.port(9128)
			.server(true)
			.adapter(MLI.EXCLUSIVE)
			.build();
    	
    	new UseCaseBuilder("PurchaseVisa", "SVisa")
	    	.addSimulator(sVisaIssuer)
	    	.mti("0200")
	    	.header("ISO858300000")
	    	.value(2, "1234567890123456")
	    	.value(3, "000000")
	    	.value(4, "000000012300")
	    	.value(11, "123456")
	    	.execute();

    	new UseCaseBuilder("ReversalVisa", "SVisa")
	    	.addSimulator(sVisaIssuer)
	    	.authorization("PurchaseVisa")
	    	.mti("0420")
	    	.header("ISO858300000")
	    	.value(2, "1234567890123456")
	    	.value(3, "000000")
	    	.value(4, "000000012300")
	    	.value(11, "123456")
	    	.execute();	
	}
}
