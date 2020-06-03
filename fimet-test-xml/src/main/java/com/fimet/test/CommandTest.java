package com.fimet.test;

import com.fimet.ICommandManager;
import com.fimet.Manager;

public class CommandTest {
	public static void main(String[] args) {
		//System.out.println(com.fimet.Version.getInstance().decrypt("vd5A+ZAtDnKpgj8RMGVVpw=="));
		Manager.get(ICommandManager.class);
	}
}
