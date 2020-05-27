package com.fimet.utils;

import java.util.List;
import java.util.Map.Entry;

import com.fimet.FimetException;
import com.fimet.FimetLogger;
import com.fimet.IClassLoaderManager;
import com.fimet.ICompilerManager;
import com.fimet.Manager;
import com.fimet.dao.SimulatorMessageDAO;
import com.fimet.entity.ESimulator;
import com.fimet.entity.ESimulatorMessage;
import com.fimet.json.SimulatorExtensionJson;
import com.fimet.json.SimulatorJson;
import com.fimet.json.SimulatorRules;
import com.fimet.json.UseCaseJson;
import com.fimet.parser.IMessage;
import com.fimet.parser.Message;
import com.fimet.pojo.SimulatorField;
import com.fimet.simulator.AbstractSimulatorExtension;
import com.fimet.simulator.AbstractSimulatorModel;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ISimulatorExtension;
import com.fimet.simulator.ISimulatorModel;
import com.fimet.simulator.IValidator;
import com.fimet.simulator.NullSimulatorExtension;
import com.fimet.simulator.ValidationResult;
import com.fimet.usecase.IUseCase;

public final class SimulatorUtils {
	public static final String PACKAGE_EXTENSION = "com.fimet.simulator.extension";
	public static final String PACKAGE_MODEL = "com.fimet.simulator.model";
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
		if (hash == 961L) {
			return NullSimulatorExtension.class;
		}
		String simpleClassName = "SE"+Math.abs(hash);
		String className = PACKAGE_EXTENSION+"."+simpleClassName;
		if (Manager.get(IClassLoaderManager.class).wasInstalled(className)) {
			try {
				return Manager.get(IClassLoaderManager.class).loadClass(className);
			} catch (ClassLoaderException e) {
				throw new FimetException(e);
			}
		}
		StringBuilder s = new StringBuilder();
		s.append("package ").append(PACKAGE_EXTENSION).append(";\n\n");
		s.append("import ").append(AbstractSimulatorExtension.class.getName()).append(";\n");
		s.append("import ").append(ISimulator.class.getName()).append(";\n");
		s.append("import ").append(ValidationResult.class.getName()).append(";\n");
		s.append("import ").append(IValidator.class.getName()).append(";\n");
		s.append("import ").append(IUseCase.class.getName()).append(";\n");
		s.append("import ").append(Message.class.getName()).append(";\n");
		s.append("import ").append(IMessage.class.getName()).append(";\n");
		
		s.append("/**\n");
		s.append("* FIMET\n");
		s.append("* Code generated automatically\n");
		s.append("**/\n");
		s.append("public class ").append(simpleClassName).append(" extends ").append(AbstractSimulatorExtension.class.getSimpleName()).append(" {\n\n");

		s.append("\t/**\n");
		s.append("\t* @param IMessage msg the icomming message\n");
		s.append("\t**/\n");
		s.append("\t@Override\n");
		s.append("\tpublic "+ValidationResult.class.getSimpleName()+"[] validateIncomingMessage(ISimulator simulator, IMessage msg){\n");
		
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
		s.append("\t* @param IMessage msg is the outgoing message\n");
		s.append("\t**/\n");
		s.append("\t").append("@Override").append("\n");
		s.append("\t").append("public IMessage simulateOutgoingMessage(ISimulator simulator, IMessage msg){\n");

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
	@SuppressWarnings("unchecked")
	public static Class<ISimulatorModel> getClassSimulatorModel(ESimulator simulator) {
		String simpleClassName = "SM"+Math.abs(simulator.getName().hashCode());
		String className = PACKAGE_MODEL+"."+simpleClassName;
		if (Manager.get(IClassLoaderManager.class).wasInstalled(className)) {
			try {
				return (Class<ISimulatorModel>)Manager.get(IClassLoaderManager.class).loadClass(className);
			} catch (ClassLoaderException e) {
				throw new FimetException(e);
			}
		}
		return installClassSimulatorModel(simulator);
	}
	@SuppressWarnings("unchecked")
	public static Class<ISimulatorModel> installClassSimulatorModel(ESimulator simulator) {
		String simpleClassName = "SM"+Math.abs(simulator.getName().hashCode());
		String className = PACKAGE_MODEL+"."+simpleClassName;
		StringBuilder s = new StringBuilder();
		s.append("package ").append(PACKAGE_MODEL).append(";\n\n");
		s.append("import ").append(ISimulatorModel.class.getName()).append(";\n");
		s.append("import ").append(AbstractSimulatorModel.class.getName()).append(";\n");
		s.append("import ").append(Message.class.getName()).append(";\n");
		s.append("import ").append(IMessage.class.getName()).append(";\n");
		
		s.append("/**\n");
		s.append("* FIMET\n");
		s.append("* Code generated automatically\n");
		s.append("**/\n");
		s.append("public class ").append(simpleClassName).append(" extends ").append(AbstractSimulatorModel.class.getSimpleName()).append(" {\n\n");
		s.append("\tpublic "+simpleClassName+" () {\n\t\tsuper(\""+simulator.getName()+"\");\n\t}\n\n");

		StringBuilder sreq = new StringBuilder();
		StringBuilder sres = new StringBuilder();
		StringBuilder sc = null;
		List<ESimulatorMessage> simulators = SimulatorMessageDAO.getInstance().findByIdSimulator(simulator.getId());
		if (simulators != null && !simulators.isEmpty()) {
			for (ESimulatorMessage sm : simulators) {
				if (sm.getType() == ESimulatorMessage.REQUEST) {
					sc = sreq;
				} else if (sm.getType() == ESimulatorMessage.RESPONSE) {
					sc = sres;
				} else {
					throw new FimetException("Invalid Simulator type "+sm.getType());
				}
				sc.append("\t\tif (\""+sm.getMti()+"\".equals(mti)){\n");
				if (sm.getType() == ESimulatorMessage.RESPONSE) {
					sc.append("\t\t\tmsg = cloneMessage(msg);\n");
					sc.append("\t\t\tmsg.setProperty(\""+Message.MTI+"\",String.format(\"%04d\", Integer.parseInt(mti)+10));\n");
				}
				if (sm.getHeader() != null) {
					sc.append("\t\t\tmsg.setHeader(\""+sm.getHeader()+"\");\n");
				}
				if (sm.getExcludeFields() != null && !sm.getExcludeFields().isEmpty()) {
					sc.append("\t\t\tmsg.removeAll(");
					for (String idField : sm.getExcludeFields()) {
						sc.append("\""+idField+"\",");
					}
					sc.delete(sc.length()-1, sc.length());
					sc.append(");\n");
				}
				if (sm.getIncludeFields() != null && !sm.getIncludeFields().isEmpty()) {
					for (SimulatorField f : sm.getIncludeFields()) {
						if (f.getType() == SimulatorField.FIXED) {
							sc.append("\t\t\tmsg.setValue(\""+f.getIdField()+"\",\""+f.getValue()+"\");\n");
						} else if (f.getType() == SimulatorField.CUSTOM) {
							sc.append("\t\t\t"+f.getValue()+".getInstance().simulate(msg,\""+f.getIdField()+"\");\n");
						} else {
							throw new FimetException("Invalid Simulator Field type "+sm.getType());
						}
					}
				}
				sc.append("\t\t\treturn msg;\n");
				sc.append("\t\t}\n");
			}
		}
		s.append("\t/**\n");
		s.append("\t* @param IMessage msg the outgoing message\n");
		s.append("\t**/\n");
		s.append("\t@Override\n");
		s.append("\tpublic IMessage simulateRequest(IMessage msg){\n");
		if (sreq.length() > 0) {
			s.append("\t\tString mti = (String)msg.getProperty(\""+Message.MTI+"\");\n");
			s.append(sreq);
		}
		s.append("\t\treturn msg;\n");
		s.append("\t").append("}\n\n");
		
		
		s.append("\t/**\n");
		s.append("\t* @param IMessage msg is the incoming message\n");
		s.append("\t**/\n");
		s.append("\t@Override\n");
		s.append("\tpublic IMessage simulateResponse(IMessage msg){\n");
		if (sres.length() > 0) {
			s.append("\t\tString mti = (String)msg.getProperty(\""+Message.MTI+"\");\n");
			s.append(sres);
		}
		s.append("\t\treturn null;\n");
		s.append("\t").append("}\n");
		s.append("}\n");
		Class<?> compile = Manager.get(ICompilerManager.class).compile(className, s.toString());
		return (Class<ISimulatorModel>)compile;		
	}
}
