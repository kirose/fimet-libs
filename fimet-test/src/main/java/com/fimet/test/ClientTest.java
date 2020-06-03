package com.fimet.test;

import com.fimet.Manager;
import com.fimet.client.IClientManager;
import com.fimet.client.ws.IUseCaseWS;
import com.fimet.usecase.UseCase;
import com.fimet.utils.UseCaseUtils;

public class ClientTest {

	public static void main(String[] args) {
		IUseCaseWS ws = Manager.get(IClientManager.class).getImplementation(IUseCaseWS.class);
		UseCase usecase = UseCaseUtils.fromPath("usecases/purchase.uc");
		ws.execute(usecase);
	}
}
