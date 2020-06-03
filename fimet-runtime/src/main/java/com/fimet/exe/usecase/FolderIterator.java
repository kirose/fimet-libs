package com.fimet.exe.usecase;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.fimet.FimetException;
import com.fimet.FimetLogger;
import com.fimet.usecase.UseCase;
import com.fimet.utils.UseCaseUtils;

public class FolderIterator implements Iterator<UseCase> {
	
	static DirectoryStream.Filter<Path> FILTER = new DirectoryStream.Filter<Path>() {
	    @Override
	    public boolean accept(Path entry) throws IOException {
	    	if (isUseCaseFile(entry)) {
	    		return true;
	    	} else {
	    		BasicFileAttributes attr = Files.readAttributes(entry, BasicFileAttributes.class);
	    		return attr.isDirectory();
	    	}
	    }
	};
	
	DirectoryStream<Path> stream = null;
	Iterator<Path> iterator = null;
	UseCase next = null;
	Path curr = null;
	Queue<Path> queue = new LinkedBlockingQueue<Path>();
	public FolderIterator(File folder) {
		_newIterator(Paths.get(folder.getAbsolutePath()));
	}
	private void _newIterator(Path path) {
		try {
			if (curr != null) {
				close();
			}
			curr = path;
			stream = Files.newDirectoryStream(path, FILTER);//"*.{uc}");
			iterator = stream.iterator();
		} catch (Exception ex) {
			throw new FimetException("Cannot found folder "+path);
		}
	}
	@Override
	public UseCase next() {
		UseCase prev = next;
		next = null;
		return prev;
	}
	@Override
	public boolean hasNext() {
		if (next != null) {
			return true;
		} else if (iterator == null) {
			return false;
		} else {
			do {
				Path entry = iterator.next();
				if (isUseCaseFile(entry)) {
					next = parseUseCase(entry);
				} else {
					queue.add(entry);
				}
			} while (next == null & _hasNext());
			return next != null;
		}
	}
	private boolean _hasNext() {
		if (iterator == null) {
			if (queue.isEmpty()) {
				return false;
			} else {
				Path path = queue.poll();
				_newIterator(path);
				return iterator.hasNext();
			}
		} else {
			if (iterator.hasNext()) {
				return true;
			} else if (queue.isEmpty()) {
				close();
				return false;
			} else {
				Path path = queue.poll();
				_newIterator(path);
				return iterator.hasNext();
			}
		}
	}
	public void close() {
		if (stream != null) {
			curr = null;
			try {
				stream.close();
				stream = null;
			} catch (Exception e) {}
			iterator = null;
		}
	}
	private static boolean isUseCaseFile(Path entry) {
		return entry.toString().endsWith(".uc") || entry.toString().endsWith(".UC");		
	}
	private static UseCase parseUseCase(Path entry) {
		if (isUseCaseFile(entry)) {
			try {
				UseCase useCase = UseCaseUtils.fromFile(entry.toFile());
				return useCase;
			} catch (Exception e) {
				FimetLogger.error(FolderIterator.class,"Invalid use case "+(entry.getFileName()),e);
				return null;
			}
		}
		return null;
	}
}
