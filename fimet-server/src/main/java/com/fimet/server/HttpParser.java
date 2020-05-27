package com.fimet.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Date;

import com.fimet.FimetException;
import com.fimet.FimetLogger;
import com.fimet.Version;
import com.fimet.utils.ByteUtils;
import com.fimet.utils.DateUtils;
import com.fimet.utils.FileUtils;
import com.fimet.utils.data.ByteBuilder;

public class HttpParser {

	private byte[] buffer;
	private int lengthBuffer;
	private int positionBuffer;
	private InputStream input;
	private HttpMessage message;
	private ByteBuilder line;
	private ByteBuilder body;
	public HttpParser() {
		buffer = new byte[1024];
		line = new ByteBuilder();
		body = new ByteBuilder();
	}
	public HttpMessage parse(InputStream in) throws IOException {
		try {
			reset();
			input = in;
			lengthBuffer = input.read(buffer);
			HttpMessage m = message = new HttpMessage();
			String line = readStringLine();
			//System.out.print(line);
		    if (line == null || line.length() == 0) {
		    	reset();
		    	return null;
		    }
		    if (Character.isWhitespace(line.charAt(0))) {
		    	message.setCode(HttpCode.BAD_REQUEST);
		    	reset();
		    	return message;
		    }
		    String[] header = line.split("\\s");
		    if (header.length != 3) {
		    	message.setCode(HttpCode.BAD_REQUEST);
		    	reset();
		    	return message;
		    }
		    message.setMethod(header[0]);
		    String url = header[1];
		    int index = url.indexOf('?');
		    if (index != -1) {
		    	String[] params = url.substring(index+1).split("&");
		    	url = URLDecoder.decode(url.substring(0, index), "ISO-8859-1");
		    	for (int i=0; i<params.length; i++) {
		    		String[] keyValue = params[i].split("=");
		    		if (keyValue.length == 2) {
		    			message.addParam(
	    					URLDecoder.decode(keyValue[0], "ISO-8859-1"),
	    					URLDecoder.decode(keyValue[1], "ISO-8859-1")
		    			);
		    		}
		    		else if(keyValue.length == 1) {
		    			message.addParam(URLDecoder.decode(keyValue[0], "ISO-8859-1"), "");
		    		}
		    	}
		    }
		    message.setUrl(url);		    
		    
		    if (header[2].indexOf("HTTP/") == 0 && header[2].indexOf('.') > 5) {
		      message.setVersion(header[2].substring(5));
		    }
			while (true) {
				line = readStringLine();
				if ("".equals(line) || "\n".equals(line) || "\r\n".equals(line)) {
					break;
				}
				//System.out.print(line);
				index = line.indexOf(':');
				message.addHeader(
					line.substring(0,index).toLowerCase(),
					line.substring(index+1,line.length()).trim()
				);
			}
			byte[] bline;
			while ((bline = readBytesLine()).length != 0) {
				body.append(bline);
			}
			if (body.length() > 0) {
				message.setBody(body.getBytes());
			}
			reset();
			return m;
		} catch (IOException e) {
			throw e;
		} finally {
			reset();
		}
	}
	private void reset() {
		positionBuffer = 0;
		lengthBuffer = 0;
		input = null;
		message = null;
		body.delete(0, body.length());
		line.delete(0, line.length());
	}
	private int bufferIndexOfEndOfLine() {
		if (positionBuffer < lengthBuffer) {
			for (int i = positionBuffer; i < lengthBuffer; i++) {
				if (buffer[i] == (byte)10) {
					return i;
				}
			}
		}
		return -1;
	}
	private String readStringLine() throws IOException {
		return new String(readBytesLine());
	}
	private byte[] readBytesLine() throws IOException {
		if (input == null || (positionBuffer >= lengthBuffer && input.available() == 0)) {
			return ByteUtils.EMPTY;
		}
		int index;
		int end;
		while (true) {
			index = bufferIndexOfEndOfLine();
			end = index != -1 ? index+1 : lengthBuffer;
			line.append(buffer,positionBuffer, end);
			positionBuffer = end;
			if (positionBuffer >= lengthBuffer) {
				if (input.available() > 0) {
					lengthBuffer = input.read(buffer);
					positionBuffer = 0;
				} else {
					break;
				}
			}
			if (index != -1) {
				break;
			}
		}
		byte[] bytes = line.getBytes();
		line.delete(0, line.length());
		return bytes;
	}
	public void format(HttpSocket socket, HttpMessage m) throws IOException {
		if (m.getResponse() == null) {
			formatForNull(socket, m);
		} else {
			if (m.getResponse() instanceof String) {
				formatForText(socket, m, ((String)m.getResponse()).getBytes());
			} else if (m.getResponse() instanceof byte[]) {
				formatForText(socket, m, (byte[])m.getResponse());
			} else if (m.getResponse() instanceof File) {
				formatForFile(socket, m, (File)m.getResponse());
			} else {
				FimetLogger.error(getClass(),"Invalid response type "+m.getResponse());
				throw new FimetException("Invalid response type "+m.getResponse());				
			}
		}
	}
	private void doWrite(HttpSocket socket, byte[] message) throws IOException {
		doWrite(socket, message, 0, message.length);
	}
	private void doWrite(HttpSocket socket, byte[] message, int start, int length) throws IOException {
		synchronized (socket) {
			socket.socket.getOutputStream().write(message, start, length);
		}
	}
	private void formatForNull(HttpSocket socket, HttpMessage m) throws IOException {
		ByteBuilder w = new ByteBuilder(100);
		w.append("HTTP/"+m.getVersion()+" "+m.getCode().getCode()+" "+m.getCode().getDescription()+"\n");
		w.append("Date: "+DateUtils.HTTP_FMT.format(new Date())+"\n");
		w.append("Server: "+Version.getVersion()+"\n");
		w.append("Content-Type: "+m.getHeader("Accept")+"\n");
		w.append("Connection: Closed\n");
		w.append("Content-Length: 0\n");
		w.append("\n");
		doWrite(socket, w.getBytes());
	}
	private void formatForText(HttpSocket socket, HttpMessage m, byte[] text) throws IOException {
		ByteBuilder w = new ByteBuilder(100);
		w.append("HTTP/"+m.getVersion()+" "+m.getCode().getCode()+" "+m.getCode().getDescription()+"\n");
		w.append("Date: "+DateUtils.HTTP_FMT.format(new Date())+"\n");
		w.append("Server: "+Version.getVersion()+"\n");
		w.append("Content-Type: "+m.getHeader("Accept")+"\n");
		w.append("Connection: Closed\n");
		w.append("Content-Length: "+text.length+"\n");
		w.append("\n");
		w.append(text);
		doWrite(socket, w.getBytes());
	}
	private void formatForFile(HttpSocket socket, HttpMessage m, File file) throws IOException {
		ByteBuilder w = new ByteBuilder(100);
		w.append("HTTP/"+m.getVersion()+" "+m.getCode().getCode()+" "+m.getCode().getDescription()+"\n");
		w.append("Date: "+DateUtils.HTTP_FMT.format(new Date())+"\n");
		w.append("Server: "+Version.getVersion()+"\n");
		w.append("Pragma: public\n");
		w.append("Expires: 0\n");
		w.append("Cache-Control: must-revalidate, post-check=0, pre-check=0\n");
		w.append("Cache-Control: public\n");
		w.append("Content-Description: File Transfer\n");
		addTypeHeaders(w, file);
		w.append("Content-Disposition: attachment; filename=\""+file.getName()+"\"\n");
		w.append("Content-Length: "+file.length()+"\n");
		w.append("\n");
		doWrite(socket, w.getBytes());
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			byte[] step = new byte[1024];
			int ln = 0;
			while ((ln = in.read(step)) > 0) {
				doWrite(socket, step, 0, ln);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			FileUtils.close(in);
		}
	}
	private void addTypeHeaders(ByteBuilder w, File file) {
		String ext = FileUtils.getExtension(file).toLowerCase();
		if ("txt".equals(ext)) {
			w.append("Content-type: "+HttpContentType.APPLICATION_TXT.getType()+"\n");
		} else if ("csv".equals(ext)) {
			w.append("Content-type: "+HttpContentType.APPLICATION_CSV.getType()+"\n");
		} else if ("json".equals(ext)) {
			w.append("Content-type: "+HttpContentType.APPLICATION_JSON.getType()+"\n");
		} else if ("xml".equals(ext)) {
			w.append("Content-type: "+HttpContentType.APPLICATION_XML.getType()+"\n");
		} else {
			w.append("Content-type: "+HttpContentType.APPLICATION_OCTET_STREAM.getType()+"\n");
			w.append("Content-Transfer-Encoding: binary\n");
		}
	}
}
