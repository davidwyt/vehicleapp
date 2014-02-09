package com.vehicle.app.msgprocessors;

import com.vehicle.app.bean.IMessageItem;

public interface IMessageProcessor {
	void process(IMessageItem msg);
}
