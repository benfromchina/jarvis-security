package com.stark.jarvis.security.app.social;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import com.stark.jarvis.security.app.support.AppSecretException;

/**
 * APP 环境下替换 {@link ProviderSignInUtils} ，避免由于没有 session 导致读不到社交用户信息的问题。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Component
public class AppSignInUtils {

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;
	@Autowired
	private UsersConnectionRepository usersConnectionRepository;
	@Autowired
	private ConnectionFactoryLocator connectionFactoryLocator;

	/**
	 * 缓存社交网站用户信息到 redis 。
	 * @param request HTTP 请求对象。
	 * @param connectionData 连接数据。
	 */
	public void saveConnectionData(WebRequest request, ConnectionData connectionData) {
		redisTemplate.opsForValue().set(getKey(request), connectionData, 10, TimeUnit.MINUTES);
	}

	/**
	 * 将缓存的社交网站用户信息与系统注册用户信息绑定。
	 * @param request HTTP 请求对象。
	 * @param userId 用户标识。
	 */
	public void doPostSignUp(WebRequest request, String userId) {
		String key = getKey(request);
		if (!redisTemplate.hasKey(key)) {
			throw new AppSecretException("缓存中的用户社交账号信息已失效");
		}
		ConnectionData connectionData = (ConnectionData) redisTemplate.opsForValue().get(key);
		Connection<?> connection = connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId())
				.createConnection(connectionData);
		usersConnectionRepository.createConnectionRepository(userId).addConnection(connection);
		
		redisTemplate.delete(key);
	}

	/**
	 * 获取 redis 存储的 key 。
	 * @param request HTTP 请求对象。
	 * @return redis 存储的 key 。
	 */
	private String getKey(WebRequest request) {
		String deviceId = request.getHeader("deviceId");
		if (StringUtils.isBlank(deviceId)) {
			throw new AppSecretException("参数 deviceId 不能为空");
		}
		return "jarvis:security:social:connect:" + deviceId;
	}

}
