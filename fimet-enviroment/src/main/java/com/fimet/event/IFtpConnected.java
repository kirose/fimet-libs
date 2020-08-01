package com.fimet.event;

import com.fimet.event.IEventListener;
import com.fimet.ftp.IFtp;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IFtpConnected extends IEventListener {
	public void onFtpConnected(IFtp e);
}
