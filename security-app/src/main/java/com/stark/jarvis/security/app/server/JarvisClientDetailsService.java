package com.stark.jarvis.security.app.server;

import java.util.List;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;

/**
 * 继承 {@link ClientDetailsService} 并增加获取客户端列表的方法。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public interface JarvisClientDetailsService extends ClientDetailsService {

	/**
	 * 获取所有客户端。
	 * @return 所有客户端列表。
	 */
	List<ClientDetails> getClients();
	
}
