package com.stark.jarvis.security.social.qq.client.userinfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stark.jarvis.security.social.client.userinfo.DefaultOAuth2UserDetails;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * QQ用户信息。
 * @author Ben
 * @since 1.0.1
 * @version 1.0.0
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QQOAuth2UserDetails extends DefaultOAuth2UserDetails {
	
	private static final long serialVersionUID = -2123005420568025209L;

	/** 用户标识 */
	private String openid;

	/** {未知属性} */
	private Integer isLost;
	
	/** 用户在QQ空间的昵称 */
	private String nickname;
	
	/** 性别。 如果获取不到则默认返回"男" */
	private String gender;
	
	/** {未知属性} */
	@JsonProperty("gender_type")
	private Integer genderType;
	
	/** 省份 */
	private String province;
	
	/** 城市 */
	private String city;
	
	/** 出生年份 */
	private String year;
	
	/** 星座 */
	private String constellation;
	
	/** 大小为30×30像素的QQ空间头像URL */
	private String figureurl;
	
	/** 大小为50×50像素的QQ空间头像URL */
	@JsonProperty("figureurl_1")
	private String figureurl1;
	
	/** 大小为100×100像素的QQ空间头像URL */
	@JsonProperty("figureurl_2")
	private String figureurl2;
	
	/** 大小为40×40像素的QQ头像URL */
	@JsonProperty("figureurl_qq_1")
	private String figureurlQq1;
	
	/** 大小为100×100像素的QQ头像URL。需要注意，不是所有的用户都拥有QQ的100x100的头像，但40x40像素则是一定会有 */
	@JsonProperty("figureurl_qq_2")
	private String figureurlQq2;
	
	/** 大小为640×640像素的QQ头像URL */
	@JsonProperty("figureurl_qq")
	private String figureurlQq;
	
	/** {未知属性} */
	@JsonProperty("figureurl_type")
	private String figureurlType;
	
	/** 是否黄钻 */
	@JsonProperty("is_yellow_vip")
	private String isYellowVip;
	
	/** 是否QQ会员 */
	private String vip;
	
	/** 黄钻等级 */
	@JsonProperty("yellow_vip_level")
	private String yellowVipLevel;
	
	/** QQ会员等级 */
	private String level;
	
	/** 是否年费黄钻 */
	@JsonProperty("is_yellow_year_vip")
	private String isYellowYearVip;
	
}
