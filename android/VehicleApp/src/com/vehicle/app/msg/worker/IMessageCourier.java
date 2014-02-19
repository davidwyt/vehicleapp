package com.vehicle.app.msg.worker;

import com.vehicle.app.msg.bean.IMessageItem;


public interface IMessageCourier {
	
	void dispatch(IMessageItem item);
}
