package com.fimet;

import java.io.File;

public final class Paths {
	public static final File FIMET_PATH = new File(Manager.getProperty("classpath.fimet","fimet"));
	public static final File STORE_PATH = new File(Manager.getProperty("classpath.store","fimet/store"));
	public static final File TASKS_PATH = new File(STORE_PATH,"tasks");
	public static final File MODEL_PATH = new File(Manager.getProperty("classpath.model","fimet/model"));
	public static final File BIN_PATH = new File(Manager.getProperty("classpath.bin","fimet/bin"));
	public static final File SRC_PATH = new File(Manager.getProperty("classpath.src","fimet/src"));
	public static final File LIB_PATH = new File(Manager.getProperty("classpath.lib","fimet/lib"));
	public static final File LOGS_PATH = new File(Manager.getProperty("classpath.lib","fimet/logs"));
	static {
		create(FIMET_PATH);
		create(STORE_PATH);
		create(TASKS_PATH);
		create(MODEL_PATH);
		create(BIN_PATH);
		create(SRC_PATH);
		create(LIB_PATH);
		create(LOGS_PATH);
	}
	private static void create(File path) {
		if (!path.exists()) {
			path.mkdirs();
		}
	}
	private Paths() {}
}
