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
import com.fimet.parser.Message;
import com.fimet.simulator.ISimulator;
import com.fimet.usecase.UseCase;

public final class UseCaseUtils {
	private UseCaseUtils() {}
	public static UseCaseJson jsonFromString(String json) {
		return GSON.fromJson(json, UseCaseJson.class);
	}
	public static UseCaseJson jsonFromPath(String path) {
		return jsonFromFile(new File(path));
	}
	public static UseCaseJson jsonFromFile(File fileJson) {
		if ("uc".equalsIgnoreCase(FileUtils.getExtension(fileJson))) {
			UseCaseJson json = GSON_PRETTY.fromJson(FileUtils.readContents(fileJson), UseCaseJson.class);
			if (json.getName() == null) {
				json.setName(FileUtils.getSimpleName(fileJson));
			}
			return json;
		} else {
			return null;
		}
	}
	public static UseCase fromString(String json) {
		UseCaseJson ucJson = jsonFromString(json);
		if (json != null) {
			UseCase useCase = fromJson(ucJson);
			return useCase;
		} else {
			return null;
		}
	}
	public static UseCase fromJson(String json) {
		UseCaseJson ucJson = jsonFromString(json);
		if (json != null) {
			UseCase useCase = fromJson(ucJson);
			return useCase;
		} else {
			return null;
		}
	}
	public static UseCase fromPath(String useCasePath) {
		UseCaseJson json = jsonFromFile(new File(useCasePath));
		if (json != null) {
			UseCase useCase = fromJson(json);
			return useCase;
		} else {
			return null;
		}
	}
	public static UseCase fromFile(File fileJson) {
		UseCaseJson json = jsonFromFile(fileJson);
		if (json != null) {
			UseCase useCase = fromJson(json);
			return useCase;
		} else {
			return null;
		}
	}
	public static UseCase fromJson(UseCaseJson json) {
		ISimulatorManager simulatorManager = Manager.get(ISimulatorManager.class);
		if (json.getSimulators() == null) {
			throw new NullPointerException("Expected a simulator entry");
		}
		List<ISimulator> simulators = new ArrayList<>(json.getSimulators().size()); 
		for (SimulatorJson s : json.getSimulators()) {
			simulators.add(simulatorManager.getSimulator(s.toESimulator()));
		}
		UseCase useCase = new UseCase();
		useCase.setName(json.getName() != null ? json.getName() : "UseCaseUnkown");
		useCase.setSimulators(simulators);
		Message message = MessageUtils.fromJson(json.getMessage(), useCase.getAcquirer().getParser());
		useCase.setMessage(message);
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
