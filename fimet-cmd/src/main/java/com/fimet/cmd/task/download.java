package com.fimet.cmd.task;

import java.io.File;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.fimet.FimetLogger;
import com.fimet.cmd.AbstractCommander;
import com.fimet.cmd.Command;
import com.fimet.cmd.Command.Status;
import com.fimet.exe.Task;
import com.fimet.utils.FileUtils;

public class download extends AbstractCommander {
	@Override
	public void doRequest(Command cmd) {
		if (cmd.getParam("idTask") != null) {
			String idTask = cmd.getParam("idTask");
			File folder = new File(Task.TASKS_PATH, idTask);
			File zipFile = new File(Task.TASKS_PATH, idTask + ".zip");
			if (folder.exists()) {
				File[] files = folder.listFiles();
				if (files != null && files.length > 0) {
					ZipOutputStream outputZip = null;
					try {
						FileOutputStream output = new FileOutputStream(zipFile);
						outputZip = new ZipOutputStream(output);
						for (File file : files) {
							outputZip.putNextEntry(new ZipEntry(file.getName()));
							FileUtils.write(file, outputZip, false);
							outputZip.closeEntry();
						}
					} catch (Exception e) {
						FimetLogger.error(getClass(), e);
					} finally {
						FileUtils.close(outputZip);
					}
				}
			}
			commandManager.reply(cmd.getId(), Status.OK, zipFile);
		} else {
			commandManager.reply(cmd.getId(), Status.ERROR, null);
		}
	}
}
