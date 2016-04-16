package com.frame.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ch.qos.logback.classic.Logger;

import com.frame.base.common.Pager;
import com.frame.base.security.BaseAuthToken;
import com.frame.demo.service.FrameTestBS;

@Controller
public class FrameTestController {

	private static Logger logger = (Logger) LoggerFactory
			.getLogger(FrameTestController.class);

	@Autowired
	private FrameTestBS frameTestBS;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView mdv = new ModelAndView();
		mdv.setViewName("login");
		return mdv;
	}
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView index() {
		ModelAndView mdv = new ModelAndView();
		mdv.setViewName("index");
		return mdv;
	}

	@RequestMapping(value = "/logon", method = RequestMethod.POST)
	@ResponseBody
	private Map<String , Object> logon(String erpNo, String password , ServletResponse response) {
		Map<String , Object> returnMap = new HashMap<String , Object>();
		BaseAuthToken token = new BaseAuthToken("demo", "demo");
		Subject subject = SecurityUtils.getSubject();
		StringBuilder errorMsg = new StringBuilder("");
		try {
			if (subject.isAuthenticated()) {
				subject.logout();
			}
			subject.login(token);
		} catch (UnknownAccountException uae) {
			errorMsg.append("登录失败,").append(uae.getMessage());
			logger.info(errorMsg.toString());
		} catch (IncorrectCredentialsException ice) {
			errorMsg.append("登录失败,帐号[").append(erpNo).append("]用户名密码错误!");
			logger.info(errorMsg.toString());
		} catch (DisabledAccountException dae) {
			errorMsg.append("登录失败,帐号[").append(erpNo).append("]处于停用状态!");
			logger.info(errorMsg.toString());
		} catch (AuthenticationException ae) {
			errorMsg.append("系统登录时发生异常,认证实现代码和远程认证服务连接是否正常!");
			logger.info(errorMsg.toString());
			ae.printStackTrace();
		}
		returnMap.put("msg", errorMsg.toString());
		returnMap.put("token", "");
		return returnMap;
	}
	
	/**
	 * 登出
	 */
	public void logout(){
		Subject subject = SecurityUtils.getSubject();
		if(subject != null){
			subject.logout();
		}
	}

	@RequestMapping(value = "/func1", method = RequestMethod.GET)
	public ModelAndView func1() {
		ModelAndView mdv = new ModelAndView();
		mdv.setViewName("func1-index");
		return mdv;
	}

	@RequestMapping(value = "/func2", method = RequestMethod.GET)
	public ModelAndView func2() {
		ModelAndView mdv = new ModelAndView();
		mdv.setViewName("func2-index");
		return mdv;
	}

	@RequestMapping(value = "/getFuncs", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getFuncs(String userId,
			HttpServletRequest request) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Map<String, Object> func1 = new HashMap<String, Object>();
		func1.put("funcId", "01");
		func1.put("funcNm", "功能点1");
		func1.put("templateUrl", "functions/demo/template/Func01.tmp.html");
		func1.put("controllerNm", "DemoController");
		func1.put("controllerPath", "functions/demo/DemoController");
		Map<String, Object> func2 = new HashMap<String, Object>();
		func2.put("funcId", "02");
		func2.put("state", "func02");
		func2.put("templateUrl", "functions/demo/template/Func02.tmp.html");
		func2.put("funcNm", "功能点2");
		func2.put("controllerNm", "DemoController");
		func2.put("controllerPath", "functions/demo/DemoController");
		Map<String, Object> func3 = new HashMap<String, Object>();
		func3.put("funcId", "03");
		func3.put("state", "func03");
		func3.put("templateUrl", "functions/dataview/template/Chart01.tmp.html");
		func3.put("funcNm", "可视化1");
		func3.put("controllerNm", "DataviewController");
		func3.put("controllerPath", "functions/dataview/DataviewController");
		List<Map<String, Object>> funcList = new ArrayList<Map<String, Object>>();
		funcList.add(func1);
		funcList.add(func2);
		funcList.add(func3);
		returnMap.put("funcs", funcList);
		return returnMap;
	}
	
	@RequestMapping(value = "/getUsers" , method = RequestMethod.POST)
	@ResponseBody
	public Map<String , Object> getUsers(Pager pager){
		Map<String , Object> returnMap = new HashMap<String , Object>();
		returnMap.put("users", this.frameTestBS.getUsers());
		return returnMap;
	}

}
