package com.fimet.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.fimet.AbstractManager;
import com.fimet.IThreadManager;
import com.fimet.Manager;

public class ThreadManager extends AbstractManager implements IThreadManager {
	volatile ThreadPoolExecutor executor;
	public ThreadManager() {
		Integer numOfThreads = Manager.getPropertyInteger("thread.numOfThreads", 2);
		executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numOfThreads);
		//executor.setMaximumPoolSize(10);
	}
	@Override
	public void execute(Runnable runnable) {
		executor.execute(runnable);
		//if (executor.get)
		//System.out.println("ThreadPoolQueueSize-"+executor.getCompletedTaskCount()+"/"+executor.getActiveCount()+"/"+executor.getTaskCount());
	}

}
