[![](https://img.shields.io/badge/maven%20central-v2.0.2-brightgreen)](https://central.sonatype.com/artifact/io.github.benfromchina/jarvis-security)
[![](https://img.shields.io/badge/release-v2.0.2-blue)](https://gitee.com/jarvis-lib/jarvis-security/releases/v2.0.2)
[![](https://img.shields.io/badge/Java-17-9cf)](https://www.oracle.com/cn/java/technologies/downloads/#java17)
[![](https://img.shields.io/badge/Spring_Authorization_Server-1.3.1-27A0C9)](https://docs.spring.io/spring-authorization-server/reference/index.html)
[![](https://img.shields.io/badge/Spring%20Boot-3.3.2-blue)](https://github.com/spring-projects/spring-boot/tree/v3.3.2)
[![](https://img.shields.io/badge/Spring%20Cloud-2023.0.3-brightgreen)](https://docs.spring.io/spring-cloud-release/reference/index.html)
[![](https://img.shields.io/badge/Spring%20Framework-6.1.11-blueviolet)](https://github.com/spring-projects/spring-framework/tree/v6.1.11)

### 目录
- [介绍](#介绍)
    - [简介](#简介)
    - [架构](#架构)
- [功能](#功能)
    - [HttpSecurityConfigurer 替换 WebSecurityConfigurerAdapter 配置 HttpSecurity](#httpsecurityconfigurer-替换-websecurityconfigureradapter-配置-httpsecurity)
    - [配置不需要认证授权的请求](#配置不需要认证授权的请求)
    - [OAuth2.0第三方登录](#oauth20第三方登录)
        - [QQ](#qq)
        - [支付宝](#支付宝)
        - [开源中国](#开源中国)
- [基于`jarvis-security-social`快速开发第三方登录](#基于jarvis-security-social快速开发第三方登录)
    - [OAuth2.0流程图](#oauth20流程图)
    - [必选的接口](#必选的接口)
        - [客户端注册构造器](#客户端注册构造器)
        - [封装用户信息对象](#封装用户信息对象)
    - [可选的接口](#可选的接口)
        - [`1`步骤中获取授权码请求参数自定义](#1步骤中获取授权码请求参数自定义)
        - [`4.1`步骤中返回的授权码参数名不叫`code`](#41步骤中返回的授权码参数名不叫code)
        - [`5`步骤中获取令牌请求参数自定义](#5步骤中获取令牌请求参数自定义)
        - [`6`步骤中获取令牌响应参数处理](#6步骤中获取令牌响应参数处理)
        - [`5`到`6`获取令牌过程自定义](#5到6获取令牌过程自定义)
        - [`6.1`步骤中获取用户信息请求参数自定义](#61步骤中获取用户信息请求参数自定义)
        - [`7`步骤中获取用户信息响应参数处理](#7步骤中获取用户信息响应参数处理)
        - [`6.1`到`7`获取用户信息过程自定义](#61到7获取用户信息过程自定义)
        - [持久化用户第三方登录信息](#持久化用户第三方登录信息)

### 介绍

#### 简介

`jarvis-security`是一个基于`Spring Authorization Server`的**无状态**的**后端服务**的**安全框架**，旨在通过简单的配置，实现`Spring Authorization Server`的功能，并可以简单快速地扩展其他的授权方式。

#### 架构

```
jarvis-security                                          // 父模块，统一维护依赖版本、公共配置属性、maven 插件配置等，供其他模块引用和继承
├── jarvis-security-oauth2-authorization-server          // 基于 Spring Authorization Server 实现的授权服务器
├── jarvis-security-oauth2-authorization-core            // 核心包，基于该模块可扩展自定义的 OAuth2.0 授权方式
├── jarvis-security-oauth2-authorization-password        // 支持密码模式登录
├── jarvis-security-oauth2-authorization-sms             // 支持短信验证码模式登录
└── jarvis-security-oauth2-authorization-wxmp            // 支持微信小程序模式登录
```

### 功能

#### 配置授权服务器

1. 引入依赖

```xml
<dependency>
    <groupId>io.github.benfromchina</groupId>
    <artifactId>jarvis-security-oauth2-authorization-server</artifactId>
    <version>2.0.4</version>
</dependency>
```

2. 实现 `UsernameUserDetailsServiceProvider` 接口，用于根据用户名获取用户信息

```java
@Component
@AllArgsConstructor
public class UsernameUserDetailsServiceProviderImpl implements UsernameUserDetailsServiceProvider {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO user = userService.getByUsername(username).orElseThrow(() -> new UsernameNotFoundException("用户名不存在"));
        return userToUserDetails(user); // 业务系统用户转换为 UserDetails
    }

}
```

3. 实现 `RegisteredClientRepository` 接口，用于 OAuth2.0 客户端信息存储

```java
@Component
@AllArgsConstructor
public class RegisteredClientServiceImpl implements RegisteredClientRepository {

    private final ClientService clientService;

    @Override
    public void save(RegisteredClient registeredClient) {
        // TODO: 保存客户端信息
    }

    @Override
    public RegisteredClient findById(String id) {
        // 注意抛出异常类型必须为 OAuth2AuthenticationException
        ClientDTO client = clientService.get(id).orElseThrow(() -> new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT));
        return clientToRegisteredClient(client);    // 业务系统客户端转换为 RegisteredClient
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        // 注意抛出异常类型必须为 OAuth2AuthenticationException
        ClientDTO client = clientService.getByClientId(clientId).orElseThrow(() -> new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT));
        return clientToRegisteredClient(client);    // 业务系统客户端转换为 RegisteredClient
    }

}
```

3.

#### 配置不需要认证授权的请求

1. 配置文件方式

```yml
spring:
  security:
    permit-all-requests:
      - path: /actuator/health
        method: GET
      - path: /path_all_methods
```

2. 实现`PermitAllRequestsSupplier`接口

```java
@Component
public class PermitAllRequestsSupplierImpl implements PermitAllRequestsSupplier {

    @Override
    public List<SecurityProperties.Request> get() {
        List<SecurityProperties.Request> requests = new ArrayList<>();
        // 指定请求方式
        requests.add(new SecurityProperties.Request("/actuator/health", SecurityProperties.HttpMethod.GET));
        // 所有请求方式
        requests.add(new SecurityProperties.Request("/path_all_methods"));
        return requests;
    }

}
```

#### 存储授权信息实现强制下线

实现 `OAuth2AuthorizationService` 接口，除了官方的 `InMemoryOAuth2AuthorizationService` 和 `JdbcOAuth2AuthorizationService` 之外，内置了 `RedisOAuth2AuthorizationService` 。

```java
@Configuration
public class AuthServerConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private SecurityProperties securityProperties;

    @Bean
    public OAuth2AuthorizationService oauth2AuthorizationService() {
        return new RedisOAuth2AuthorizationService(redisTemplate(), securityProperties);
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate() {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}
```

2. 配置参数

```yml
spring:
  security:
    oauth2:
      client:
        registration:
          qq:
            client-id: xxxxxxxxx
            client-secret: xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            redirect-uri: http://localhost/login/oauth2/code/qq
```

#### 支付宝

1. 引入依赖

```xml
<dependency>
    <groupId>io.github.benfromchina</groupId>
    <artifactId>jarvis-security-social-alipay</artifactId>
    <version>1.0.1</version>
</dependency>
```

2. 配置参数

```yml
spring:
  security:
    oauth2:
      client:
        registration:
          alipay:
            client-id: xxxxxxxxxxxxxxxx
            redirect-uri: http://localhost/login/oauth2/code/alipay
            # 私钥文件路径，支持 classpath 或磁盘绝对路径，可选配置（实现PrivateKeySupplier接口）
            private-key-path: classpath:alipay/应用私钥2048.txt
            # 支付宝公钥文件路径，支持 classpath 或磁盘绝对路径，可选配置（实现AlipayPublicKeySupplier接口）
            alipay-public-key-path: classpath:alipay/支付宝公钥2048.txt
```

3. 自定义获取私钥接口

实现 [PrivateKeySupplier](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social-alipay/src/main/java/com/stark/jarvis/security/social/alipay/security/PrivateKeySupplier.java) 接口

4. 自定义获取支付宝公钥接口

实现 [AlipayPublicKeySupplier](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social-alipay/src/main/java/com/stark/jarvis/security/social/alipay/security/AlipayPublicKeySupplier.java) 接口

#### 开源中国

1. 引入依赖

```xml
<dependency>
    <groupId>io.github.benfromchina</groupId>
    <artifactId>jarvis-security-social-oschina</artifactId>
    <version>1.0.1</version>
</dependency>
```

2. 配置参数

```yml
spring:
  security:
    oauth2:
      client:
        registration:
          oschina:
            client-id: xxxxxxxxxxxxxxxxxxxx
            client-secret: xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
            redirect-uri: http://localhost/login/oauth2/code/oschina
```

### 基于`jarvis-security-social`快速开发第三方登录

#### OAuth2.0流程图

<img src="https://gimg2.baidu.com/image_search/src=http%3A%2F%2Ff1.market.xiaomi.com%2Fdownload%2FMiPass%2F0f45243d348759584109cd6820a4bfb517342805a%2Fpassport_oauth_authorization_code.png&refer=http%3A%2F%2Ff1.market.xiaomi.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1635470749&t=42c473b899798543c4a1699978c22038" width="627" height="292">

> 注：以下接口描述中的数字与图中对应。

#### 必选的接口

##### 客户端注册构造器

实现 [ClientRegistrationBuilderProvider](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social/src/main/java/com/stark/jarvis/security/social/client/ClientRegistrationBuilderProvider.java)

> 参考 [OschinaClientRegistrationBuilderProvider](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social-oschina/src/main/java/com/stark/jarvis/security/social/oschina/client/OschinaClientRegistrationBuilderProvider.java)

##### 封装用户信息对象

实现 [OAuth2UserConverterProvider](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social/src/main/java/com/stark/jarvis/security/social/client/userinfo/OAuth2UserConverterProvider.java)

> 参考 [QQUserConverterProvider](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social-qq/src/main/java/com/stark/jarvis/security/social/qq/client/userinfo/QQUserConverterProvider.java)

#### 可选的接口

##### `1`步骤中获取授权码请求参数自定义

实现 [OAuth2AuthorizationRequestEnhancerProvider](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social/src/main/java/com/stark/jarvis/security/social/client/web/OAuth2AuthorizationRequestEnhancerProvider.java) 接口

> 参考 [AlipayAuthorizationRequestEnhancerProvider](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social-alipay/src/main/java/com/stark/jarvis/security/social/alipay/client/web/AlipayAuthorizationRequestEnhancerProvider.java)

##### `4.1`步骤中返回的授权码参数名不叫`code`

实现 [OAuth2AuthorizationCodeParameterNameProvider](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social/src/main/java/com/stark/jarvis/security/social/client/web/OAuth2AuthorizationCodeParameterNameProvider.java) 接口

> 参考[AlipayAuthorizationCodeParameterNameProvider](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social-alipay/src/main/java/com/stark/jarvis/security/social/alipay/client/web/AlipayAuthorizationCodeParameterNameProvider.java)

##### `5`步骤中获取令牌请求参数自定义

实现 [OAuth2AuthorizationCodeGrantRequestEntityConverterProvider](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social/src/main/java/com/stark/jarvis/security/social/client/web/OAuth2AuthorizationCodeGrantRequestEntityConverterProvider.java) 接口

> 参考[QQAuthorizationCodeGrantRequestConverterProvider](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social-qq/src/main/java/com/stark/jarvis/security/social/qq/client/web/QQAuthorizationCodeGrantRequestConverterProvider.java)

##### `6`步骤中获取令牌响应参数处理

实现 [OAuth2AccessTokenResponseConverterProvider](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social/src/main/java/com/stark/jarvis/security/social/client/web/OAuth2AccessTokenResponseConverterProvider.java) 接口

```java
@Component
public class AkanAccessTokenResponseConverterProvider implements OAuth2AccessTokenResponseConverterProvider {

    @Override
    public boolean supports(ClientRegistration clientRegistration) {
        return Constants.REGISTRATION_ID.equalsIgnoreCase(clientRegistration.getRegistrationId());
    }

    @Override
    public OAuth2AccessTokenResponse convert(ClientRegistration clientRegistration, Map<String, String> tokenResponseParameters) {
        try {
            tokenResponseParameters = AkanUtils.getData(tokenResponseParameters, clientRegistration.getClientSecret());
        } catch (Exception e) {
            throw new HttpMessageConversionException(e.getMessage(), e);
        }
        return convert(tokenResponseParameters);
    }

}
```

##### `5`到`6`获取令牌过程自定义

实现 [OAuth2AccessTokenResponseClientProvider](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social/src/main/java/com/stark/jarvis/security/social/client/endpoint/OAuth2AccessTokenResponseClientProvider.java) 接口

> 参考 [AlipayAccessTokenResponseClientProvider](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social-alipay/src/main/java/com/stark/jarvis/security/social/alipay/client/endpoint/AlipayAccessTokenResponseClientProvider.java)

##### `6.1`步骤中获取用户信息请求参数自定义

实现 [OAuth2UserRequestEntityConverterProvider](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social/src/main/java/com/stark/jarvis/security/social/client/userinfo/OAuth2UserRequestEntityConverterProvider.java) 接口

> 参考 [QQUserRequestEntityConverterProvider](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social-qq/src/main/java/com/stark/jarvis/security/social/qq/client/userinfo/QQUserRequestEntityConverterProvider.java)

##### `7`步骤中获取用户信息响应参数处理

实现 [OAuth2UserInfoResponseHttpMessageConverterProvider](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social/src/main/java/com/stark/jarvis/security/social/client/userinfo/OAuth2UserInfoResponseHttpMessageConverterProvider.java) 接口

> 参考 [QQUserInfoResponseHttpMessageConverterProvider](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social-qq/src/main/java/com/stark/jarvis/security/social/qq/client/userinfo/QQUserInfoResponseHttpMessageConverterProvider.java)

##### `6.1`到`7`获取用户信息过程自定义

实现 [OAuth2UserInfoResponseClientProvider](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social/src/main/java/com/stark/jarvis/security/social/client/userinfo/OAuth2UserInfoResponseClientProvider.java) 接口

> 参考 [AlipayUserInfoResponseClientProvider](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social-alipay/src/main/java/com/stark/jarvis/security/social/alipay/client/userinfo/AlipayUserInfoResponseClientProvider.java)

##### 持久化用户第三方登录信息

实现 [UserConnectionRepository](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social/src/main/java/com/stark/jarvis/security/social/client/userinfo/UserConnectionRepository.java) 接口

```java
@Service
public class UserConnectionServiceImpl implements UserConnectionRepository {

    @Autowired
    private UserService userService;

    @Override
    public UserConnectionForm saveForm(UserConnectionForm form) {
        OAuth2UserDetails formUser = form.getUser();
        UserConnection formUserConnection = form.getUserConnection();

        UserDetailsImpl user = new UserDetailsImpl();
        BeanUtils.copyProperties(formUser, user);
        UserConnectionImpl userConnection = new UserConnectionImpl();
        BeanUtils.copyProperties(formUserConnection, userConnection);
        if (userConnection.getUserId() == null) {
            userConnection.setUserId(0l);
        }

        com.eastsoft.esstock.core.form.manager.UserConnectionForm savedForm = userService.saveUserConnection(new com.eastsoft.esstock.core.form.manager.UserConnectionForm(user, userConnection));

        user = new UserDetailsImpl();
        BeanUtils.copyProperties(savedForm.getUser(), user);
        userConnection = new UserConnectionImpl();
        BeanUtils.copyProperties(savedForm.getUserConnection(), userConnection);
        return new UserConnectionForm(user, userConnection);
    }

}
```