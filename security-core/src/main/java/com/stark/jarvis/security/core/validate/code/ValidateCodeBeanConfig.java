package com.stark.jarvis.security.core.validate.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import com.stark.jarvis.security.core.boot.properties.SecurityProperties;
import com.stark.jarvis.security.core.validate.code.image.ImageCodeGenerator;
import com.stark.jarvis.security.core.validate.code.sms.DefaultSmsCodeSender;
import com.stark.jarvis.security.core.validate.code.sms.SmsCodeSender;

/**
 * 验证码相关的扩展点配置。
 * <p>配置在这里的 bean ，业务系统都可以通过声明同类型或同名的 bean 来覆盖安全模块默认的配置。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Configuration
public class ValidateCodeBeanConfig {
	
	@Autowired
	private SecurityProperties securityProperties;
	
	@Bean
	@Description("图片验证码图片生成器")
	@ConditionalOnMissingBean(name = "imageCodeGenerator")
	public ValidateCodeGenerator imageCodeGenerator() {
		ImageCodeGenerator codeGenerator = new ImageCodeGenerator(); 
		codeGenerator.setSecurityProperties(securityProperties);
		return codeGenerator;
	}
	
	@Bean
	@Description("短信验证码发送器")
	@ConditionalOnMissingBean(SmsCodeSender.class)
	public SmsCodeSender smsCodeSender() {
		return new DefaultSmsCodeSender();
	}

}
