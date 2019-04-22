package com.xfrj.base.config;

import java.util.HashSet;
import java.util.Set;

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

import com.alibaba.fastjson.JSONObject;
import com.xfrj.base.utils.DataConverUtil;
import com.xfrj.user.model.UserEntity;
import com.xfrj.user.service.ILoginService;

// 实现AuthorizingRealm接口用户用户认证
public class UserRealm extends AuthorizingRealm {

	@Autowired
	private ILoginService loginService;
	
	// 获取授权信息
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		System.out.println("————权限认证————");
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        
        Set<String> set = new HashSet<>();
        set.add("user");
        set.add("sys");
//        String role = loginService.getRole(username);    //获得该用户角色
//        set.add(role);  //需要将 role 封装到 Set 作为 info.setRoles() 的参数
        info.setRoles(set);  //设置该用户拥有的角色
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
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(token.getPrincipal(), user, getName());
        System.out.println(JSONObject.toJSON(info));
        return info;
    }

}
