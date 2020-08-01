package com.fimet.exe;

import java.io.File;

public interface IExecutable {
	Object getSource();
	Object getExecutable();
	File getFolder();
}
