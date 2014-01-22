package com.vehicle.imserver.dao.interfaces;

import java.util.Date;
import java.util.List;

import com.vehicle.imserver.dao.bean.OfflineMessage;

public interface OfflineMessageDao extends BaseDao<OfflineMessage> {

	public List<OfflineMessage> getOffline(String target,Date time);
}
