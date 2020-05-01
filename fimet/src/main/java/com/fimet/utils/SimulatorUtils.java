package com.fimet.utils;

import java.util.Map.Entry;

import com.fimet.IClassLoaderManager;
import com.fimet.ICompilerManager;
import com.fimet.Manager;
import com.fimet.commons.FimetLogger;
import com.fimet.iso8583.parser.Message;
import com.fimet.simulator.AbstractSimulatorExtension;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ISimulatorExtension;
import com.fimet.simulator.IValidator;
import com.fimet.simulator.NullSimulatorExtension;
import com.fimet.simulator.ValidationResult;
import com.fimet.usecase.UseCase;
import com.fimet.usecase.json.SimulatorExtensionJson;
import com.fimet.usecase.json.SimulatorJson;
import com.fimet.usecase.json.SimulatorRules;
import com.fimet.usecase.json.UseCaseJson;

public final class SimulatorUtils {
	public static final String PACKAGE = "com.fimet.validator.tmp"; 
	private SimulatorUtils() {}
	public static ISimulatorExtension newSimulatorExtension(UseCaseJson json) {
		Class<?> clazz = getClassValidator(json);
		try {
			return (ISimulatorExtension)clazz.newInstance();
		} catch (Exception e) {
			FimetLogger.error(UseCaseJson.class, "Error instantianting validator class "+clazz.getName(), e);
		}
		return NullSimulatorExtension.INSTANCE;
	}

	public static Class<?> getClassValidator(UseCaseJson json) {
		long hash = json.hashCodeExtensions();
		if (hash == 1L) {
			return NullSimulatorExtension.class;
		}
		String simpleClassName = "SE"+Math.abs(hash);
		String className = PACKAGE+"."+simpleClassName;
		if (Manager.get(IClassLoaderManager.class).isInstalled(className)) {
			try {
				return Manager.get(IClassLoaderManager.class).loadClass(className);
			} catch (ClassNotFoundException e) {}
		}
		StringBuilder s = new StringBuilder();
		s.append("package ").append(PACKAGE).append(";\n\n");
		s.append("import ").append(AbstractSimulatorExtension.class.getName()).append(";\n");
		s.append("import ").append(ISimulator.class.getName()).append(";\n");
		s.append("import ").append(ValidationResult.class.getName()).append(";\n");
		s.append("import ").append(IValidator.class.getName()).append(";\n");
		s.append("import ").append(UseCase.class.getName()).append(";\n");
		s.append("import ").append(Message.class.getName()).append(";\n");
		
		s.append("/**\n");
		s.append("* FIMET\n");
		s.append("* Code generated automatically\n");
		s.append("**/\n");
		s.append("public class ").append(simpleClassName).append(" extends ").append(AbstractSimulatorExtension.class.getSimpleName()).append(" {\n\n");

		s.append("\t/**\n");
		s.append("\t* @param Message msg the icomming message\n");
		s.append("\t**/\n");
		s.append("\t@Override\n");
		s.append("\tpublic "+ValidationResult.class.getSimpleName()+"[] validateIncomingMessage(ISimulator simulator, Message msg){\n");
		
		int indexSocket = 0;
		StringBuilder sv = new StringBuilder();
		for (SimulatorJson simulator : json.getSimulators()) {
			if (simulator.getExtension() != null && simulator.getExtension().getValidations()!= null&&!simulator.getExtension().getValidations().isEmpty()) {
				SimulatorExtensionJson extension = simulator.getExtension();
				if (extension.getValidations() != null && !extension.getValidations().isEmpty()) {
					sv.append("\t\tif (indexSimulator == "+indexSocket+"){\n");
					sv.append("\t\t\treturn new "+ValidationResult.class.getSimpleName()+"[]{\n");
					for (String v : extension.getValidations()) {
						createValidation(sv, v);
					}
					sv.delete(sv.length()-2, sv.length());
					sv.append("\n\t\t\t};\n");
					sv.append("\t\t}\n");
				}
			}
			indexSocket++;
		}
		if(sv.length() != 0) {
			s.append("\t\tint indexSimulator = indexOf(simulator, msg);\n");
			s.append(sv.toString());
		}
		s.append("\t\treturn null;\n");
		s.append("\t").append("}\n\n");
		
		
		s.append("\t/**\n");
		s.append("\t* This method is executed after Simulator processing\n");
		s.append("\t* @param Message msg is the outgoing message\n");
		s.append("\t**/\n");
		s.append("\t").append("@Override").append("\n");
		s.append("\t").append("public Message simulateOutgoingMessage(ISimulator simulator, Message msg){\n");

		indexSocket = 0;
		StringBuilder ss = new StringBuilder();
		for (SimulatorJson simulator : json.getSimulators()) {
			if (simulator.getExtension() != null && simulator.getExtension().getRules()!= null) {
				ss.append("\t\tif (indexSimulator == "+indexSocket+"){\n");
				SimulatorExtensionJson extension = simulator.getExtension();
				if (extension.getTimeout() != null && extension.getTimeout()) {
					ss.append("\t\t\treturn null;\n");
				} else if (simulator.getExtension().getRules() != null){
					SimulatorRules rules = simulator.getExtension().getRules();
					if (rules.getDel() != null && !rules.getDel().isEmpty()) {
						for (String id : rules.getDel()) {
							ss.append("\t\t\tmsg.remove(\"").append(id).append("\");\n");
						}
					}
					if (rules.getAdd() != null && !rules.getAdd().isEmpty()) {
						for (Entry<String, String> e : rules.getAdd().entrySet()) {
							ss.append("\t\t\tmsg.setValue(\"").append(e.getKey()).append("\",\"").append(e.getValue()).append("\");\n");
						}
					}
				}
				ss.append("\t\t}\n");
			}
			indexSocket++;
		}
		if(ss.length() != 0) {
			s.append("\t\tint indexSimulator = indexOf(simulator, msg);\n");
			s.append(ss.toString());
		}
		s.append("\t\treturn msg;\n");
		s.append("\t").append("}\n");
		s.append("}\n");
		return Manager.get(ICompilerManager.class).compile(className, s.toString());
	}
	private static void createValidation(StringBuilder s, String v) {
		if (v.indexOf('\n') != -1 || v.indexOf(';') != -1) {
			String sv = v.replace("\n", "\\\n").replace("\"", "\\\"");
			s.append("\t\t\t\tnew "+ValidationResult.class.getSimpleName()+"(\""+sv+"\", exe(()->"+v+")),\n");
		} else {
			s.append("\t\t\t\tnew "+ValidationResult.class.getSimpleName()+"(\""+v.replace("\"", "\\\"")+"\", "+v+"),\n");
		}		
	}
}
