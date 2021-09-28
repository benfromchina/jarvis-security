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
  - [配置不需要认证授权的请求](#配置不需要认证授权的请求)
  - [实现`HttpSecurityConfigurer`接口直接配置`HttpSecurity`](#实现httpsecurityconfigurer接口直接配置httpsecurity)
  - [OAuth2.0第三方登录](#oauth20第三方登录)
    - [QQ](#qq)
    - [支付宝](#支付宝)
    - [开源中国](#开源中国)
- [基于`jarvis-security-social`快速开发第三方登录](基于jarvis-security-social快速开发第三方登录)
  - [OAuth2.0流程图](#oauth20流程图)
  - [必选的接口](#必选的接口)
    - [客户端注册构造器](ClientRegistrationBuilderProvider)
    - [封装用户信息对象](OAuth2UserConverterProvider)
  - [可选的接口](#可选的接口)
    - [`1`步骤中获取授权码请求需要对请求参数进行增删改查](OAuth2AuthorizationRequestEnhancerProvider)
    - [`4.1`步骤中返回的授权码参数名不叫`code`](OAuth2AuthorizationCodeParameterNameProvider)
    - [`5`步骤中使用授权码获取令牌请求需要对请求参数进行增删改查](OAuth2AuthorizationCodeGrantRequestEntityConverterProvider)
    - [获取令牌响应参数处理](OAuth2AccessTokenResponseConverterProvider)
    - [`5`到`6`获取令牌过程自定义](OAuth2AccessTokenResponseClientProvider)
    - [获取用户信息请求参数自定义](OAuth2UserRequestEntityConverterProvider)
    - [获取用户信息响应参数处理](OAuth2UserInfoResponseHttpMessageConverterProvider)
    - [获取用户信息过程自定义](OAuth2UserInfoResponseClientProvider)
    

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

#### 配置不需要认证授权的请求

1. 配置文件方式

2. 实现`AuthorizeRequestsPermitAllProvider`接口

#### 实现`HttpSecurityConfigurer`接口直接配置`HttpSecurity`

### OAuth2.0第三方登录

#### QQ

1. 引入依赖

```xml
<repositories>
    <repository>
        <id>eastsoft-snapshots</id>
        <name>Eastsoft Snapshots</name>
        <url>http://218.58.62.115:18081/nexus/repository/snapshots/</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>

<dependency>
    <groupId>com.stark</groupId>
    <artifactId>jarvis-security-social-qq</artifactId>
    <version>1.0.0-SNAPSHOT</version>
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
<repositories>
    <repository>
        <id>eastsoft-snapshots</id>
        <name>Eastsoft Snapshots</name>
        <url>http://218.58.62.115:18081/nexus/repository/snapshots/</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>

<dependency>
    <groupId>com.stark</groupId>
    <artifactId>jarvis-security-social-alipay</artifactId>
    <version>1.0.0-SNAPSHOT</version>
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

实现[PrivateKeySupplier](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social-alipay/src/main/java/com/stark/jarvis/security/social/alipay/security/PrivateKeySupplier.java)接口

4. 自定义获取支付宝公钥接口

实现[AlipayPublicKeySupplier](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social-alipay/src/main/java/com/stark/jarvis/security/social/alipay/security/AlipayPublicKeySupplier.java)接口

#### 开源中国

1. 引入依赖

```xml
<repositories>
    <repository>
        <id>eastsoft-snapshots</id>
        <name>Eastsoft Snapshots</name>
        <url>http://218.58.62.115:18081/nexus/repository/snapshots/</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>

<dependency>
    <groupId>com.stark</groupId>
    <artifactId>jarvis-security-social-oschina</artifactId>
    <version>1.0.0-SNAPSHOT</version>
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

#### 必选的接口

- [ClientRegistrationBuilderProvider](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social/src/main/java/com/stark/jarvis/security/social/client/ClientRegistrationBuilderProvider.java)

#### 可选的接口

- [OAuth2AuthorizationRequestEnhancerProvider](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social/src/main/java/com/stark/jarvis/security/social/client/web/OAuth2AuthorizationRequestEnhancerProvider.java)
- [OAuth2AuthorizationCodeGrantRequestEntityConverterProvider](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social/src/main/java/com/stark/jarvis/security/social/client/web/OAuth2AuthorizationCodeGrantRequestEntityConverterProvider.java)
- [OAuth2AuthorizationCodeParameterNameProvider](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social/src/main/java/com/stark/jarvis/security/social/client/web/OAuth2AuthorizationCodeParameterNameProvider.java)

#### 按流程解释各个接口

1. 实现[ClientRegistrationBuilderProvider](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social/src/main/java/com/stark/jarvis/security/social/client/ClientRegistrationBuilderProvider.java)接口，提供客户端注册构造器，包含客户端信息。

> 参考[QQClientRegistrationBuilderProvider](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social-qq/src/main/java/com/stark/jarvis/security/social/qq/client/QQClientRegistrationBuilderProvider.java)

2. 如果获取授权码请求**不标准**，比如`支付宝`的客户端标识参数名不叫`client_id`而叫`app_id`参数，只需实现[OAuth2AuthorizationRequestEnhancerProvider](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social/src/main/java/com/stark/jarvis/security/social/client/web/OAuth2AuthorizationRequestEnhancerProvider.java)接口。

> 参考[AlipayAuthorizationRequestEnhancerProvider](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social-alipay/src/main/java/com/stark/jarvis/security/social/alipay/client/web/AlipayAuthorizationRequestEnhancerProvider.java)

3. 如果获取授权码请求返回的响应**不标准**，比如`支付宝`的授权码不叫`code`而叫`auth_code`，只需实现[OAuth2AuthorizationCodeParameterNameProvider](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social/src/main/java/com/stark/jarvis/security/social/client/web/OAuth2AuthorizationCodeParameterNameProvider.java)接口，返回授权码参数名。

> 参考[AlipayAuthorizationCodeParameterNameProvider](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social-alipay/src/main/java/com/stark/jarvis/security/social/alipay/client/web/AlipayAuthorizationCodeParameterNameProvider.java)

4. 如果使用授权码获取令牌的请求**不标准**，比如`QQ`要返回`json`需要附加`fmt=json`参数，只需实现[OAuth2AuthorizationCodeGrantRequestEntityConverterProvider](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social/src/main/java/com/stark/jarvis/security/social/client/web/OAuth2AuthorizationCodeGrantRequestEntityConverterProvider.java)接口。

> 参考[QQAuthorizationCodeGrantRequestConverterProvider](https://gitee.com/jarvis-lib/jarvis-security/blob/master/jarvis-security-social-qq/src/main/java/com/stark/jarvis/security/social/qq/client/web/QQAuthorizationCodeGrantRequestConverterProvider.java)
