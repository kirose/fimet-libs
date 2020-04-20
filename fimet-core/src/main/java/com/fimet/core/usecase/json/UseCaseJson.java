package com.fimet.core.usecase.json;

import java.util.List;

import com.fimet.core.IClassLoaderManager;
import com.fimet.core.ICompilerManager;
import com.fimet.core.Manager;
import com.fimet.core.iso8583.adapter.IAdapter;
import com.fimet.core.iso8583.parser.IParser;
import com.fimet.core.iso8583.parser.Message;
import com.fimet.core.simulator.ISimulator;
import com.fimet.core.usecase.IUseCase;
import com.fimet.core.validator.AbstractValidator;
import com.fimet.core.validator.IValidator;


public class UseCaseJson {
	private Boolean timeout;
	private Integer delay;
	private boolean isServer;
	private IParser parser;
	private ISimulator simulator;
	private IAdapter adapter;
	private String authorization;
	private MessageJson message;
	private List<SocketJson> connections;
	public UseCaseJson() {
	}
	public Boolean getTimeout() {
		return timeout;
	}
	public void setTimeout(Boolean timeout) {
		this.timeout = timeout;
	}
	public Integer getDelay() {
		return delay;
	}
	public void setDelay(Integer delay) {
		this.delay = delay;
	}
	public boolean isServer() {
		return isServer;
	}
	public void setServer(boolean isServer) {
		this.isServer = isServer;
	}
	public IParser getParser() {
		return parser;
	}
	public void setParser(IParser parser) {
		this.parser = parser;
	}
	public ISimulator getSimulator() {
		return simulator;
	}
	public void setSimulator(ISimulator simulator) {
		this.simulator = simulator;
	}
	public IAdapter getAdapter() {
		return adapter;
	}
	public void setAdapter(IAdapter adapter) {
		this.adapter = adapter;
	}
	public String getAuthorization() {
		return authorization;
	}
	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}
	public MessageJson getMessage() {
		return message;
	}
	public void setMessage(MessageJson message) {
		this.message = message;
	}
	public List<SocketJson> getConnections() {
		return connections;
	}
	public void setConnections(List<SocketJson> connections) {
		this.connections = connections;
	}
	public Class<IValidator> getSourceValidator() {
		int hash = hashValidations();
		String classPackage = "com.fimet.validator.tmp";
		String simpleClassName = "Validator"+Math.abs(hash);
		String className = classPackage+"."+simpleClassName;
		if (Manager.get(IClassLoaderManager.class).isInstalled(className)) {
			try {
				return (Class<IValidator>)Manager.get(IClassLoaderManager.class).loadClass(className);
			} catch (ClassNotFoundException e) {}
		}
		StringBuilder s = new StringBuilder();
		s.append("package ").append(classPackage).append(";\n\n");
		s.append("import ").append(AbstractValidator.class.getName()).append(";\n");
		s.append("import ").append(IUseCase.class.getName()).append(";\n");
		s.append("import ").append(Message.class.getName()).append(";\n");
		
		s.append("\t/**\n");
		s.append("\t* Validator generated automatically\n");
		s.append("\t* Modify this source will not take effect\n");
		s.append("\t**/\n");
		s.append("public class ").append("Validator").append(" extends ").append(AbstractValidator.class.getSimpleName()).append(" {\n");

		s.append("\t").append("public ").append(simpleClassName).append("(UseCase useCase){\n")
			.append("\t\tsuper(useCase);\n")
		.append("\t}\n");
		
		int indexSocket = 0;
		for (SocketJson socket : connections) {
			if (socket.getInValidations()!= null) {
				int indexValidation = 0;
				for (String v : socket.getInValidations()) {
					s.append("\tprivate boolean validationIn"+indexSocket+"_"+indexValidation+"(Message msg){\n");
					if (v.indexOf("return ") >= 0) {
						s.append(v);
					} else {
						s.append("\t\treturn ").append(v).append(";\n");
					}
					s.append("}\n");
					indexValidation++;
				}
			}
			if (socket.getOutValidations()!= null) {
				int indexValidation = 0;
				for (String v : socket.getOutValidations()) {
					s.append("\tprivate boolean validationOut"+indexSocket+"_"+indexValidation+"(Message msg){\n");
					if (v.indexOf("return ") >= 0) {
						s.append(v);
					} else {
						s.append("\t\treturn ").append(v).append(";\n");
					}
					s.append("}\n");
					indexValidation++;
				}
			}
			indexSocket++;
		}
		s.append("\t/**\n");
		s.append("\t* This method is invoked after the issuer reads the request\n");
		s.append("\t* @param Message msg is the issuer request message\n");
		s.append("\t**/\n");
		s.append("\t").append("@Override").append("\n");
		s.append("\t").append("public void onReadMessage(ISocket socket,Message msg){");
		s.append("\t\tint indexSocket = indexOf(socket);\n");
		indexSocket = 0;
		for (SocketJson socket : connections) {
			if (socket.getOutValidations()!= null) {
				int indexValidation = 0;
				s.append("\t\tif (indexSocket == "+indexSocket+"){\n");
				List<String> outValidations = socket.getOutValidations();
				for (String v : outValidations) {
					s.append("\t\t\tvalidation(\""+v.replace("\"", "\\\"")+"\",")
					.append("validationIn"+indexSocket+"_"+indexValidation).append(");\n");
				}
				s.append("\t\t}\n");
			}
		}
		s.append("\t").append("}\n");

		s.append("\t/**\n");
		s.append("\t* This method is invoked after the acquirer reads the response\n");
		s.append("\t* @param Message msg is the acquirer response message\n");
		s.append("\t**/\n");
		s.append("\t").append("@Override").append("\n");
		s.append("\t").append("public void onWriteMessage(ISocket socket, Message msg){");
		s.append("\t\tint indexSocket = indexOf(socket);\n");
		indexSocket = 0;
		for (SocketJson socket : connections) {
			if (socket.getOutValidations()!= null) {
				int indexValidation = 0;
				s.append("\t\tif (indexSocket == "+indexSocket+"){\n");
				List<String> outValidations = socket.getOutValidations();
				for (String v : outValidations) {
					s.append("\t\t\tvalidation(\""+v.replace("\"", "\\\"")+"\",")
					.append("validationOut"+indexSocket+"_"+indexValidation).append(");\n");
				}
				s.append("\t\t}\n");
			}
		}
		s.append("\t").append("}\n");
		s.append("}\n");
		Class<?> classValidator = Manager.get(ICompilerManager.class).compile(className, s.toString());
		return (Class<IValidator>)classValidator;
	}
	public int hashValidations() {
		final int prime = 31;
		int result = 1;
		for (SocketJson s : connections) {
			result = prime * result + s.hashInValidations();
		}
		return result;
	}
}
