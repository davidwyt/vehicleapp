package com.vehicle.app.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.vehicle.app.utils.Constants;
import com.vehicle.app.utils.StringUtil;

public class Driver implements Serializable {

	private static final long serialVersionUID = 6645547105668102215L;

	@SerializedName("member_id")
	protected String id;

	@SerializedName("nick_name")
	protected String alias;

	protected String email;

	@SerializedName("last_log_date")
	protected String lastLoginDate;

	@SerializedName("log_count")
	protected String logCount;

	protected String name;
	protected String sex;
	protected String avatar;
	protected String city;
	protected String province;
	protected String address;

	@SerializedName("zip_code")
	protected String zipCode;

	protected String birthday;

	protected String mobile;
	protected String telephone;

	@SerializedName("view_total")
	protected String viewTotal;

	protected String introduction;

	private String lastMessage;
	private Date lastMessageTime;
	private double distance;

	private long duration;

	public long getDuration() {
		return this.duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	@SerializedName("Review.list")
	protected List<Comment> comments;

	protected List<Car> cars;

	public List<Comment> getComments() {
		return this.comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

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

	public String getLastLoginDate() {
		return this.lastLoginDate;
	}

	public void setLastLoginDate(String date) {
		this.lastLoginDate = date;
	}

	public String getLogCount() {
		return this.logCount;
	}

	public void setLogCount(String logCount) {
		this.logCount = logCount;
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
		if (null == avatar || avatar.isEmpty()) {
			return Constants.URL_DEFAULTICON;
		} else
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
		return StringUtil.formatNumber(this.mobile);
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTelephone() {
		return StringUtil.formatNumber(this.telephone);
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
		return StringUtil.filterHtml(this.introduction);
	}

	public void setIntroduction(String intro) {
		this.introduction = intro;
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

	public double getDistance() {
		return this.distance;
	}

	public void setDistance(double dis) {
		this.distance = dis;
	}

}
