package com.fimet.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.fimet.ISimulatorManager;
import com.fimet.Manager;
import com.fimet.Paths;
import com.fimet.SimulatorModelManager;
import com.fimet.json.JExeAdapterFactory;
import com.fimet.json.JSimulator;
import com.fimet.json.JUseCase;
import com.fimet.json.JUseCaseJMsg;
import com.fimet.simulator.ISimulator;
import com.fimet.usecase.UseCase;

public final class UseCaseUtils {
	private static SimulatorModelManager simulatorModelManager = Manager.getManager(SimulatorModelManager.class);
	private UseCaseUtils() {}
	public static JUseCaseJMsg fromStringToJUseCaseJMsg(String json) {
		return JExeAdapterFactory.GSON.fromJson(json, JUseCaseJMsg.class);
	}
	public static JUseCase fromStringToJUseCase(String json) {
		return JExeAdapterFactory.GSON.fromJson(json, JUseCase.class);
	}
	public static JUseCase jsonFromPath(String path) {
		return fromFileToJUseCase(new File(path).getAbsoluteFile());
	}
	public static JUseCase fromFileToJUseCase(File fileJson) {
		if ("uc".equalsIgnoreCase(FileUtils.getExtension(fileJson))) {
			JUseCase json = JExeAdapterFactory.GSON.fromJson(FileUtils.readContents(fileJson), JUseCase.class);
			if (json.getName() == null) {
				json.setName(getRelativePath(fileJson));
			}
			return json;
		} else {
			return null;
		}
	}
	public static String getRelativePath(File file) {
		String path = file.getAbsolutePath();
		return path.substring(Paths.USECASES.getAbsolutePath().length()+1);
	}
	public static UseCase fromJson(String json) {
		JUseCase ucJson = fromStringToJUseCase(json);
		if (json != null) {
			UseCase useCase = fromJson(ucJson);
			useCase.setSource(json);
			return useCase;
		} else {
			return null;
		}
	}
	public static UseCase fromPath(String useCasePath) {
		File file = new File(useCasePath);
		return fromFile(file);
	}
	public static UseCase fromFile(File fileJson) {
		JUseCase json = fromFileToJUseCase(fileJson);
		if (json != null) {
			UseCase useCase = fromJson(json);
			useCase.setSource(fileJson);
			return useCase;
		} else {
			return null;
		}
	}
	public static UseCase fromJson(JUseCase json) {
		ISimulatorManager simulatorManager = Manager.getManager(ISimulatorManager.class);
		if (json.getSimulators() == null) {
			throw new NullPointerException("Expected a simulator entry");
		}
		List<ISimulator> simulators = new ArrayList<>(json.getSimulators().size()); 
		for (JSimulator s : json.getSimulators()) {
			simulators.add(simulatorManager.getSimulator(s));
		}
		UseCase useCase = new UseCase();
		useCase.setAuthorization(json.getAuthorization());
		useCase.setName(json.getName() != null ? json.getName() : "UseCaseUnkown");
		useCase.setSimulators(simulators);
		useCase.setMessage(json.getMessage());
		useCase.setSimulatorExtension(simulatorModelManager.newSimulatorExtension(json.getSimulators()));
		return useCase;
	}
	public static String toJsonPretty(JUseCase useCaseJson) {
		return JExeAdapterFactory.GSON_PRETTY.toJson(useCaseJson);
	}
	public static String toJson(JUseCase useCaseJson) {
		return JExeAdapterFactory.GSON.toJson(useCaseJson);
	}
}
