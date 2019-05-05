package com.xfrj.login.shiro.token;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.UsernamePasswordToken;

import com.xfrj.core.redis.RedisUtil;
import com.xfrj.core.security.SecurityUtil;
import com.xfrj.login.shiro.UserDto;

/**
 * Token 工具类
 */
public class TokenUtil {

	/** token head */
	public final static String TOKEN_HEAD = "token";
	/** token key */
	public final static String TOKEN_PREFIX = "S:mutistic:TOKEN_KEY:";
	/** token盐值 */
	public final static String TOKEN_SALT = "#&#";
	/** token过期时间(秒) <br/> 默认：7*24*60*60 = 604800  */
	public final static Long TOKEN_EXPIRATION_TIME = 7 * 24 * 60 * 60l;
	/** 错误登录次数 key*/
	public final static String ERROR_LOGIN_NUM_PREFIX = "S:mutistic:ERROR_LOGIN_NUM:";
	/** 错误登录次数 过期时间(秒) */
	public final static Long ERROR_LOGIN_NUM_EXP_TIME = 24 * 60 * 60l;

	/**
	 * token加密
	 * 
	 * @param data
	 * @param expTimeMillis
	 * @return
	 */
	public static String encryptToken(String data, Long expTimeMillis) {
		return SecurityUtil.encryptDES(data + TOKEN_SALT + expTimeMillis);
	}

	/**
	 * token解密
	 * 
	 * @param token
	 * @return
	 */
	public static UserDto decryptToken(String token) {
		try {
			String data = SecurityUtil.decryptDES(token);
			String[] array = data.split(TOKEN_SALT);
			UserDto dto = new UserDto();
			dto.setId(Long.valueOf(array[0]));
			dto.setExpirationTime(Long.valueOf(array[1]));
			return dto;
		} catch (Exception e) {
			e.printStackTrace(); // TODO
			exception("token无效，请重新登陆！", e);
		}
		return null;
	}

	/**
	 * 验证token
	 * 
	 * @param token
	 */
	public static UserDto validateToken(String token) {
		UserDto dto = decryptToken(token);
		notNull(dto, "token非法，请重新登陆！");
		return dto;
	}

	public static UsernamePasswordToken user2Token(UserDto user) {
		UsernamePasswordToken token = new UsernamePasswordToken();
		token.setUsername(user.getUsername());
		token.setPassword(user.getPassword().toCharArray());
		return token;
	}
	
	public static String tokenKey(String token) {
		return TOKEN_PREFIX + token;
	}
	
	public static String errorLoginNumKey(Long id) {
		return ERROR_LOGIN_NUM_PREFIX + id;
	}

	public static UserDto getTokenInfo(String token) {
		return (UserDto) RedisUtil.opsForValue.get(tokenKey(token));
	}
	
	public static void setTokenInfo(UserDto dto) {
		RedisUtil.opsForValue.set(tokenKey(dto.getToken()), dto, TOKEN_EXPIRATION_TIME);
	}
	
	public static void deleteTokenInfo(String token) {
		RedisUtil.opsForValue.delete(tokenKey(token));
	}

	public static Integer getLoginNum(Long id) {
		Integer num = (Integer) RedisUtil.opsForValue.get(errorLoginNumKey(id));
		return num == null ? 0 : num;
	}
	
	public static Integer setLoginNum(Long id) {
		Integer num = getLoginNum(id) + 1;
		RedisUtil.opsForValue.set(errorLoginNumKey(id), num, ERROR_LOGIN_NUM_EXP_TIME);
		return num;
	}
	
	public static void deleteLoginNum(Long id) {
		RedisUtil.opsForValue.delete(errorLoginNumKey(id));
	}
	
	public static void notNull(Object obj, String string) {
		if(obj == null) {
			exception(string);
		}
	}
	public static void exception(String msg) {
		throw new AccountException(msg == null ? "" : msg);
	}

	public static void exception(String msg, Throwable e) {
		throw new AccountException(msg == null ? "" : msg, e);
	}

	public static void main(String[] args) {
		String aa = TokenUtil.encryptToken("1026448466399707138", System.currentTimeMillis());
		System.out.println(TokenUtil.decryptToken(aa));
	}
}
