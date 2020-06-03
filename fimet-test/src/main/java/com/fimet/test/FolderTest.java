package com.fimet.test;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FolderTest {

	public static void main(String[] args) throws IOException {
        Path path = Paths.get("usecases");
        System.out.println("Path to stream: " + path);
        //stream all files with name ending .log
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(path, "*")) {
            ds.forEach(System.out::println);
        }
	}
}
