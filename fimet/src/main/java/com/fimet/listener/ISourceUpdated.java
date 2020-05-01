package com.fimet.listener;

import com.fimet.entity.sqlite.ESource;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ISourceUpdated extends ISourceListener {
	public void onSourceUpdated(ESource e);
}
