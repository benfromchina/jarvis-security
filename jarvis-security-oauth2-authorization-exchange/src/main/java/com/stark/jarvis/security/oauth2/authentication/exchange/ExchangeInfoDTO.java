package com.stark.jarvis.security.oauth2.authentication.exchange;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 交换令牌信息
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/11/29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeInfoDTO implements Serializable {

    /** 类型 */
    private String type;

    /** 用户ID */
    private String userId;

    /** 令牌 */
    private String token;

}
