package com.fimet.event;

import java.util.List;

import com.fimet.event.IEventListener;
import com.fimet.ftp.IFtp;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IFtpManagerReloaded extends IEventListener {
	public void onFtpManagerReloaded(List<IFtp> ftps);
}
