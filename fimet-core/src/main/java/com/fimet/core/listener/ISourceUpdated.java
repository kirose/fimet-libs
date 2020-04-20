package com.fimet.core.listener;

import com.fimet.core.entity.sqlite.Source;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface ISourceUpdated extends ISourceListener {
	public void onSourceUpdated(Source e);
}
