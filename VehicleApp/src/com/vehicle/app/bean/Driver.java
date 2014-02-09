package com.vehicle.app.bean;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

import android.graphics.Bitmap;

public class Driver {

	@SerializedName("member_id")
	private String id;

	@SerializedName("nick_name")
	private String alias;

	private String email;
	private String password;
	private String guid;

	@SerializedName("last_log_date")
	private String lastLoginDate;

	private String name;
	private String sex;
	private String avatar;
	private String province;
	private String address;

	@SerializedName("zip_code")
	private String zipCode;

	private String birthday;

	private String mobile;
	private String telephone;

	private String introduction;

	private String sid;

	private Bitmap icon;
	private String lastMessage;
	private Date lastMessageTime;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAlias() {
		return this.alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String pwd) {
		this.password = pwd;
	}

	public String getGuid() {
		return this.guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getLastLoginDate() {
		return this.lastLoginDate;
	}

	public void setLastLoginDate(String date) {
		this.lastLoginDate = date;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAvatar() {
		return this.avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getProvince() {
		return this.province;
	}

	public void setProvince(String pro) {
		this.province = pro;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String addr) {
		this.address = addr;
	}

	public String getBirthday() {
		return this.birthday;
	}

	public void setBirthday(String bir) {
		this.birthday = bir;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String tel) {
		this.telephone = tel;
	}

	public String getIntroduction() {
		return this.introduction;
	}

	public void setIntroduction(String intro) {
		this.introduction = intro;
	}

	public String getSid() {
		return this.sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public Bitmap getIcon() {
		return this.icon;
	}

	public void setIcon(Bitmap icon) {
		this.icon = icon;
	}

	public String getLastMessage() {
		return this.lastMessage;
	}

	public void setLastMessage(String lastMsg) {
		this.lastMessage = lastMsg;
	}

	public Date getLastMessageDate() {
		return this.lastMessageTime;
	}

	public void setLastMessageDate(Date lastMsgDate) {
		this.lastMessageTime = lastMsgDate;
	}

}
