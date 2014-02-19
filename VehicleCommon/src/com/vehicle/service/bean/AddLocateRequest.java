<<<<<<< HEAD
package com.vehicle.service.bean;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AddLocateRequest implements IRequest{
	
	private String id;
	private double locateX;
	private double locateY;
	
	@XmlElement
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@XmlElement
	public double getLocateX() {
		return locateX;
	}
	public void setLocateX(double locateX) {
		this.locateX = locateX;
	}
	
	@XmlElement
	public double getLocateY() {
		return locateY;
	}
	public void setLocateY(double locateY) {
		this.locateY = locateY;
	}

}
=======
package com.vehicle.service.bean;

public class AddLocateRequest implements IRequest{
	
	private String id;
	private double locateX;
	private double locateY;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public double getLocateX() {
		return locateX;
	}
	public void setLocateX(double locateX) {
		this.locateX = locateX;
	}
	public double getLocateY() {
		return locateY;
	}
	public void setLocateY(double locateY) {
		this.locateY = locateY;
	}

}
>>>>>>> 873b6dca133d6876b5a1aa2e5a8409097dec413a
