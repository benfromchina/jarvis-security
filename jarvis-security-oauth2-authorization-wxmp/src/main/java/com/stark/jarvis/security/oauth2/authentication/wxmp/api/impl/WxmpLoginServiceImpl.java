package com.stark.jarvis.security.oauth2.authentication.wxmp.api.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.stark.jarvis.security.oauth2.authentication.wxmp.api.WxmpLoginService;
import com.stark.jarvis.security.oauth2.authentication.wxmp.api.dto.SessionDTO;
import com.stark.jarvis.security.oauth2.authentication.wxmp.api.util.WxmpUtils;
import com.stark.jarvis.security.oauth2.authentication.wxmp.config.WxmpProperties;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 微信小程序登录接口实现
 *
 * @author <a href="mailto:mengbin@hotmail.com">Ben</a>
 * @version 1.0.0
 * @since 2024/7/23
 */
@AllArgsConstructor
public class WxmpLoginServiceImpl implements WxmpLoginService {

    private final WxmpProperties wxmpProperties;

    private final RestTemplate restTemplate;

    @Override
    public SessionDTO code2Session(@NonNull String code) {
        UriComponents uri = UriComponentsBuilder
                .fromHttpUrl("https://api.weixin.qq.com/sns/jscode2session")
                .queryParam("appid", wxmpProperties.getAppId())
                .queryParam("secret", wxmpProperties.getAppSecret())
                .queryParam("js_code", code)
                .queryParam("grant_type", "authorization_code")
                .build();
        RequestEntity<Void> requestEntity = RequestEntity
                .get(uri.toUri())
                .accept(MediaType.APPLICATION_JSON)
                .build();
        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException(response.getBody());
        }

        JsonNode root;
        try {
            root = WxmpUtils.readTree(response.getBody());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        String sessionKey = root.has("session_key") ? root.get("session_key").asText() : "";
        String unionid = root.has("unionid") ? root.get("unionid").asText() : "";
        // String errmsg = root.has("errmsg") ? root.get("errmsg").asText() : "";
        String openid = root.has("openid") ? root.get("openid").asText() : "";
        int errcode = root.has("errcode") ? root.get("errcode").asInt() : 0;

        switch (errcode) {
            case 40029:
                throw new RuntimeException("js_code无效");
            case 45011:
                throw new RuntimeException("API 调用太频繁，请稍候再试");
            case 40226:
                throw new RuntimeException("高风险等级用户，小程序登录拦截。风险等级详见用户安全解方案");
            case -1:
                throw new RuntimeException("系统繁忙，此时请开发者稍候再试");
        }

        return new SessionDTO()
                .setSessionKey(sessionKey)
                .setOpenid(openid)
                .setUnionid(unionid);
    }

}
