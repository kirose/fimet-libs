package com.fimet.json;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import com.fimet.Paths;
import com.fimet.utils.FileUtils;
import com.fimet.utils.UseCaseUtils;
import com.fimet.utils.VariatorUtils;

public class JStressFileBuilderAdapter extends TypeAdapter<JStressFileBuilder> {

	protected final TypeAdapter<JStressFileBuilder> delegate;
	private TypeAdapter<JMessageMap> messageAdapter;
	private TypeAdapter<List<Map<String,String>>> mapAdapter;
	private TypeAdapter<JVariator> variatorAdapter;
	public JStressFileBuilderAdapter(TypeAdapter<JStressFileBuilder> delegate) {
		if (delegate == null) {
			throw new NullPointerException("TypeAdapter delegate is null");
		}
		this.delegate = delegate;
		this.messageAdapter = JExeAdapterFactory.GSON.getAdapter(JMessageMap.class);
		this.mapAdapter = JExeAdapterFactory.GSON.getAdapter(new TypeToken<List<Map<String,String>>>() {});
		this.variatorAdapter = JExeAdapterFactory.GSON.getAdapter(new TypeToken<JVariator>() {});
	}
	@Override
	public JStressFileBuilder read(JsonReader in) throws IOException {
		JStressFileBuilder e = new JStressFileBuilder();
		in.beginObject();
		String name;
 		while (in.hasNext() && in.peek() == JsonToken.NAME) {
			name = in.nextName();
			if ("message".equals(name)) {
				e.setMessage(messageAdapter.read(in));
			} else if ("usecase".equals(name)) {
				String path = in.nextString();
				if (!path.toLowerCase().endsWith(".uc")) {
					path = path+".uc";
				}
				String ucjson = FileUtils.readContents(new File(Paths.USECASES,path));
				JUseCaseJMsg juc = UseCaseUtils.fromStringToJUseCaseJMsg(ucjson);
				e.setMessage(juc.getMessage());
			} else if ("cartesian".equals(name)) {
				Map<String, String[]> fields = readFields(in);
				e.setCartesian(fields);
			} else if ("replacements".equals(name)) {
				List<Map<String,String>> replacements = mapAdapter.read(in);
				e.setReplacements(replacements);
			} else if ("variator".equals(name)) {
				JVariator variator = variatorAdapter.read(in);
				e.setVariator(variator);
			} else {
				//logger.warn("Unknow field "+name);
			}
		}
		in.endObject();
		return e;
	}
	private Map<String,String[]> readFields(JsonReader in) throws IOException{
		Map<String,String[]> fields = new LinkedHashMap<String,String[]>();
		if (in.hasNext() && in.peek() == JsonToken.NULL) {
			in.nextNull();
		}
		in.beginObject();
		String name;
		while (in.hasNext()) {
			name = in.nextName();
			String[] list = readValues(in);
			fields.put(name, list);
		}
		in.endObject();
		return fields;
	}
	private String[] readValues(JsonReader in) throws IOException {
		if (!in.hasNext()) {
			return new String[0];
		}
		List<String> values = new ArrayList<String>();
		JsonToken next = in.peek();
		if (next==JsonToken.BEGIN_ARRAY) {
			in.beginArray();
			String value;
			while(in.hasNext()) {
				value = in.nextString();
				values.add(value);
			}
			in.endArray();
			return values.toArray(new String[values.size()]);
		} else if (next==JsonToken.BEGIN_OBJECT){
			String preappend = null;
			String append = null;
			char pad= ' ';
			Long length = null;
			Long start = null;
			Long end = null;
			String charset = null;
			String name = null;
			in.beginObject();
			while(in.hasNext()) {
				name = in.nextName();
				if ("preappend".equals(name)) {
					preappend = in.nextString();
				} else if ("append".equals(name)) {
					append = in.nextString();
				} else if ("pad".equals(name)) {
					pad = in.nextString().charAt(0);
				} else if ("length".equals(name)) {
					length = in.nextLong();
				} else if ("start".equals(name)) {
					start = in.nextLong();
				} else if ("end".equals(name)) {
					end = in.nextLong();
				} else if ("charset".equals(name)) {
					charset = in.nextString();
				} else {
					throw new IllegalStateException("Unexpected a name " + name + in);
				}
			}
			in.endObject();
			return VariatorUtils.generateRange(start, end, length, pad, charset, preappend, append);
		} else {
			throw new MalformedJsonException("Expected a string or Object" + in);
		}
	}

	@Override
	public void write(JsonWriter out, JStressFileBuilder uc) throws IOException {
		delegate.write(out, uc);
	}
}
