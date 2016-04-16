/**
 * <pre>
 * Title:title
 * Description: description
 * </pre>
 * 
 * @author caiqy caiqinyu@jd.com

 * 修改记录
 *    修改后版本:     修改人：  修改日期:     修改内容:
 * </pre>
 */
package com.frame.base.security;

import java.io.Serializable;

import org.apache.shiro.authc.UsernamePasswordToken;

public class BaseAuthToken extends UsernamePasswordToken implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3277350176868586950L;
	
	private String erpNo;
	private String passwordStr;
	
	public BaseAuthToken() {
		super();
	}
	
	public BaseAuthToken(String erpNo , String passwordStr) {
		super();
		this.erpNo = erpNo;
		this.passwordStr = passwordStr;
	}
	
	public String getErpNo() {
		return erpNo;
	}
	public void setErpNo(String erpNo) {
		this.erpNo = erpNo;
	}
	public String getPasswordStr() {
		return passwordStr;
	}
	public void setPasswordStr(String passwordStr) {
		this.passwordStr = passwordStr;
	}
	

}
