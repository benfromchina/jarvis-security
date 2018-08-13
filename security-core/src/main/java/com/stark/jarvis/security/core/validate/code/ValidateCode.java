package com.stark.jarvis.security.core.validate.code;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

/**
 * 验证码。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public class ValidateCode implements Serializable {
	
	private static final long serialVersionUID = 8690772146728014326L;

	private String code;
	
	private Date expireTime;
	
	public ValidateCode(String code, Date expireTime) {
		this.code = code;
		this.expireTime = expireTime;
	}
	
	public ValidateCode(String code, int expireIn) {
		this.code = code;
		this.expireTime = DateUtils.addSeconds(new Date(), expireIn);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}
	
	public boolean isExpired() {
		return new Date().after(expireTime);
	}
	
}
