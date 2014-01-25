package com.vehicle.service.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class OfflineMessageRequest implements IRequest{

	private String target;
	private long since;
	
	@XmlElement
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	@XmlElement
	public long getSince() {
		return since;
	}
	public void setSince(long since) {
		this.since = since;
	}
}
