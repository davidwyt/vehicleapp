package com.vehicle.service.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.vehicle.imserver.dao.bean.VersionInfo;

@XmlRootElement
public class LatestVersionResponse extends BaseResponse {

	private VersionInfo version;

	public void setVersion(VersionInfo v) {
		this.version = v;
	}

	@XmlElement
	public VersionInfo getVersion() {
		return this.version;
	}
}
