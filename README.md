[![](https://img.shields.io/badge/maven%20central-v1.0.1-brightgreen)](https://search.maven.org/artifact/io.github.benfromchina/jarvis-security)
[![](https://img.shields.io/badge/release-v1.0.1-blue)](https://gitee.com/jarvis-lib/jarvis-security/releases/v1.0.1)
[![](https://img.shields.io/badge/Java-8-9cf)](https://www.oracle.com/java/technologies/javase/javase8u211-later-archive-downloads.html)
[![](https://img.shields.io/badge/Spring%20Boot-2.2.6.RELEASE-blue)](https://docs.spring.io/spring-boot/docs/2.2.6.RELEASE/reference/html/)
[![](https://img.shields.io/badge/Spring%20Cloud-Hoxton.SR4-brightgreen)]()
[![](https://img.shields.io/badge/Spring%20Framework-5.2.6.RELEASE-blueviolet)](https://docs.spring.io/spring-framework/docs/5.2.5.RELEASE/spring-framework-reference/)

### 目录

- [介绍](#介绍)
  - [简介](#简介)
  - [背景](#背景)
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

`jarvis-security`是一个基于`Spring Security 5`的**无状态**的**后端服务**的**安全框架**，引入相应模块可支持`QQ`、`支付宝`、`开源中国`等第三方登录。基于`jarvis-security-social`模块，可快速开发自定义的第三方`OAuth2.0`协议标准或非标准实现的登录模块。

#### 背景

1. 之前很精致的社交登录框架[Spring Social](https://docs.spring.io/spring-social/docs/1.1.x/reference/htmlsingle/)停止维护，见[Spring Social停止维护声明](https://spring.io/blog/2018/07/03/spring-social-end-of-life-announcement)；
2. [`Spring Security 5`](https://spring.io/blog/2018/03/06/using-spring-security-5-to-integrate-with-oauth-2-secured-services-such-as-facebook-and-github)作为替代方案，实现了**标准**的`OAuth2.0`协议。但是，国内主流的第三方登录服务提供商如[`QQ`](https://wiki.connect.qq.com/%e5%87%86%e5%a4%87%e5%b7%a5%e4%bd%9c_oauth2-0)、[`支付宝`](https://opendocs.alipay.com/open/284/web)等出于**安全加密**或其他未知的原因都**不是**那么标准（以下简称**不标准**），所以没法直接用；
3. 公司正好有个刚上码的项目需要用，不妨写一个。

#### 架构

```
jarvis-security                      // 父模块，统一维护依赖版本、公共配置属性、maven 插件配置等，供其他模块引用和继承
├── jarvis-security-core             // 核心包，底层的安全配置
├── jarvis-security-social           // OAuth2.0第三方登录核心模块，基于该模块可开发自己的第三方登录
├── jarvis-security-social-alipay    // 支付宝登录，基于jarvis-security-social模块
├── jarvis-security-social-oschina   // 开源中国登录，基于jarvis-security-social模块
└── jarvis-security-social-qq        // QQ登录，基于jarvis-security-social模块
```

### 功能

#### HttpSecurityConfigurer 替换 WebSecurityConfigurerAdapter 配置 HttpSecurity

1. 继承 `WebSecurityConfigurerAdapter` 接口配置 `HttpSecurity` 的方式将**失效**！！！

2. 实现 [HttpSecurityConfigurer](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-core/src/main/java/com/stark/jarvis/security/core/config/HttpSecurityConfigurer.java) 接口来配置 `HttpSecurity`。

#### 配置不需要认证授权的请求

1. 配置文件方式

```yml
spring:
  security:
    authorize-requests:
      permit-all:
      - http-method: GET
        path: /actuator/health,/actuator/hystrix.stream
```

2. 实现`AuthorizeRequestsPermitAllProvider`接口

```java
@Component
public class AuthorizeRequestsPermitAll implements AuthorizeRequestsPermitAllProvider {

	@Override
	public List<Request> getRequests() {
		List<Request> requests = new ArrayList<>();
		// 指定请求方式
		requests.add(new Request("GET", "/actuator/health"));
		// 所有请求方式
		requests.add(new Request("/actuator/hystrix.stream"));
		return requests;
	}

}
```

### OAuth2.0第三方登录

#### QQ

1. 引入依赖

```xml
<dependency>
    <groupId>io.github.benfromchina</groupId>
    <artifactId>jarvis-security-social-qq</artifactId>
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
