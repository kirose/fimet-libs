package com.fimet.core.iso8583.adapter;

public class MliExclusiveSimQueueAdapter extends MliSimQueueAdapter {
	MliExclusiveSimQueueAdapter(int id, String name) {
		super(id, name, true);
	}
}
