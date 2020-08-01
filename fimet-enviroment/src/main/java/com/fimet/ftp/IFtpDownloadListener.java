package com.fimet.ftp;

import java.io.File;

public interface IFtpDownloadListener {
	void onFtpDonwloadFinish(File file, long numOfLines, long size);
}
