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
package com.frame.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.frame.base.service.BaseBS;
import com.frame.demo.entity.FrameUserInfo;

@Service
@Transactional(readOnly=false)
public class FrameTestBS extends BaseBS<Object>{

	public List<FrameUserInfo> getUsers(){
		return this.getEntityList(FrameUserInfo.class);
	}
	
	public List<FrameUserInfo> getOwnerUser(){
		List<FrameUserInfo> users = new ArrayList<FrameUserInfo>();
		String jql = "select user from FrameUserInfo user where user.userName like ?0";
		List<FrameUserInfo> querys = this.baseDAO.findWithIndexParam(jql, "%cai%");
		if(querys != null){
			users.addAll(querys);
		}
		return users;
	}
	
}
