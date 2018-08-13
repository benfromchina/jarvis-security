package com.stark.jarvis.security.core.boot.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 社交登录配置项。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@ConfigurationProperties(prefix = "spring.social")
public class SocialProperties {
	
	/**
	 * UserConnection 表前缀
	 */
	private String tablePrefix;
	
	public String getTablePrefix() {
		return tablePrefix;
	}

	public void setTablePrefix(String tablePrefix) {
		this.tablePrefix = tablePrefix;
	}
	
}
