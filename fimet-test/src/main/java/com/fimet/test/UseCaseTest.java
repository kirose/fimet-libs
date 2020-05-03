package com.fimet.test;

import com.fimet.IExecutorManager;
import com.fimet.Manager;
import com.fimet.adapter.iso8583.MLI;
import com.fimet.exe.IUseCaseExecutorListener;
import com.fimet.simulator.PSimulator;
import com.fimet.usecase.IUseCase;
import com.fimet.usecase.exe.ExecutionResult;
import com.fimet.utils.UseCaseBuilder;
/**
 * A simple example for create and execute use cases 
 * @author Marco A. Salazar
 *
 */
public class UseCaseTest implements IUseCaseExecutorListener {
	static IExecutorManager useCaseManager = Manager.get(IExecutorManager.class);
	public static void main(String[] args) throws Exception {
		new UseCaseTest().test();
	}
	private void test() {
		// Execute all use cases in the folder usecases/ and use a executor listener
		// Use cases execution is sequential
		// See logs/socket.log and logs/validatios.log for results execution
		useCaseManager.execute("usecases/", this);

		// Simulator Parameters
		PSimulator socketAcq = new PSimulator("National", "National", "127.0.0.1", 8583, false, MLI.EXCLUSIVE);
    	PSimulator socketIss = new PSimulator("National", "National", "127.0.0.1", 8583, true, MLI.EXCLUSIVE);
    	PSimulator socketVisaIss = new PSimulator("National", "National", "127.0.0.1", 9128, true, MLI.EXCLUSIVE);

		// Execute a use cases with UseCaseBuilder
    	new UseCaseBuilder("Compra", socketAcq)
    	.addConnection(socketIss)
    	.setMessageMti("0200")
    	.setMessageHeader("ISO858300000")
    	.setMessageValue(2, "1234567890123456")
    	.setMessageValue(3, "000000")
    	.setMessageValue(4, "000000012300")
    	.setMessageValue(11, "123456")
    	// Simulator extensions are created authomatically for files "uc"
    	.setSimulatorExtension(new SimulatorExtension())
    	.execute();

    	new UseCaseBuilder("PurchaseVisa", "SVisa")
    	.addConnection(socketVisaIss)
    	.setMessageMti("0200")
    	.setMessageHeader("ISO858300000")
    	.setMessageValue(2, "1234567890123456")
    	.setMessageValue(3, "000000")
    	.setMessageValue(4, "000000012300")
    	.setMessageValue(11, "123456")
    	.execute();
    	new UseCaseBuilder("ReversalVisa", "SVisa")
    	.addConnection(socketVisaIss)
    	.setAuthorization("PurchaseVisa")
    	.setMessageMti("0420")
    	.setMessageHeader("ISO858300000")
    	.setMessageValue(2, "1234567890123456")
    	.setMessageValue(3, "000000")
    	.setMessageValue(4, "000000012300")
    	.setMessageValue(11, "123456")
    	.execute();	
	}
	@Override
	public void onStart(IUseCase useCase) {
		System.out.println("start-"+useCase.getName());
	}
	@Override
	public void onFinish(IUseCase useCase, ExecutionResult results) {
		System.out.println("finish-"+useCase.getName());
	}
}
