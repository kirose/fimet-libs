package com.fimet.adapter.iso8583;

import com.fimet.IAdapterManager;
import com.fimet.adapter.IAdapter;

public final class MLI {
	private MLI() {}
	public static final IAdapter EXCLUSIVE = IAdapterManager.MLI_EXCLUSIVE;
	public static final IAdapter INCLUSIVE = IAdapterManager.MLI_INCLUSIVE;
	public static final IAdapter VISA_EXCLUSIVE = IAdapterManager.MLI_VISA_EXCLUSIVE;
	public static final IAdapter STX = IAdapterManager.STX;
}
