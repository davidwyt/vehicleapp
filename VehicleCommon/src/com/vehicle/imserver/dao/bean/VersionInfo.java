package com.vehicle.imserver.dao.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "VERSIONINFO")
public class VersionInfo {

	private long id;
	private int majorVersion;
	private int minorVersion;
	private String apkPath;
	private long updateTime;

	public void setId(long id) {
		this.id = id;
	}

	@Id
	@Column(name = "ID")
	public long getId() {
		return this.id;
	}

	@Column(name = "MAJORVERSION")
	public int getMajorVersion() {
		return this.majorVersion;
	}

	public void setMajorVersion(int version) {
		this.majorVersion = version;
	}

	@Column(name = "MINORVERSION")
	public int getMinorVersion() {
		return this.minorVersion;
	}

	public void setMinorVersion(int version) {
		this.minorVersion = version;
	}

	@Column(name = "APKPATH")
	public String getApkPath() {
		return this.apkPath;
	}

	public void setApkPath(String path) {
		this.apkPath = path;
	}

	@Column(name = "UPDATETIME")
	public long getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(long time) {
		this.updateTime = time;
	}
}
