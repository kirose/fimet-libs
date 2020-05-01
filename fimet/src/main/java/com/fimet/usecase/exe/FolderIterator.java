package com.fimet.usecase.exe;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import com.fimet.commons.FimetLogger;
import com.fimet.commons.exception.FimetException;
import com.fimet.usecase.UseCase;
import com.fimet.usecase.json.UseCaseJson;
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
		if (stream == null || iterator == null) {
			return false;
		} else if (iterator.hasNext()){
			return true;
		} else {
			close();	
		}
		return false;
	}
	private void close() {
		if (stream != null) {
			try {
				stream.close();
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
				UseCase useCase = UseCaseUtils.parseForExecution(nextJson);
				return useCase;
			} catch (Exception e) {
				FimetLogger.error(FolderIterator.class,"Invalid use case "+(next != null ? next.toFile().getAbsolutePath() : null),e);
			}
		}
		return null;
	}

}
