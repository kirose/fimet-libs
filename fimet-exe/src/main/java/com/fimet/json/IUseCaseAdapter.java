
package com.fimet.json;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.fimet.net.ISocket;
import com.fimet.parser.IMessage;
import com.fimet.simulator.ISimulator;
import com.fimet.simulator.NullSimulatorExtension;
import com.fimet.usecase.IUseCase;
import com.fimet.utils.UseCaseUtils;

public class IUseCaseAdapter extends TypeAdapter<IUseCase>{
	protected final TypeAdapter<IUseCase> delegate;
	private TypeAdapter<IMessage> messageAdapter;
	private TypeAdapter<JUseCase> useCaseJsonAdapter;
	public IUseCaseAdapter(TypeAdapter<IUseCase> delegate) {
		if (delegate == null) {
			throw new NullPointerException("TypeAdapter delegate is null");
		}
		this.delegate = delegate;
		this.messageAdapter = JExeAdapterFactory.GSON.getAdapter(IMessage.class);
		this.useCaseJsonAdapter = JExeAdapterFactory.GSON.getAdapter(JUseCase.class);
	}
	@Override
	public IUseCase read(JsonReader in) throws IOException {
		JUseCase json = useCaseJsonAdapter.read(in);
		return UseCaseUtils.fromJson(json);
	}
	@Override
	public void write(JsonWriter out, IUseCase uc) throws IOException {
		if (uc != null) {
			out.beginObject();
			if (uc.getName() != null) {
				out.name("name").value(uc.getName());
			}
			if (uc.getAuthorization() != null) {
				out.name("authorization").value(uc.getAuthorization());
			}
			if (uc.getMessage()!= null) {
				out.name("message");
				messageAdapter.write(out, uc.getMessage());
			}
			if (uc.getSimulatorExtension() != null && uc.getSimulatorExtension() != NullSimulatorExtension.INSTANCE) {
				out.name("simulatorExtension").value(uc.getSimulatorExtension().getClass().getName());
			}
			if (uc.getSimulators() != null && !uc.getSimulators().isEmpty()) {
				out.beginArray();
				for (ISimulator	s : uc.getSimulators()) {
					out.beginObject();
					if (s.getParser() != null) {
						out.name("parser").value(s.getParser().getName());
					}
					if (s.getModel() != null) {
						out.name("model").value(s.getModel().getName());
					}
					if (s.getSocket() != null) {
						ISocket socket = s.getSocket();
						out.name("address").value(socket.getAddress());
						out.name("port").value(socket.getPort());
						out.name("server").value(socket.isServer());
						out.name("adapter").value(socket.getAdapter().getName());
					}
					out.endObject();
				}
				out.endArray();
			}
			out.endObject();
		} else {
			out.nullValue();
		}
	}
}
