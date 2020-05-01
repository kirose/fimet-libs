package com.fimet.listener;

import com.fimet.entity.sqlite.ESource;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ISourceInserted extends ISourceListener {
	public void onSourceInserted(ESource e);
}
