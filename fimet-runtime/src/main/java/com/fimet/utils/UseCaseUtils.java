package com.fimet.utils;

import static com.fimet.json.JsonAdapterFactory.GSON;
import static com.fimet.json.JsonAdapterFactory.GSON_PRETTY;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.fimet.ISimulatorManager;
import com.fimet.Manager;
import com.fimet.json.SimulatorJson;
import com.fimet.json.UseCaseJson;
import com.fimet.simulator.ISimulator;
import com.fimet.usecase.UseCase;

public final class UseCaseUtils {
	private UseCaseUtils() {}
	public static UseCaseJson parseJson(String json) {
		return GSON.fromJson(json, UseCaseJson.class);
	}
	public static UseCaseJson parseJson(File fileJson) {
		if ("uc".equalsIgnoreCase(FileUtils.getExtension(fileJson))) {
			UseCaseJson json = GSON_PRETTY.fromJson(FileUtils.readContents(fileJson), UseCaseJson.class);
			json.setName(FileUtils.getSimpleName(fileJson));
			return json;
		} else {
			return null;
		}
	}
	public static UseCase parseForExecutionFromJson(String json) {
		UseCaseJson ucJson = parseJson(json);
		if (json != null) {
			UseCase useCase = parseForExecutionFromJson(ucJson);
			return useCase;
		} else {
			return null;
		}
	}
	public static UseCase parseForExecutionFromPath(String useCasePath) {
		UseCaseJson json = parseJson(new File(useCasePath));
		if (json != null) {
			UseCase useCase = parseForExecutionFromJson(json);
			return useCase;
		} else {
			return null;
		}
	}
	public static UseCase parseForExecutionFromFile(File fileJson) {
		UseCaseJson json = parseJson(fileJson);
		if (json != null) {
			UseCase useCase = parseForExecutionFromJson(json);
			return useCase;
		} else {
			return null;
		}
	}
	public static UseCase parseForExecutionFromJson(UseCaseJson json) {
		ISimulatorManager simulatorManager = Manager.get(ISimulatorManager.class);
		List<ISimulator> simulators = new ArrayList<>(json.getSimulators().size()); 
		for (SimulatorJson s : json.getSimulators()) {
			simulators.add(simulatorManager.getSimulator(s.toPSimulator()));
		}
		UseCase useCase = new UseCase();
		useCase.setName(json.getName() != null ? json.getName() : "UseCaseUnkown");
		useCase.setSimulators(simulators);
		useCase.setMessage(json.getMessage());
		useCase.setSimulatorExtension(SimulatorUtils.newSimulatorExtension(json));
		return useCase;
	}
	public static String toJsonPretty(UseCaseJson useCaseJson) {
		return GSON_PRETTY.toJson(useCaseJson);
	}
	public static String toJson(UseCaseJson useCaseJson) {
		return GSON.toJson(useCaseJson);
	}
}
