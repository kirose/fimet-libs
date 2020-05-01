package com.fimet.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.fimet.ISimulatorManager;
import com.fimet.Manager;
import com.fimet.commons.utils.FileUtils;
import com.fimet.json.adapter.JsonAdapterFactory;
import com.fimet.simulator.ISimulator;
import com.fimet.usecase.UseCase;
import com.fimet.usecase.json.SimulatorJson;
import com.fimet.usecase.json.UseCaseJson;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class UseCaseUtils {
	public static final Gson GSON = new GsonBuilder()
			.setPrettyPrinting()
			.disableHtmlEscaping()
			.registerTypeAdapterFactory(new JsonAdapterFactory())
			.create();
	private UseCaseUtils() {}
	public static UseCaseJson parseJson(String json) {
		return GSON.fromJson(json, UseCaseJson.class);
	}
	
	public static UseCaseJson parseJson(File fileJson) {
		if ("uc".equalsIgnoreCase(FileUtils.getExtension(fileJson))) {
			UseCaseJson json = GSON.fromJson(FileUtils.readContents(fileJson), UseCaseJson.class);
			json.setName(FileUtils.getSimpleName(fileJson));
			return json;
		} else {
			return null;
		}
	}
	public static UseCase parseForExecution(String useCasePath) {
		UseCaseJson json = parseJson(new File(useCasePath));
		if (json != null) {
			UseCase useCase = parseForExecution(json);
			return useCase;
		} else {
			return null;
		}
	}
	public static UseCase parseForExecution(File fileJson) {
		UseCaseJson json = parseJson(fileJson);
		if (json != null) {
			UseCase useCase = parseForExecution(json);
			return useCase;
		} else {
			return null;
		}
	}
	public static UseCase parseForExecution(UseCaseJson json) {
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
	public static String toJson(UseCaseJson useCaseJson) {
		return GSON.toJson(useCaseJson);
	}
}
