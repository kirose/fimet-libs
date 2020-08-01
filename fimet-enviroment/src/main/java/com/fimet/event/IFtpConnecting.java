package com.fimet.event;

import com.fimet.event.IEventListener;
import com.fimet.ftp.IFtp;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IFtpConnecting extends IEventListener {
	public void onFtpConnecting(IFtp e);
}
