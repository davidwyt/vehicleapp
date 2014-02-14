package com.vehicle.app.msg.worker;

import com.vehicle.app.msg.bean.IMessageItem;

public interface IMessageRecipient {
	void receive(IMessageItem item);
}
