package com.vehicle.app.msg.worker;

import android.content.Context;

public abstract class MessageBaseCourier implements IMessageCourier {

	protected Context context;

	public MessageBaseCourier(Context context) {
		this.context = context;
	}
}
