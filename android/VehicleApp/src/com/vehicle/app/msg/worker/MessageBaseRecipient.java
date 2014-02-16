package com.vehicle.app.msg.worker;

import android.content.Context;

public abstract class MessageBaseRecipient implements IMessageRecipient {

	protected Context context;

	public MessageBaseRecipient(Context context) {
		this.context = context;
	}

	protected boolean shouldNotifyBar() {
		return true;
	}
}
