package com.fimet.parser;

import com.fimet.IAdapterManager;
import com.fimet.Manager;
import com.fimet.parser.IAdapter;

public final class MLI {
	private MLI() {}
	public static final IAdapter EXCLUSIVE = Manager.get(IAdapterManager.class).getAdapter("Exclusive");
	public static final IAdapter INCLUSIVE = Manager.get(IAdapterManager.class).getAdapter("Inclusive");
	public static final IAdapter VISA_EXCLUSIVE = Manager.get(IAdapterManager.class).getAdapter("VisaExclusive");
	public static final IAdapter STX = Manager.get(IAdapterManager.class).getAdapter("Stx");
}
