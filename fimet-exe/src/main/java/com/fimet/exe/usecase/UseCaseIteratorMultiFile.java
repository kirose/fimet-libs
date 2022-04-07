package com.fimet.exe.usecase;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fimet.usecase.UseCase;
import com.fimet.utils.UseCaseUtils;

public class UseCaseIteratorMultiFile implements Iterator<UseCase> {
	private static Logger logger = LoggerFactory.getLogger(UseCaseIteratorMultiFile.class);
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
	Queue<Path> queue = new LinkedBlockingQueue<Path>();
	public UseCaseIteratorMultiFile(File file) {
		queue.add(file.toPath());
	}
	public UseCaseIteratorMultiFile(File[] files) {
		for (File file : files) {
			queue.add(file.toPath());
		}
	}
	public UseCaseIteratorMultiFile(List<File> executable) {
		for (File file : executable) {
			queue.add(file.toPath());
		}
	}
	private Iterator<Path> _newIterator(Path path) {
		try {
			stream = Files.newDirectoryStream(path, FILTER);//"*.{uc}");
			Iterator<Path> iterator = stream.iterator();
			return iterator;
		} catch (Exception ex) {
			logger.error("Folder iterator exception",ex);
			iterator = null;
			return null;
		}
	}
	@Override
	public UseCase next() {
		UseCase curr = next;
		next = null;
		return curr;
	}
	@Override
	public boolean hasNext() {
		if (next != null) {
			return true;
		} else if (iterator != null) {
			while (iterator.hasNext()) {
				Path path = iterator.next();
				if (isUseCaseFile(path)) {
					next = parseUseCase(path);
					if (next!=null) {
						return true;
					}
				} else {
					queue.add(path);
				}
			}
			close();
			return hasNext();
		} else if (!queue.isEmpty()) {
			while (!queue.isEmpty()) {
				Path path = queue.poll();
				if (isUseCaseFile(path)) {
					next = parseUseCase(path);
					if (next!=null) {
						return true;
					}
				} else if (path.toFile().isDirectory()) {
					close();
					iterator = _newIterator(path);
					if (iterator!=null) {
						return hasNext();
					}
				} else {
					logger.warn("Invalid file to execute usecase {}",path);
				}
			}
			return hasNext();
		} else {
			return false;
		}
	}
	public void close() {
		if (stream != null) {
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
				UseCase useCase = (UseCase)UseCaseUtils.fromFile(entry.toFile());
				return useCase;
			} catch (Exception e) {
				logger.error("Invalid use case {}",entry.toFile().getPath(),e);
				return null;
			}
		}
		return null;
	}
}
