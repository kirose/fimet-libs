package com.fimet;

import com.fimet.timer.ITimerListener;
import com.fimet.timer.Scheduled;

public interface ITimerManager extends IManager {
	Scheduled schedule(Object object, long time, ITimerListener listener);
	void cancel(Object object);
}
