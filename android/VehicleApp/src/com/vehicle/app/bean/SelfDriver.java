package com.vehicle.app.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.vehicle.app.utils.Constants;

public class SelfDriver implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8008365646929127682L;

	@SerializedName("member_id")
	private String id;

	@SerializedName("nick_name")
	private String alias;

	private String email;
	private String password;
	private String guid;

	@SerializedName("log_count")
	private String logCount;

	private int integral;

	@SerializedName("last_log_date")
	private String lastLoginDate;

	private String name;
	private String sex;
	private String avatar;
	private String city;
	private String province;
	private String address;

	@SerializedName("zip_code")
	private String zipCode;

	private String birthday;

	private String mobile;
	private String telephone;

	@SerializedName("view_total")
	private String viewTotal;

	private String introduction;

	private String sid;

	private List<Car> cars;

	private List<Comment> comments;

	public List<Car> getCars() {
		return this.cars;
	}

	public void setCars(List<Car> cars) {
		this.cars = cars;
	}

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

	public String getLogCount() {
		return this.logCount;
	}

	public void setLogCount(String logCount) {
		this.logCount = logCount;
	}

	public int getIntegral() {
		return this.integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
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
		if (null == avatar || avatar.isEmpty()) {
			this.avatar = Constants.URL_DEFAULTICON;
		} else {
			this.avatar = avatar;
		}
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
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

	public String getViewTotal() {
		return this.viewTotal;
	}

	public void setViewTotal(String num) {
		this.viewTotal = num;
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

	public List<Comment> getComments() {
		return this.comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
}
