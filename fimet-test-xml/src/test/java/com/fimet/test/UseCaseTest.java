package com.fimet.test;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fimet.IExecutorManager;
import com.fimet.exe.Task;
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
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UseCaseTest {
	
	@Autowired private IExecutorManager executorManager;
	
	/**
	 * Example 1
	 * Execute single use cases
	 * See store/task/* for results execution
	 */
	@Test
	public void executeSingleUseCaseTest() {
		Task task = executorManager.executeUseCase("usecases/purchase.uc");
		assertNotNull(task);
	}
	/**
	 * Example 2
	 * Execute all use cases in the folder fimet/usecases/ sequentially
	 * See store/task/* for results execution
	 */
	@Test
	public void executeAllUseCaseTest() {
		Task task = executorManager.executeUseCase("usecases/");
		assertNotNull(task);
	}
	/**
	 * Example 2           
	 * Using UseCaseBuilder
	 * Simulators          
	 */
	@Test
	public void execute() {

		ISimulator sAquirer = new SimulatorBuilder()
			.name("Aquirer") // Use a unique name
			.parser("National") // fimet/model/parsers.xml
			.model("National") // fimet/model/simulatorModels.xml
			.address("127.0.0.1")
			.port(8583)
			.server(false)
			.adapter(MLI.EXCLUSIVE)
			.build();
		ISimulator sIssuer = new SimulatorBuilder()
			.name("Issuer")
			.parser("National")
			.model("National")
			.address("127.0.0.1")
			.port(8583)
			.server(true)
			.adapter(MLI.EXCLUSIVE)
			.build();

		// Execute a use cases with UseCaseBuilder
    	Task task = new UseCaseBuilder("Compra", sAquirer)
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
    	assertNotNull(task);
    	
		ISimulator sVisaIssuer = new SimulatorBuilder()
			.name("VisaIssuer")
			.parser("National")
			.model("National")
			.address("127.0.0.1")
			.port(9128)
			.server(true)
			.adapter(MLI.EXCLUSIVE)
			.build();
    	
    	task = new UseCaseBuilder("PurchaseVisa", "Visa")
	    	.addSimulator(sVisaIssuer)
	    	.mti("0200")
	    	.header("ISO858300000")
	    	.value(2, "1234567890123456")
	    	.value(3, "000000")
	    	.value(4, "000000012300")
	    	.value(11, "123456")
	    	.execute();
    	assertNotNull(task);

    	task = new UseCaseBuilder("ReversalVisa", "Visa")
	    	.addSimulator(sVisaIssuer)
	    	.authorization("PurchaseVisa")
	    	.mti("0420")
	    	.header("ISO858300000")
	    	.value(2, "1234567890123456")
	    	.value(3, "000000")
	    	.value(4, "000000012300")
	    	.value(11, "123456")
	    	.execute();
    	assertNotNull(task);
	}
}
