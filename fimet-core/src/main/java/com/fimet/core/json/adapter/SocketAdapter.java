package com.fimet.core.json.adapter;

import java.io.IOException;

//import com.fimet.commons.exception.ParserException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.fimet.commons.exception.ParserException;
import com.fimet.core.IParserManager;
import com.fimet.core.ISimulatorManager;
import com.fimet.core.Manager;
import com.fimet.core.iso8583.adapter.IAdapter;
import com.fimet.core.iso8583.adapter.IAdapterManager;
import com.fimet.core.iso8583.parser.IParser;
import com.fimet.core.net.ISocket;
import com.fimet.core.net.Socket;
import com.fimet.core.simulator.ISimulator;

public class SocketAdapter extends TypeAdapter<ISocket>{

	protected final TypeAdapter<ISocket> delegate;
	public SocketAdapter(TypeAdapter<ISocket> delegate) {
		if (delegate == null) {
			throw new NullPointerException("SocketAdapter delegate is null");
		}
		this.delegate = delegate;
	}
	@Override
	public ISocket read(JsonReader in) throws IOException {
		
		String address = null;
		Integer port = null;
		IAdapter adapter = null;
		IParser parser = null;
		ISimulator simulator = null;
		boolean isServer = false;
		in.beginObject();
		while (in.hasNext() && in.peek() == JsonToken.NAME) {
			String key = in.nextName();
			if ("address".equals(key)) {
				address = in.nextString();
			} else if ("port".equals(key)) {
				port = in.nextInt();
			} else if ("adapter".equals(key)) {
				adapter = Manager.get(IAdapterManager.class).getAdapter(in.nextString());
			} else if ("parser".equals(key)) {
				parser = Manager.get(IParserManager.class).getParser(in.nextString());
			} else if ("simulator".equals(key)) {
				simulator = Manager.get(ISimulatorManager.class).getSimulator(in.nextString());
			} else if ("isServer".equals(key)) {
				isServer = in.nextBoolean();
			} else {
				throw new ParserException("socket: Unexpected field "+in.peek());
			}
		}
		in.endObject();
		/*if (name == null) {
			throw new ParserException("The name of source connection is null");			
		}
		if (address == null) {
			throw new ParserException("The machine address of source connection "+name+" is null");
		}
		if (port == null) {
			throw new ParserException("The socket port of source connection "+name+","+address+" is null");
		}*/
		return new Socket(address, port, isServer, parser, simulator, adapter);
	}
	@Override
	public void write(JsonWriter out, ISocket sc) throws IOException {
		out.beginObject()
	 		.name("address").value(sc.getAddress())
	 		.name("port").value(sc.getPort())
	 		.name("adapter").value(sc.getAdapter().getName())
	 		.name("parser").value(sc.getParser().getName())
	 		.name("simulator").value(sc.getSimulator().getName())
	 		.name("isServer").value(sc.isServer())
		.endObject();
	}
}
