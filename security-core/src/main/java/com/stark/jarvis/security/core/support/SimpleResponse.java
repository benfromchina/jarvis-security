package com.stark.jarvis.security.core.support;

import java.io.Serializable;

/**
 * 简单响应对象。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public class SimpleResponse implements Serializable {

	private static final long serialVersionUID = -4336416122353842890L;
	
	private Object message;
	
	public SimpleResponse(Object message) {
		this.message = message;
	}

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}
	
}
