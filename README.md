# Jarvis Security OAuth2 授权模块

这是一个基于 Spring Security 的 OAuth2 授权服务模块集合，提供多种认证方式支持。适用于构建安全的认证授权服务系统。

## 模块说明

本项目包含以下核心模块：

### 核心模块
- **OAuth2ResourceOwnerBaseAuthenticationConverter**  
  认证请求转换器基类，定义通用认证参数处理逻辑

- **OAuth2ResourceOwnerBaseAuthenticationProvider**  
  认证提供者基类，实现客户端验证、令牌生成等公共逻辑

- **OAuth2ResourceOwnerBaseAuthenticationToken**  
  认证令牌基类，包含作用域、客户端信息等基础属性

### 认证扩展模块
- **密码模式** (`password`)  
  实现标准 OAuth2 密码授权模式

- **短信验证码** (`sms-code`)  
  支持手机号+短信验证码的认证方式

- **微信小程序** (`wxmp`)  
  集成微信小程序登录认证，包含微信接口调用实现

- **令牌交换** (`exchange`)  
  支持已有令牌换取新令牌的认证方式

### 授权服务器模块
- 提供完整的 OAuth2 授权服务器配置
- 支持 JWT 令牌生成与验证
- 集成 Redis 的授权信息存储实现
- 包含异常处理、安全配置等完整安全体系

## 主要功能

- 多种认证方式支持（密码、短信、微信、令牌交换）
- 完整的 OAuth2 授权服务实现
- JWT 令牌生成与验证支持
- Redis 分布式会话支持
- 统一的异常处理机制
- 可扩展的用户详情服务接口

## 使用说明

1. 引入需要的模块依赖
2. 配置 SecurityProperties 安全参数
3. 实现 UserDetailsServiceProvider 接口
4. 配置授权服务器参数（如 JWT 密钥、Redis 连接等）
5. 根据需要扩展认证方式

## 配置示例

```yaml
spring:
  security:
    oauth2:
      wxmp:
        app-id: your_appid
        app-secret: your_secret
      jwt:
        key-pair-file: classpath:keystore.jks
        key-store-password: storepass
        key-pair-alias: alias
```

## 许可证

本项目遵循 Apache 2.0 许可证，详细信息请查看 LICENSE 文件。