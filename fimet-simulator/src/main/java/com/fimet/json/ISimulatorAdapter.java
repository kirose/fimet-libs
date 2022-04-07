package com.fimet.json;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.fimet.ISimulatorManager;
import com.fimet.Manager;
import com.fimet.net.ISocket;
import com.fimet.simulator.ESimulatorXml;
import com.fimet.simulator.ISimulator;

public class ISimulatorAdapter extends TypeAdapter<ISimulator>{
	protected final TypeAdapter<ISimulator> delegate;
	private TypeAdapter<ESimulatorXml> simulatorJsonAdapter;
	public ISimulatorAdapter(TypeAdapter<ISimulator> delegate) {
		if (delegate == null) {
			throw new NullPointerException("TypeAdapter delegate is null");
		}
		this.delegate = delegate;
		this.simulatorJsonAdapter = JMessageAdapterFactory.GSON.getAdapter(ESimulatorXml.class);
	}
	@Override
	public ISimulator read(JsonReader in) throws IOException {
		ESimulatorXml json = simulatorJsonAdapter.read(in);
		return Manager.getManager(ISimulatorManager.class).getSimulator(json);
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
				out.name("server").value(c.isServer());
			}
			out.endObject();
		} else {
			out.nullValue();
		}
	}
}
