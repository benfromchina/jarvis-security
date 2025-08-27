# Jarvis Security OAuth2 Authorization Module

This is an OAuth2 authorization service module suite based on Spring Security, providing support for multiple authentication methods. Suitable for building secure authentication and authorization service systems.

## Module Description

This project contains the following core modules:

### Core Module
- **OAuth2ResourceOwnerBaseAuthenticationConverter**  
  Base class for authentication request converters, defining common authentication parameter processing logic

- **OAuth2ResourceOwnerBaseAuthenticationProvider**  
  Base class for authentication providers, implementing common logic such as client verification and token generation

- **OAuth2ResourceOwnerBaseAuthenticationToken**  
  Base class for authentication tokens, containing basic properties such as scope and client information

### Authentication Extension Modules
- **Password Mode** (`password`)  
  Implements the standard OAuth2 password grant type

- **SMS Code** (`sms-code`)  
  Supports authentication via mobile phone number and SMS verification code

- **WeChat Mini Program** (`wxmp`)  
  Integrates WeChat Mini Program login authentication, including implementation of WeChat API calls

- **Token Exchange** (`exchange`)  
  Supports authentication by exchanging existing tokens for new ones

### Authorization Server Module
- Provides complete OAuth2 authorization server configuration
- Supports JWT token generation and validation
- Implements authorization information storage with Redis
- Includes comprehensive security framework such as exception handling and security configuration

## Key Features

- Support for multiple authentication methods (password, SMS, WeChat, token exchange)
- Complete OAuth2 authorization service implementation
- Support for JWT token generation and validation
- Redis-based distributed session support
- Unified exception handling mechanism
- Extensible UserDetailsService interface

## Usage Instructions

1. Include the required module dependencies
2. Configure the SecurityProperties security parameters
3. Implement the UserDetailsServiceProvider interface
4. Configure authorization server parameters (e.g., JWT keys, Redis connection, etc.)
5. Extend authentication methods as needed

## Configuration Example

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

## License

This project is licensed under the Apache 2.0 License. See the LICENSE file for details.