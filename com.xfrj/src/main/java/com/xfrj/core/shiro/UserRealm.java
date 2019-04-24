package com.xfrj.core.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.xfrj.core.contants.AuthorizationContant;
import com.xfrj.core.utils.DataConverUtil;
import com.xfrj.user.model.UserEntity;
import com.xfrj.user.service.ILoginService;

/**
 * Shiro 用户认证规则
 */
public class UserRealm extends AuthorizingRealm {

	@Autowired
	private ILoginService loginService;
	
	/**
	 * 获取用户权限
	 * @param principals
	 * @return
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		System.out.println("————权限认证————");
		SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(AuthorizationContant.AUTHOR_SYS_SET);  //设置该用户拥有的角色
        return info;
	}

	
    /**
     * 获取身份验证信息
     * Shiro中，最终是通过 Realm 来获取应用程序中的用户、角色及权限信息的。
     *
     * @param authenticationToken 用户身份信息 token
     * @return 返回封装了用户信息的 AuthenticationInfo 实例
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("————身份认证方法————");
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        // 从数据库获取对应用户名密码的用户
        UserEntity user = loginService.queryUser(token.getUsername());
        if (null == user) {
            throw new AccountException("用户名不正确");
        } 
        if (!user.getPassword().equals(DataConverUtil.char2String(token.getCredentials()))) {
            throw new AccountException("密码不正确");
        }
        
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(toUserSession(user), user.getPassword(), getName());
        return info;
    }

    private UserSession toUserSession(UserEntity user) {
    	UserSession session = new UserSession();
    	session.setId(user.getId());
    	session.setUserName(user.getUsername());
    	session.setToken(TokenUtil.encryptToken(user.getUsername(), user.getPassword()));
    	return session;
    }
    
}
