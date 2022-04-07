package com.fimet;

import static com.fimet.ISimulatorManager.PACKAGE_EXTENSIONS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fimet.IClassLoaderManager;
import com.fimet.ISimulatorModelManager;
import com.fimet.Manager;
import com.fimet.assertions.IAssertion;
import com.fimet.assertions.IAssertionMaker;
import com.fimet.assertions.IAssertionResult;
import com.fimet.dao.ISimulatorMessageDAO;
import com.fimet.dao.ISimulatorModelDAO;
import com.fimet.json.JSimulator;
import com.fimet.json.JSimulatorExtension;
import com.fimet.parser.IMessage;
import com.fimet.parser.Message;
import com.fimet.simulator.AbstractSimulatorExtension;
import com.fimet.simulator.AbstractSimulatorModel;
import com.fimet.simulator.IESimulator;
import com.fimet.simulator.IESimulatorMessage;
import com.fimet.simulator.IESimulatorModel;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.ISimulatorExtension;
import com.fimet.simulator.ISimulatorModel;
import com.fimet.simulator.NullSimulatorExtension;
import com.fimet.simulator.NullSimulatorModel;
import com.fimet.simulator.SimulatorException;
import com.fimet.simulator.SimulatorField;
import com.fimet.simulator.SimulatorModelWrapper;
import com.fimet.simulator.field.*;
import com.fimet.utils.ClassLoaderException;
import com.fimet.utils.ClassModel;

/**
 * 
 * @author Marco A. Salazar
 * @email marcoasb99@ciencias.unam.mx
 */
@Component
public class SimulatorModelManager implements ISimulatorModelManager {
	private static Logger logger = LoggerFactory.getLogger(SimulatorModelManager.class);
	@Autowired private ICompilerManager compilerManager;
	@Autowired private ISimulatorModelDAO<? extends IESimulatorModel> dao;
	@Autowired private IClassLoaderManager classLoaderManager;
	@Autowired private ISimulatorMessageDAO<? extends IESimulatorMessage> simulatorMessageDAO;
	private Map<String, SimulatorModelWrapper> mapNameSimulator = new HashMap<>();
	private Map<String, Class<ISimulatorModel>> classes = new HashMap<>();
	public SimulatorModelManager() {
	}
	@PostConstruct
	public void start() {
		reload();
		mapNameSimulator.put("None", new SimulatorModelWrapper(NullSimulatorModel.INSTANCE));
	}
	@Override
	public void reload() {
		mapNameSimulator.clear();
		classes.clear();
		loadClases();
	}
	@SuppressWarnings("unchecked")
	private void loadClases() {
		boolean recompileModels = Manager.getPropertyBoolean("simulator.model.recompile",true);
		List<? extends IESimulatorModel> entities = dao.findAll();
		if (entities!=null) {
			for (IESimulatorModel s : entities) {
				Class<ISimulatorModel> clazz = null;
				if (recompileModels || !classLoaderManager.wasInstalled(s.getClassModel())) {
					clazz = installClassSimulatorModel(s);
				} else {
					clazz = (Class<ISimulatorModel>)classLoaderManager.loadClass(s.getClassModel());
				}
				classes.put(s.getName(), clazz);
			}
		}
	} 
	@Override
	public void reload(String name) {
		IESimulatorModel entity = dao.findByName(name);
		if (entity != null) {
			Class<ISimulatorModel> clazz = reinstallClassSimulatorModel(entity);
			classes.put(name, clazz);
			SimulatorModelWrapper wrapper = mapNameSimulator.get(name);
			if (wrapper != null) {
				ISimulatorModel instance;
				try {
					instance = clazz.newInstance();
				} catch (Exception e) {
					throw new SimulatorException("Invalid Simulator Model "+name,e);
				}
				logger.debug("Reloaded Simulator Model "+name);
				wrapper.setWapped(instance);
			} else {
				logger.debug("Unkown Simulator Model Wrapper is null for "+name);
			}
		} else if (mapNameSimulator.containsKey(name)) {
			mapNameSimulator.remove(name).setWapped(NullSimulatorModel.INSTANCE);
		} else {
			logger.debug("Unkown Simulator Model Wrapper "+name);
		}
	}
	public ISimulatorModel getSimulatorModel(String name) {
		if (mapNameSimulator.containsKey(name)) {
			return mapNameSimulator.get(name);
		} else if (classes.containsKey(name)){
			ISimulatorModel instance;
			try {
				instance = classes.get(name).newInstance();
			} catch (Exception e) {
				throw new SimulatorException("Invalid simulator model "+name,e);
			}
			SimulatorModelWrapper wapper = new SimulatorModelWrapper(instance);
			mapNameSimulator.put(name, wapper);
			return wapper;
		} else {
			throw new SimulatorException("Unkown simulator model "+name);
		}
	}
	@Override
	public Class<?>[] getSimulatorFieldClasses() {
		return new Class<?>[] {
			IfHasSetNewDateyyMMddhhmmss.class,
			IfHasSetNewDateMMddhhmmss.class,
			IfHasSetNewDatehhmmss.class,
			IfHasSetNewDateMMdd.class,
			IfHasSetAmount.class,
			IfHasSetEntryMode.class,
			IfHasSetCorrectPanLastDigit.class,
			IfHasSetPanLast4Digits.class,
			IfHasSetRRN.class,
			IfHasSetModuloExtranjero.class,
			IfHasSetRandom12N.class,
			IfHasSetRandom6N.class,
			IfHasSetRandom12AN.class,
			IfHasSetRandom6AN.class,
			SetRandom15N.class,
			SetRandom12N.class,
			SetRandom6N.class,
			SetRandom9N.class,
			SetRandom12AN.class,
			SetRandom6AN.class,
			SetNewDateyyMMddhhmmss.class,
			SetNewDateMMddhhmmss.class,
			SetNewDatehhmmss.class,
			SetNewDateMMdd.class
		};
	}
	@Override
	public void stop() {
	}
	public long hashCodeExtensions(List<JSimulator> simulators) {
		final int prime = 31;
		long result = 1;
		if (simulators != null && !simulators.isEmpty()) {
			for (JSimulator s : simulators) {
				result = prime * result + (s.getName().hashCode());
				result = prime * result + (s.getExtension() == null ? 0 : s.getExtension().hashCodeExtension());
			}
		}
		return result;
	}
	public ISimulatorExtension newSimulatorExtension(List<JSimulator> simulators) {
		Class<?> clazz = null;
		try {
			clazz = getClassValidator(simulators);
		} catch (Exception e) {
			logger.error("Error compiling simulator extension, will be use default extension", e);
			return NullSimulatorExtension.INSTANCE;
		}
		try {
			return (ISimulatorExtension)clazz.newInstance();
		} catch (Exception e) {
			logger.error("Error instantianting validator class "+clazz.getName()+", will be use default extension", e);
			return NullSimulatorExtension.INSTANCE;
		}
	}

	public Class<?> getClassValidator(List<JSimulator> simulators) {
		long hash = hashCodeExtensions(simulators);
		if (hash == 961L) {
			return NullSimulatorExtension.class;
		}
		String simpleClassName = "SE"+Math.abs(hash);
		String className = PACKAGE_EXTENSIONS+"."+simpleClassName;
		if (classLoaderManager.wasInstalled(className)) {
			try {
				return classLoaderManager.loadClass(className);
			} catch (ClassLoaderException e) {
				throw new FimetException(e);
			}
		}
		StringBuilder s = new StringBuilder();
		s.append("package ").append(PACKAGE_EXTENSIONS).append(";\n\n");
		s.append("import ").append(AbstractSimulatorExtension.class.getName()).append(";\n");
		s.append("import ").append(ISimulator.class.getName()).append(";\n");
		String packageAssertions = IAssertion.class.getName().substring(0,IAssertion.class.getName().length()-IAssertion.class.getSimpleName().length());
		s.append("import ").append(packageAssertions).append("*;\n");
		s.append("import static ").append(packageAssertions).append("Assertions.*").append(";\n");
		s.append("import ").append(IAssertionMaker.class.getName()).append(";\n");
		s.append("import ").append("com.fimet.usecase.IUseCase").append(";\n");
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
		s.append("\tpublic "+IAssertionResult.class.getSimpleName()+"[] validateIncomingMessage(ISimulator simulator, IMessage msg){\n");
		
		StringBuilder sv = new StringBuilder();
		for (JSimulator simulator : simulators) {
			if (simulator.getExtension() != null && simulator.getExtension().getValidations()!= null&&!simulator.getExtension().getValidations().isEmpty()) {
				JSimulatorExtension extension = simulator.getExtension();
				if (extension.getValidations() != null && !extension.getValidations().isEmpty()) {
					sv.append("\t\tif (\""+simulator.getName()+"\".equals(simulator.getName())){\n");
					sv.append("\t\t\treturn new "+IAssertionResult.class.getSimpleName()+"[]{\n");
					for (Map.Entry<String,String> v : extension.getValidations().entrySet()) {
						createValidation(sv, v.getKey(), v.getValue());
					}
					sv.delete(sv.length()-2, sv.length());
					sv.append("\n\t\t\t};\n");
					sv.append("\t\t}\n");
				}
			}
		}
		if(sv.length() != 0) {
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

		StringBuilder ss = new StringBuilder();
		for (JSimulator simulator : simulators) {
			JSimulatorExtension extension = simulator.getExtension();
			if (extension != null&&(extension.getTimeout() != null||extension.getAdd()!=null||extension.getDel()!=null)) {
				ss.append("\t\tif (\""+simulator.getName()+"\".equals(simulator.getName())){\n");
				if (extension.getTimeout() != null && extension.getTimeout()) {
					ss.append("\t\t\treturn null;\n");
				} else {
					if (extension.getDel() != null && !extension.getDel().isEmpty()) {
						for (String id : extension.getDel()) {
							ss.append("\t\t\tmsg.remove(\"").append(id).append("\");\n");
						}
					}
					if (extension.getAdd() != null && !extension.getAdd().isEmpty()) {
						for (Entry<String, String> e : extension.getAdd().entrySet()) {
							ss.append("\t\t\tmsg.setValue(\"").append(e.getKey()).append("\",\"").append(e.getValue()).append("\");\n");
						}
					}
				}
				ss.append("\t\t}\n");
			}
		}
		if(ss.length() != 0) {
			s.append(ss.toString());
		}
		s.append("\t\treturn msg;\n");
		s.append("\t").append("}\n");
		s.append("}\n");
		return compilerManager.compileAndLoad(className, s.toString());
	}
	private void createValidation(StringBuilder s, String name, String v) {
		name = escapeJavaString(name);
		if (v.startsWith("(m)->{")) {
			s.append("\t\t\t\texe(\""+name+"\",msg,"+v+"),\n");
		} else {
			s.append("\t\t\t\texe(\""+name+"\",msg,(m)->{return "+v+";}),\n");
		}
	}
	private String escapeJavaString(String s) {
		return s.replace("\\", "\\").replace("\"", "\\\"");
	}
	@SuppressWarnings("unchecked")
	public Class<ISimulatorModel> getClassSimulatorModel(IESimulatorModel simulator) {
		if (classLoaderManager.wasInstalled(simulator.getClassModel())) {
			try {
				return (Class<ISimulatorModel>)classLoaderManager.loadClass(simulator.getClassModel());
			} catch (ClassLoaderException e) {
				throw new FimetException(e);
			}
		}
		return installClassSimulatorModel(simulator);
	}
	public ClassModel classModelFromSimulatorModel(IESimulatorModel simulator) {
		if (simulator.getClassModel() == null) {
			throw new FimetException("Class name is null for simulator "+simulator.getName());
		}
		List<? extends IESimulatorMessage> simulators = simulatorMessageDAO.findByModelName(simulator.getName());
		String simpleClassName;
		int index = simulator.getClassModel().lastIndexOf('.');
		String packageName;
		if (index != -1) {
			packageName = simulator.getClassModel().substring(0,index);
			simpleClassName = simulator.getClassModel().substring(index+1);
		} else {
			packageName = "";
			simpleClassName = simulator.getClassModel();
		}
		String className = simulator.getClassModel();

		StringBuilder s = new StringBuilder();
		s.append("package ").append(packageName).append(";\n\n");
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
		
		if (simulators != null && !simulators.isEmpty()) {
			for (IESimulatorMessage sm : simulators) {
				if (sm.getType().equals(IESimulatorMessage.REQUEST)) {
					sc = sreq;
				} else if (sm.getType().equals(IESimulatorMessage.RESPONSE)) {
					sc = sres;
				} else {
					throw new FimetException("Invalid Simulator type "+sm.getType());
				}
				sc.append("\t\tif (\""+sm.getMti()+"\".equals(mti)){\n");
				if (sm.getType().equals(IESimulatorMessage.RESPONSE)) {
					sc.append("\t\t\tmsg = cloneMessage(msg);\n");
					sc.append("\t\t\tmsg.setProperty(\""+IMessage.MTI+"\",createMtiResponse(mti));\n");
				}
				if (sm.getHeader() != null) {
					sc.append("\t\t\tmsg.setProperty(\""+IMessage.HEADER+"\",\""+sm.getHeader()+"\");\n");
				}
				if (sm.getDelFields() != null && !sm.getDelFields().isEmpty()) {
					sc.append("\t\t\tmsg.removeAll(");
					for (String idField : sm.getDelFields()) {
						sc.append("\""+idField+"\",");
					}
					sc.delete(sc.length()-1, sc.length());
					sc.append(");\n");
				}
				if (sm.getAddFields() != null && !sm.getAddFields().isEmpty()) {
					for (SimulatorField f : sm.getAddFields()) {
						if (f.getClassName() != null) {
							sc.append("\t\t\t"+f.getClassName()+".getInstance().simulate(msg,\""+f.getIdField()+"\");\n");
						} else {
							sc.append("\t\t\tmsg.setValue(\""+f.getIdField()+"\",\""+f.getValue()+"\");\n");
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
			s.append("\t\tString mti = (String)msg.getProperty(\""+IMessage.MTI+"\");\n");
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
			s.append("\t\tString mti = (String)msg.getProperty(\""+IMessage.MTI+"\");\n");
			s.append(sres);
		}
		s.append("\t\treturn null;\n");
		s.append("\t").append("}\n");
		s.append("}\n");
		return new ClassModel(className, s.toString());
	}
	@SuppressWarnings("unchecked")
	public Class<ISimulatorModel> installClassSimulatorModel(IESimulatorModel simulator) {
		ClassModel classModel = classModelFromSimulatorModel(simulator);
		Class<?> compile = compilerManager.compileAndLoad(classModel.getClassName(), classModel.getSource());
		return (Class<ISimulatorModel>)compile;		
	}
	@SuppressWarnings("unchecked")
	public Class<ISimulatorModel> reinstallClassSimulatorModel(IESimulatorModel simulator) {
		ClassModel classModel = classModelFromSimulatorModel(simulator);
		Class<?> compile = compilerManager.compileAndReload(classModel.getClassName(), classModel.getSource());
		return (Class<ISimulatorModel>)compile;		
	}
	public JSimulator toJSimulator(ISimulator s) {
		if (s!=null) {
			JSimulator js = new JSimulator();
			js.setName(s.getName());
			js.setParser(s.getParser().getName());
			js.setModel(s.getModel().getName());
			js.setAddress(s.getSocket().getAddress());
			js.setPort(s.getSocket().getPort());
			js.setServer(s.getSocket().isServer());
			js.setAdapter(s.getSocket().getAdapter().getName());
			return js;
		}
		return null;
	}
	public JSimulator copy(ISimulator from, JSimulator to) {
		to.setName(from.getName());
		to.setParser(from.getParser().getName());
		to.setModel(from.getModel().getName());
		to.setAddress(from.getSocket().getAddress());
		to.setPort(from.getSocket().getPort());
		to.setServer(from.getSocket().isServer());
		to.setAdapter(from.getSocket().getAdapter().getName());
		return to;
	}
	public JSimulator copy(IESimulator from, JSimulator to) {
		to.setName(from.getName());
		to.setParser(from.getParser());
		to.setModel(from.getModel());
		to.setAddress(from.getAddress());
		to.setPort(from.getPort());
		to.setServer(from.isServer());
		to.setAdapter(from.getAdapter());
		return to;
	}
	public String createValidationSource(String packageName, String simpleClassName, String validation) {
		String block = extractBlock(validation, false);
		StringBuilder s = new StringBuilder();
		String packageAssertions = IAssertion.class.getName().substring(0,IAssertion.class.getName().length()-IAssertion.class.getSimpleName().length());
		s
		.append("package ").append(packageName).append(";\n\n")
		.append("import ").append(IMessage.class.getName()).append(";\n")
		.append("import ").append(packageAssertions).append("*;\n")
		.append("import ").append(packageAssertions).append("Assertions.*").append(";\n")
		.append("/**\n")
		.append("* FIMET\n")
		.append("* Code generated automatically\n")
		.append("**/\n")
		.append("public class ").append(simpleClassName).append(" implements ").append(IAssertionMaker.class.getSimpleName()).append(" {\n")
		.append("\tpublic IAssertion make(IMessage m) {\n")
		.append("\t\t").append(block).append("\n")
		.append("\t}\n")
		.append("}")
		;
		
		return s.toString();
	}
	private String extractBlock(String validation, boolean fail) {
		validation = removeBlanks(validation);
		int start = validation.indexOf('{');
		if (start==-1) {
			if (!validation.startsWith("return")) {// Single expression
				validation = "return "+validation;
			}
			if (!validation.endsWith(";")){
				validation = validation+";";
			}
			return validation;
		}
		int end = validation.lastIndexOf('}');
		if (start < end) {
			String block = validation.substring(start+1, end);
			return removeBlanks(block);
		} else {
			if (fail) {
				return error("Expected left braket { before }:"+validation+" expecte");
			} else {
				return validation;
			}
		}
	}
	public String classSourceParseName(String source) {
		String block = parseNameBlock(source);
		if (block==null)
			return error("Invalid source"); 
		block = removeBlanks(block);
		if (block.startsWith("return \"")&&block.endsWith("\";")) {// Single expression
			block = block.substring(8, block.length()-2);
			return block;
		} else {//Anonymous function
			return error("Expected String constant declaration (example: \"simple name\") insted: "+block);	
		}
	}
	public String classSourceParseAssertion(String source) {
		String block = parseAssertionBlock(source);
		if (block==null)
			return error("Invalid source"); 
		block = removeBlanks(block);
		if (block.startsWith("return")&&block.endsWith(";")) {// Single expression
			block = block.substring(6, block.length()-1);
			block = removeBlanks(block);
			return block;
		} else {//Anonymous function
			return "(m)->{"+block+"}";
		}
	}
	private String error(String message) {
		throw new FimetException(message); 
	}
	@SuppressWarnings("deprecation")
	private String parseNameBlock(String source){
		String signatureName = "public String getName()";
		int start = source.indexOf(signatureName);
		if (start==-1)
			return error("Expected method signature: \""+signatureName+"\"");
		String signatureAssertion = "public IAssertion make(IMessage m)";
		int ln = source.indexOf(signatureAssertion);
		if (ln==-1)
			return error("Expected method signature: \""+signatureAssertion+"\"");
		char c;
		int i = start+signatureName.length();
		while (i<ln) {
			c = source.charAt(i++);
			if (c == '{') {
				start = i;
				break;
			} else if (!Character.isSpace(c)){
				return error("Unexpected token at "+i+": "+c+source.substring(i,Math.min(20, ln-i)));
			}
		}
		i = ln;
		int end=-1;
		while (end==-1&&i>0) {
			c = source.charAt(--i);
			if (c == '}') end = i;
		}
		if (end==-1) {
			return error("Invalid source expected method close block }");			
		}
		if (start < end) {
			return source.substring(start, end);
		} else {
			return error("Invalid source validation "+source);
		}
	}
	@SuppressWarnings("deprecation")
	private String parseAssertionBlock(String source){
		String signature = "public IAssertion make(IMessage m)";
		int start = source.indexOf(signature);
		if (start==-1)
			return error("Expected method signature: \""+signature+"\"");
		char c;
		int ln = source.length();
		int i = start+signature.length();
		while (i<ln) {
			c = source.charAt(i++);
			if (c == '{') {
				start = i;
				break;
			} else if (!Character.isSpace(c)){
				return error("Unexpected token at "+i+": "+c+source.substring(i,Math.min(20, ln-i)));
			}
		}
		i = ln;
		int end=-1;
		while (end==-1&&i>0) {
			c = source.charAt(--i);
			if (c == '}') {
				while (end==-1&&i>0) {
					c = source.charAt(--i);
					if (c == '}') end = i;
				}
			}
		}
		if (end==-1) {
			return error("Invalid source expected method close block }");			
		}
		if (start < end) {
			return source.substring(start, end);
		} else {
			return error("Invalid source validation "+source);
		}
	}
	@SuppressWarnings("deprecation")
	private String removeBlanks(String block) {
		int ln = block.length();
		int i = 0;
		while (i<ln&&Character.isSpace(block.charAt(i++))) {}
		int start = i-1;
		i = ln;
		while (i>0&&Character.isSpace(block.charAt(--i))) {}
		int end = i+1;
		if (start < end) {
			return block.substring(start, end);
		} else {
			return null;
		}
	}
	public int hasCode(IESimulator s) {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((s.getName() == null) ? 0 : s.getName().hashCode());
		result = prime * result + ((s.getAddress() == null) ? 0 : s.getAddress().hashCode());
		result = prime * result + ((s.getPort() == null) ? 0 : s.getPort().hashCode());
		result = prime * result + (s.isServer() ? 1231 : 1237);
		return result;
	}
	public boolean equals(IESimulator s1, IESimulator s2) {
		if (s1 == s2)
			return true;
		if (s1 == null)
			return false;
		if (s1.getName() == null) {
			if (s2.getName() != null)
				return false;
		} else if (s1.getName().equals(s2.getName()))
			return true;
		if (s1.getAddress() == null) {
			if (s2.getAddress() != null)
				return false;
		} else if (!s1.getAddress().equals(s2.getAddress()))
			return false;
		if (s1.getPort() == null) {
			if (s2.getPort() != null)
				return false;
		} else if (!s1.getPort().equals(s2.getPort()))
			return false;
		if (s1.isServer() != s2.isServer())
			return false;
		return true;
	}
	public boolean equals(ISimulator s1, ISimulator s2) {
		if (s1 == s2)
			return true;
		if (s1 == null)
			return false;
		if (s1.getName() == null) {
			if (s2.getName() != null)
				return false;
		} else if (s1.getName().equals(s2.getName()))
			return true;
		if (s1.getSocket().getAddress() == null) {
			if (s2.getSocket().getAddress() != null)
				return false;
		} else if (!s1.getSocket().getAddress().equals(s2.getSocket().getAddress()))
			return false;
		if (s1.getSocket().getPort() == null) {
			if (s2.getSocket().getPort() != null)
				return false;
		} else if (!s1.getSocket().getPort().equals(s2.getSocket().getPort()))
			return false;
		if (s1.getSocket().isServer() != s2.getSocket().isServer())
			return false;
		return true;
	}
	public boolean equals(ISimulator s1, IESimulator s2) {
		if (s1 == s2)
			return true;
		if (s1 == null)
			return false;
		if (s1.getName() == null) {
			if (s2.getName() != null)
				return false;
		} else if (s1.getName().equals(s2.getName()))
			return true;
		if (s1.getSocket().getAddress() == null) {
			if (s2.getAddress() != null)
				return false;
		} else if (!s1.getSocket().getAddress().equals(s2.getAddress()))
			return false;
		if (s1.getSocket().getPort() == null) {
			if (s2.getPort() != null)
				return false;
		} else if (!s1.getSocket().getPort().equals(s2.getPort()))
			return false;
		if (s1.getSocket().isServer() != s2.isServer())
			return false;
		return true;
	}
	public boolean equals(Object s1, Object s2) {
		if (s1 instanceof ISimulator) {
			if (s2 instanceof ISimulator) {
				return equals((ISimulator)s1,(ISimulator)s2);
			} else if (s2 instanceof IESimulator) {
				return equals((ISimulator)s1,(IESimulator)s2);
			} else {
				return s1.equals(s2);
			}
		} else if (s1 instanceof IESimulator) {
			if (s2 instanceof ISimulator) {
				return equals((ISimulator)s2,(IESimulator)s1);
			} else if (s2 instanceof IESimulator) {
				return equals((IESimulator)s1,(IESimulator)s2);
			} else {
				return s1.equals(s2);
			}
		} else {
			return s1.equals(s2);
		}
	}
	@Override
	public List<String> getNames() {
		return classes.entrySet().stream().map(e->e.getKey()).collect(Collectors.toList());
	}
}
