package com.fimet.utils;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fimet.ISimulatorManager;
import com.fimet.Manager;
import com.fimet.Paths;
import com.fimet.json.JExeAdapterFactory;
import com.fimet.json.JMessageMap;
import com.fimet.json.JStress;
import com.fimet.json.JStressFile;
import com.fimet.json.JStressFileBuilder;
import com.fimet.parser.IMessage;
import com.fimet.parser.Message;
import com.fimet.simulator.ISimulator;
import com.fimet.stress.Stress;
import com.fimet.stress.creator.CartesianCreator;
import com.fimet.stress.creator.ReplacementsCreator;
import com.fimet.stress.creator.VariatorCreator;

public class StressUtils {

	public static Stress fromPath(String path, File folder) {
		return fromFile(new File(path), folder);
	}
	public static JStress jsonFromPath(String path) {
		return jsonFromFile(new File(path).getAbsoluteFile());
	}
	
	public static Stress fromFile(File fileJson, File folder) {
		JStress json = jsonFromFile(fileJson);
		if (json != null) {
			Stress stress = fromJson(json, folder);
			stress.setSource(fileJson);
			return stress;
		} else {
			return null;
		}
	}
	public static JStress jsonFromFile(File file) {
		if ("stress".equalsIgnoreCase(FileUtils.getExtension(file))) {
			JStress json = JExeAdapterFactory.GSON.fromJson(FileUtils.readContents(file), JStress.class);
			if (json.getName() == null) {
				json.setName(FileUtils.getSimpleName(file));
			}
			return json;
		} else {
			return null;
		}
	}
	public static Stress fromJson(String json) {
		JStress ucJson = jsonFromString(json);
		if (json != null) {
			Stress stress = fromJson(ucJson, Paths.TMP);
			stress.setSource(json);
			return stress;
		} else {
			return null;
		}
	}
	public static Stress fromJson(JStress json, File folder) {
		ISimulatorManager simulatorManager = Manager.getManager(ISimulatorManager.class);
		Map<ISimulator, File> stressFiles = new LinkedHashMap<ISimulator, File>();
		List<JStressFile> files = json.getStressFiles();
		for (JStressFile f : files) {
			ISimulator simulator = simulatorManager.getSimulator(f.getSimulator());
			String name = StringUtils.isBlank(f.getName()) ? simulator.getName() : f.getName();
			File file = new File(folder, name+".txt");
			if (file.exists()) file.delete();
			for (JStressFileBuilder b : f.getBuilders()) {
				JMessageMap m = b.getMessage();
				m.setProperty(IMessage.PARSER, simulator.getParser().getName());
				m.setProperty(IMessage.ADAPTER, simulator.getSocket().getAdapter().getName());
				Message msg = MessageUtils.fromJson(m);
				if (b.getReplacements() != null && !b.getReplacements().isEmpty()) {
					new ReplacementsCreator(file, msg, true)
						.setMessage(msg)
						.simulatorModel(simulator.getModel())
						.adapter(simulator.getSocket().getAdapter())
						.replacements(b.getReplacements())
						.create();
				} else if (b.getCartesian() != null) {
					new CartesianCreator(file, msg, true)
						.setMessage(msg)
						.simulatorModel(simulator.getModel())
						.adapter(simulator.getSocket().getAdapter())
						.variations(b.getCartesian())
						.create();
				} else if (b.getVariator() != null) {
					new VariatorCreator(file, msg, true)
						.setMessage(msg)
						.simulatorModel(simulator.getModel())
						.adapter(simulator.getSocket().getAdapter())
						.variator(b.getVariator())
						.create();
				}
					
			}
			stressFiles.put(simulator,file);
		}
		Stress stress = new Stress();
		stress.setMessagesPerCycle(json.getMessagesPerCycle());
		stress.setCycleTime(json.getCycleTime());
		stress.setMaxExecutionTime(json.getMaxExecutionTime());
		stress.setName(json.getName());
		stress.setStressFiles(stressFiles);
		return stress;
	}
	public static JStress jsonFromString(String json) {
		return JExeAdapterFactory.GSON.fromJson(json, JStress.class);
	}

}
