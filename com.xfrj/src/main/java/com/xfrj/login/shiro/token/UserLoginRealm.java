package com.xfrj.login.shiro.token;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.xfrj.core.contants.AuthorizationContant;
import com.xfrj.core.utils.DataConverUtil;
import com.xfrj.login.shiro.UserDto;
import com.xfrj.login.shiro.UserToken;
import com.xfrj.user.model.UserEntity;
import com.xfrj.user.service.ILoginService;

/**
 * Shiro 用户认证规则
 */
public class UserLoginRealm extends AuthorizingRealm {

	@Autowired
	private ILoginService loginService;

	/**
	 * 获取用户权限
	 * 
	 * @param principals
	 * @return
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		System.out.println("————权限认证————");
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

		UserDto dto = (UserDto) SecurityUtils.getSubject().getPrincipal();
		if (dto == null) {
			return info;
		} else if (Integer.valueOf(1).equals(dto.getAuthorType())) {
			info.setRoles(AuthorizationContant.AUTHOR_SYS_SET); // 设置管理员
		} else if (Integer.valueOf(0).equals(dto.getAuthorType())) {
			info.setRoles(AuthorizationContant.AUTHOR_APP_SET); // 设置移动端
		} else {
			return info;
		}
		return info;
	}

	/**
	 * 获取身份验证信息 Shiro中，最终是通过 Realm 来获取应用程序中的用户、角色及权限信息的。
	 *
	 * @param authenticationToken
	 *            用户身份信息 token
	 * @return 返回封装了用户信息的 AuthenticationInfo 实例
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
			throws AuthenticationException {
		System.out.println("————身份认证方法————");
		UserToken token = (UserToken) authenticationToken;
		// 从数据库获取对应用户名密码的用户
		UserEntity user = loginService.queryUser(token.getUsername());
		if (null == user) {
			throw new AccountException("用户名不正确");
		}

		Integer loginNum = TokenUtil.getLoginNum(user.getId());
		if (loginNum >= 5) {
			throw new AccountException("密码输入错误次数已达上限，请修改密码后重新登陆！");
		}
		if (!user.getPassword().equals(DataConverUtil.char2String(token.getCredentials()))) {
			loginNum = TokenUtil.setLoginNum(user.getId());
			throw new AccountException("密码不正确，剩余重试次数：" + (5 - loginNum));
		}
		if(!user.getAuthorType().equals(token.getAuthorType())) {
			throw new AccountException("您不是本站会员！");
		}

		return new SimpleAuthenticationInfo(getSession(user), user.getPassword(), getName());
	}

	private UserDto getSession(UserEntity user) {
		UserDto dto = new UserDto();
		dto.setId(user.getId());
		dto.setUsername(user.getUsername());
		dto.setPassword(user.getPassword());
		dto.setAuthorType(user.getAuthorType());
		dto.setLastLoginTime(System.currentTimeMillis());
		dto.setExpirationTime(dto.getLastLoginTime() + TokenUtil.TOKEN_EXPIRATION_TIME * 1000);
		if(dto.getAuthorType().intValue() == 0) {
			dto.setToken(TokenUtil.encryptToken(dto.getId().toString(), dto.getExpirationTime()));
		}
		return dto;
	}

}
