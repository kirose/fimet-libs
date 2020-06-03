package com.fimet.json;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.fimet.ISimulatorManager;
import com.fimet.Manager;
import com.fimet.simulator.ESimulator;
import com.fimet.simulator.ISimulator;
import com.fimet.socket.ISocket;

public class SimulatorAdapter extends TypeAdapter<ISimulator>{
	protected final TypeAdapter<ISimulator> delegate;
	private TypeAdapter<ESimulator> simulatorJsonAdapter;
	public SimulatorAdapter(TypeAdapter<ISimulator> delegate) {
		if (delegate == null) {
			throw new NullPointerException("TypeAdapter delegate is null");
		}
		this.delegate = delegate;
		this.simulatorJsonAdapter = JsonAdapterFactory.GSON.getAdapter(ESimulator.class);
	}
	@Override
	public ISimulator read(JsonReader in) throws IOException {
		ESimulator json = simulatorJsonAdapter.read(in);
		return Manager.get(ISimulatorManager.class).getSimulator(json);
	}
	@Override
	public void write(JsonWriter out, ISimulator s) throws IOException {
		if (s != null) {
			out.beginObject();
			if (s.getName() != null)
				out.name("name").value(s.getName());
			if (s.getParser() != null)
				out.name("parser").value(s.getParser().getName());
			if (s.getModel()!= null)
				out.name("model").value(s.getModel().getName());
			if (s.getSocket() != null) {
				ISocket c = s.getSocket();
				if (c.getAdapter() != null)
					out.name("adapter").value(c.getAdapter().getName());
				if (c.getAddress() != null)
					out.name("address").value(c.getAddress());
				if (c.getPort() != null)
					out.name("port").value(c.getPort());
				out.name("server").value(c.server());
			}
			out.endObject();
		} else {
			out.nullValue();
		}
	}
}
