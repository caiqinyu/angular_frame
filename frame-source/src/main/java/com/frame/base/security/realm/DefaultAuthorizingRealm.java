package com.frame.base.security.realm;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import com.frame.base.security.BaseAuthToken;

/**
 * @author caiqy
 * @version 1.00.00
 * 
 */
@Component
public class DefaultAuthorizingRealm extends AuthorizingRealm {

	public DefaultAuthorizingRealm() {

	}

	/**
	 * 认证回调函数,获取用户认证信息(用户名，密码)
	 */
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) {
		char[] pass = {'1'};
		BaseAuthToken token = (BaseAuthToken) authcToken;
		token.setUsername("caiqinyu");
		token.setPassword(pass);
		// 这是个测试，模拟必登录成功
		SimpleAuthenticationInfo authInfo = new SimpleAuthenticationInfo(token.getUsername(),
				pass, this.getName());
		return authInfo;
	}

	/**
	 * 获取当前用户的授权信息
	 */
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		SimpleAuthorizationInfo authzInfo = new SimpleAuthorizationInfo();

		return authzInfo;

	}

}
