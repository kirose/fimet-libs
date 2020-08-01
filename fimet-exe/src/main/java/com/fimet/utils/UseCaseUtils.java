package com.fimet.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.fimet.ISimulatorManager;
import com.fimet.Manager;
import com.fimet.json.JExeAdapterFactory;
import com.fimet.json.JSimulator;
import com.fimet.json.JUseCase;
import com.fimet.simulator.ISimulator;
import com.fimet.usecase.UseCase;

public final class UseCaseUtils {
	private UseCaseUtils() {}
	public static JUseCase jsonFromString(String json) {
		return JExeAdapterFactory.GSON.fromJson(json, JUseCase.class);
	}
	public static JUseCase jsonFromPath(String path) {
		return jsonFromFile(new File(path));
	}
	public static JUseCase jsonFromFile(File fileJson) {
		if ("uc".equalsIgnoreCase(FileUtils.getExtension(fileJson))) {
			JUseCase json = JExeAdapterFactory.GSON.fromJson(FileUtils.readContents(fileJson), JUseCase.class);
			if (json.getName() == null) {
				json.setName(FileUtils.getSimpleName(fileJson));
			}
			return json;
		} else {
			return null;
		}
	}
	public static UseCase fromJson(String json) {
		JUseCase ucJson = jsonFromString(json);
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
		JUseCase json = jsonFromFile(fileJson);
		if (json != null) {
			UseCase useCase = fromJson(json);
			useCase.setSource(fileJson);
			return useCase;
		} else {
			return null;
		}
	}
	public static UseCase fromJson(JUseCase json) {
		ISimulatorManager simulatorManager = Manager.get(ISimulatorManager.class);
		if (json.getSimulators() == null) {
			throw new NullPointerException("Expected a simulator entry");
		}
		List<ISimulator> simulators = new ArrayList<>(json.getSimulators().size()); 
		for (JSimulator s : json.getSimulators()) {
			simulators.add(simulatorManager.getSimulator(s));
		}
		UseCase useCase = new UseCase();
		useCase.setName(json.getName() != null ? json.getName() : "UseCaseUnkown");
		useCase.setSimulators(simulators);
		useCase.setMessage(json.getMessage());
		useCase.setSimulatorExtension(SimulatorUtils.newSimulatorExtension(json.getSimulators()));
		return useCase;
	}
	public static String toJsonPretty(JUseCase useCaseJson) {
		return JExeAdapterFactory.GSON_PRETTY.toJson(useCaseJson);
	}
	public static String toJson(JUseCase useCaseJson) {
		return JExeAdapterFactory.GSON.toJson(useCaseJson);
	}
}
