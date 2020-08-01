package com.fimet.event;

import com.fimet.event.IEventListener;
import com.fimet.ftp.IEFtp;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IFtpDeleted extends IEventListener {
	public void onFtpDeleted(IEFtp e);
}
