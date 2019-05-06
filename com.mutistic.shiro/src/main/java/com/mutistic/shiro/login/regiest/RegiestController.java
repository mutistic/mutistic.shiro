package com.mutistic.shiro.login.regiest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mutistic.shiro.core.controller.BaseController;
import com.mutistic.shiro.core.utils.ValidateUtil;

@RestController
@RequestMapping("/regiest")
public class RegiestController extends BaseController {
	
	/**
	 * 验证码
	 * 
	 * @param data
	 * @return
	 */
	@PostMapping("/sendCode")
	public Object login(@RequestBody RegiestDto data) {
		ValidateUtil.notNull(data, "登录信息");
		ValidateUtil.notNull(data.getMobile(), "手机号不能为空");
		
		Integer num = RegiestUtil.getNumber(data.getMobile());
		if(num > 5) {
			return warn(data.getMobile()+" 今日获取验证码次数超过上限！");
		}
		
		String code = RegiestUtil.setMobileInfo(data.getMobile());
		// TODO 发送短信
		
		return success("验证码："+code+"，发送成功!");
	}

}
