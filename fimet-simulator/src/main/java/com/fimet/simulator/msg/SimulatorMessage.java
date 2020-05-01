package com.fimet.simulator.msg;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.fimet.commons.exception.FimetException;
import com.fimet.commons.FimetLogger;
import com.fimet.iso8583.parser.Message;
import com.fimet.simulator.ISimulatorModel;

public abstract class SimulatorMessage {
	protected String header;
	protected String mti;
	protected char type;
	protected List<String> excludeFields;
	protected List<SimulatorField> simulatedFields;
	protected ISimulatorModel simulator;
	public SimulatorMessage(ISimulatorModel simulator, com.fimet.entity.sqlite.ESimulatorMessage sm) {
		super();
		this.simulator = simulator;
		mti = sm.getMti();
		type = sm.getType();
		excludeFields = sm.getExcludeFields();
		header = sm.getHeader();
		List<com.fimet.entity.sqlite.pojo.SimulatorField> incflds = sm.getIncludeFields();
		if (incflds != null && !incflds.isEmpty()) {
			simulatedFields = new ArrayList<>();
			for (com.fimet.entity.sqlite.pojo.SimulatorField sf : incflds) {
				SimulatorField simulatorField = null;
				switch (sf.getType()) {
				case com.fimet.entity.sqlite.pojo.SimulatorField.FIXED:
					simulatorField = new SimulatorFieldFixed(sf.getIdField(), sf.getValue());
					break;
				case com.fimet.entity.sqlite.pojo.SimulatorField.CUSTOM:
					ISimulatorField isf = null;
					String className = sf.getValue();
					if (sf.getValue() != null && sf.getValue().trim().length() > 0) {
						try {
							Method m = Class.forName(className).getMethod("getInstance");
							isf = (ISimulatorField)m.invoke(null);
							simulatorField = new SimulatorFieldCustom(sf.getIdField(), isf);
						} catch (NoSuchMethodException e) {
							FimetLogger.error("The class "+className + " must have the static method 'getInstance()'" , e);
						} catch (SecurityException e) {
							FimetLogger.error("The class "+className + " must have the static method 'getInstance()'" , e);
						} catch (IllegalAccessException e) {
							FimetLogger.error("The class "+className + " must have the static method 'getInstance()' as public" , e);
						} catch (IllegalArgumentException e) {
							FimetLogger.error("The class "+className + " must have the static method 'getInstance()' without arguments" , e);
						} catch (InvocationTargetException e) {
							FimetLogger.error("The class "+className + " must have the static method 'getInstance()'" , e);
						} catch (ClassNotFoundException e) {
							FimetLogger.error("Cannot found class "+className + "" , e);
						}
					}
					break;
				}
				if (simulatorField != null) {
					simulatedFields.add(simulatorField);
				} else {
					throw new FimetException("Not yet supported type '"+sf.getType()+"'");
				}
			}
		}
	}
	abstract public Message simulate(Message message);
}
