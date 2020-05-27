package com.fimet.exe.usecase;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import com.fimet.FimetException;
import com.fimet.FimetLogger;
import com.fimet.json.UseCaseJson;
import com.fimet.usecase.UseCase;
import com.fimet.utils.UseCaseUtils;

public class FolderIterator implements Iterator<UseCase> {
	DirectoryStream<Path> stream = null;
	Iterator<Path> iterator = null;
	public FolderIterator(File folder) {
		try {
			stream = Files.newDirectoryStream(Paths.get(folder.getAbsolutePath()), "*.{uc}");
			iterator = stream.iterator();
		} catch (Exception ex) {
			throw new FimetException("Cannot found folder "+(folder != null ? folder.getAbsolutePath() : null));
		}
	}
	 
	@Override
	public boolean hasNext() {
		if (iterator == null) {
			return false;
		} else if (iterator.hasNext()){
			return true;
		} else {
			close();	
		}
		return false;
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
	@Override
	public UseCase next() {
		if (iterator != null && iterator.hasNext()) {
			Path next = iterator.next();
			try {
				UseCaseJson nextJson = UseCaseUtils.parseJson(next.toFile());
				UseCase useCase = UseCaseUtils.parseForExecutionFromJson(nextJson);
				return useCase;
			} catch (Exception e) {
				FimetLogger.error(FolderIterator.class,"Invalid use case "+(next != null ? next.toFile().getAbsolutePath() : null),e);
			}
		}
		return null;
	}

}
