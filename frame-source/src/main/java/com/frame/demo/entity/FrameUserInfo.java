package com.frame.demo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="FRAME_USER_INFO")
public class FrameUserInfo implements Serializable{

	private static final long serialVersionUID = 5646756931686808801L;
	
	@Id
	@Column(name="USER_ID")
	private String userId;
	
	@Column(name="USER_NAME")
	private String userName;

	@Column(name="SEX_TYPE")
	private String sexType;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSexType() {
		return sexType;
	}

	public void setSexType(String sexType) {
		this.sexType = sexType;
	}
	
}
