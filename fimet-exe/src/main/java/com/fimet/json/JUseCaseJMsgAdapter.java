package com.fimet.json;

import java.io.IOException;
import java.util.List;

import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.fimet.FimetException;
import com.fimet.IParserManager;
import com.fimet.ISimulatorManager;
import com.fimet.Manager;
import com.fimet.parser.IMessage;
import com.fimet.parser.IParser;
import com.fimet.simulator.ISimulator;

public class JUseCaseJMsgAdapter extends TypeAdapter<JUseCaseJMsg>{
	protected final TypeAdapter<JUseCaseJMsg> delegate;
	private TypeAdapter<List<JSimulator>> simulatorsAdapter;
	private TypeAdapter<JMessageMap> messageAdapter;
	private ISimulatorManager simulatorManager = Manager.getContext().getBean(ISimulatorManager.class);
	private IParserManager parserManager = Manager.getContext().getBean(IParserManager.class);
	public JUseCaseJMsgAdapter(TypeAdapter<JUseCaseJMsg> delegate) {
		if (delegate == null) {
			throw new NullPointerException("TypeAdapter delegate is null");
		}
		this.delegate = delegate;
		this.messageAdapter = JExeAdapterFactory.GSON.getAdapter(JMessageMap.class);
		this.simulatorsAdapter = JExeAdapterFactory.GSON.getAdapter(new TypeToken<List<JSimulator>>() {});
	}
	@Override
	public JUseCaseJMsg read(JsonReader in) throws IOException {
		JUseCaseJMsg uc = new JUseCaseJMsg();
		JMessageMap msg = null;
		in.beginObject();
		String name;
		 
 		while (in.hasNext() && in.peek() == JsonToken.NAME) {
			name = in.nextName();
			if ("authorization".equals(name)) {
				uc.setAuthorization(in.nextString());
			} else if ("name".equals(name)) {
				uc.setName(in.nextString());
			} else if ("simulatorExtension".equals(name)) {
				uc.setSimulatorExtension(in.nextString());
			} else if ("simulators".equals(name)) {
				List<JSimulator> simulators = simulatorsAdapter.read(in);
				uc.setSimulators(simulators);
			} else if ("message".equals(name)) {
				msg = messageAdapter.read(in);
			}
		}
		in.endObject();
		if (msg!=null) {
			String parseName = null;
			if (uc.getAcquirer()!=null) {
				if (uc.getAcquirer().getParser()!=null) {
					parseName = uc.getAcquirer().getParser();
				} else {
					ISimulator simulator = simulatorManager.getSimulator(uc.getAcquirer());
					parseName = simulator.getParser().getName();
				}
			} else if (msg.hasProperty(IMessage.PARSER)){
				String parserName = msg.getProperty(IMessage.PARSER);
				parseName = parserName;
			}
			if (parseName == null) {
				throw new FimetException("Cannot found configured Parser for message");
			} 
			IParser parser = parserManager.getParser(parseName);
			if (parser == null) {
				throw new FimetException("Unknow parser "+parseName);
			}
			msg.setProperty(IMessage.PARSER, parser.getName());
			uc.setMessage(msg);
		}

		return uc;
	}
	@Override
	public void write(JsonWriter out, JUseCaseJMsg uc) throws IOException {
		delegate.write(out, uc);
	}
}
