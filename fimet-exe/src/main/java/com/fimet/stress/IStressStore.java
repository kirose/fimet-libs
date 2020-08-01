package com.fimet.stress;

import com.fimet.exe.IStore;
import com.fimet.exe.SocketResult;

public interface IStressStore extends IStore {
	void storeCycleResults(SocketResult result);
	void storeGlobalResults(SocketResult result);
}
