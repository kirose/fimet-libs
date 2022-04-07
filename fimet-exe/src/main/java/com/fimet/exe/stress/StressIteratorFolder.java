package com.fimet.exe.stress;

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

import org.slf4j.LoggerFactory;import org.slf4j.Logger;

import com.fimet.FimetException;

import com.fimet.stress.Stress;
import com.fimet.utils.StressUtils;

public class StressIteratorFolder implements Iterator<Stress> {
	private static Logger logger = LoggerFactory.getLogger(StressIteratorFolder.class);
	static DirectoryStream.Filter<Path> FILTER = new DirectoryStream.Filter<Path>() {
	    @Override
	    public boolean accept(Path entry) throws IOException {
	    	if (isStressFile(entry)) {
	    		return true;
	    	} else {
	    		BasicFileAttributes attr = Files.readAttributes(entry, BasicFileAttributes.class);
	    		return attr.isDirectory();
	    	}
	    }
	};
	
	DirectoryStream<Path> stream = null;
	Iterator<Path> iterator = null;
	Stress next = null;
	Path curr = null;
	Queue<Path> queue = new LinkedBlockingQueue<Path>();
	File folder;
	public StressIteratorFolder(File folder, File output) {
		folder = output;
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
	public Stress next() {
		Stress prev = next;
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
				if (isStressFile(entry)) {
					next = parseStress(entry);
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
	private static boolean isStressFile(Path entry) {
		return entry.toString().endsWith(".stress") || entry.toString().endsWith(".STRESS");		
	}
	private Stress parseStress(Path entry) {
		if (isStressFile(entry)) {
			try {
				Stress useCase = (Stress)StressUtils.fromFile(entry.toFile(), folder);
				return useCase;
			} catch (Exception e) {
				logger.error("Invalid stress "+(entry.getFileName()),e);
				return null;
			}
		}
		return null;
	}
}
