package com.fimet.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public final class FileUtils {
	private static final int SIZE_BUFFER = 512;
	private FileUtils() {
	}
	public static String readContents(File file) {
		if (file == null || !file.exists() || file.isDirectory())
			return null;
		InputStream in = null;
		StringBuilder content = new StringBuilder();
		try {
			in = new FileInputStream(file);
			byte[] bytes = new byte[1024];
			int i = 0;
			while ((i = in.read(bytes))>0) {
				content.append(new String(bytes,0,i));
			}
		} catch (IOException e) {
		} finally {
			close(in);
		}
		return content.toString();
	}
	public static byte[] readBytesContents(File file) {
		if (file == null || !file.exists() || file.isDirectory())
			return null;
		if (file.length() > 0) {
			InputStream in = null;
			try {
				in = new FileInputStream(file);
				byte[] bytes = new byte[(int)file.length()];
				in.read(bytes);
				return bytes;
			} catch (IOException e) {
				return null;
			} finally {
				close(in);
			}
		} else {
			return new byte[0];
		}
	}
	public static void appendContents(File file, byte[] contents) {
		if (file == null)
			return;
		OutputStream out = null;
		try {
			out = new FileOutputStream(file,true);
			out.write(contents);
		} catch (IOException e) {
		} finally {
			close(out);
		}
	}
	public static void writeContents(File file, byte[] contents) {
		if (file == null)
			return;
		OutputStream out = null;
		try {
			out = new FileOutputStream(file);
			out.write(contents);
		} catch (IOException e) {
		} finally {
			close(out);
		}
	}
	public static void writeContents(File file, String contents) {
		if (file == null || file.isDirectory())
			return;
		java.io.OutputStreamWriter out = null;
		try {
			out = new java.io.FileWriter(file);
			out.write(contents == null ? "" : contents);
		} catch (IOException e) {
		} finally {
			close(out);
		}
	}
	public static void appendContents(File file, String contents) {
		if (file == null || file.isDirectory())
			return;
		java.io.OutputStreamWriter out = null;
		try {
			out = new java.io.FileWriter(file, true);
			out.write(contents == null ? "" : contents);
		} catch (IOException e) {
		} finally {
			close(out);
		}
	}
	public static void createSubdirectories(File file) {
		String path = file.getAbsolutePath().replace('\\', '/');
		path = path.substring(0,path.lastIndexOf('/'));
		new File(path).mkdirs();
	}
	public static void write(File input, File output, long start, int length) {
		byte[] bytes = new byte[512];
		RandomAccessFile reader = null;
		FileOutputStream writer = null;
		try {
			writer = new FileOutputStream(output);
			reader = new RandomAccessFile(input,"r");
			reader.seek(start);
			int count = 0;
			int read;
			while (count < length && (read = reader.read(bytes, 0, Math.min(length-count, SIZE_BUFFER))) > 0) {
				writer.write(bytes,0,read);
				count += read;
			}
		} catch (IOException ex) {
			throw new RuntimeException("Cannot read file "+(input != null ? input.getName() : null), ex);
		} finally {
			close(reader);
			close(writer);
		}
	}
	public static void copy(File input, File output) {
		byte[] bytes = new byte[512];
		RandomAccessFile reader = null;
		FileOutputStream writer = null;
		try {
			writer = new FileOutputStream(output);
			reader = new RandomAccessFile(input,"r");
			int read;
			while ((read = reader.read(bytes)) > 0) {
				writer.write(bytes,0,read);
			}
		} catch (IOException ex) {
			throw new RuntimeException("Cannot copy file "+(input != null ? input.getName() : null), ex);
		} finally {
			close(reader);
			close(writer);
		}		
	}
	public static void deleteFiles(File file) {
		if (file.isFile()) {
			file.delete();
		} else if (file.isDirectory()) {
			File[] childs = file.listFiles();
			if (childs != null && childs.length > 0) {
				for (File f : childs) {
					deleteFiles(f);
				}
			}
		}
	}
	public static void delete(File file) {
		if (file.isFile()) {
			file.delete();
		} else if (file.isDirectory()) {
			File[] childs = file.listFiles();
			if (childs != null && childs.length > 0) {
				for (File f : childs) {
					delete(f);
				}
			}
			file.delete();
		}
	}
	public static String getExtension(File file) {
		if (file == null || !file.isFile()) {
			return null;
		}
		int index = file.getName().lastIndexOf('.');
		if (index > 0) {
			return file.getName().substring(index+1);
		} else {
			return null;
		}
	}
	public static String getSimpleName(File file) {
		if (file == null || !file.isFile()) {
			return null;
		}
		int index = file.getName().lastIndexOf('.');
		if (index > 0) {
			return file.getName().substring(0,index);
		} else {
			return file.getName();
		}
	}
	public static void write(File from, OutputStream to, boolean closeTo) {
		if (from != null && from.exists() && !from.isDirectory() && from.length() > 0) {
			try {
				write(new FileInputStream(from), to, true, closeTo);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	public static void write(FileInputStream from, OutputStream to, boolean closeFrom, boolean closeTo) {
		try {
			byte[] bytes = new byte[1024];
			int i = 0;
			while ((i = from.read(bytes))>0) {
				to.write(bytes,0,i);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (closeFrom) close(from);
			if (closeTo) close(to);
		}
	}
	public static void close(java.io.Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException ex) {}
		}		
	}
}
