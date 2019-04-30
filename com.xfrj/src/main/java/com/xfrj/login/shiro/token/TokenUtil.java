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
	/** token前缀 */
	public final static String TOKEN_PREFIX = "S:mutistic:TOKEN_KEY:";
	/** token盐值 */
	public final static String TOKEN_SALT = "#&#";
	/**
	 * 过期时间(毫秒) <br/>
	 * 默认：7*24*60*60 = 604800
	 */
	public final static Long EXPIRATION_TIME = 7 * 24 * 60 * 60l;

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

	public static UserDto getTokenInfo(String token) {
		return (UserDto) RedisUtil.opsForValue.get(TOKEN_PREFIX + token);
	}
	
	public static void setTokenInfo(UserDto dto) {
		RedisUtil.opsForValue.set(TOKEN_PREFIX + dto.getToken(), dto, EXPIRATION_TIME);
	}

	public static void deleteTokenInfo(String token) {
		RedisUtil.opsForValue.delete(TOKEN_PREFIX + token);
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
