package com.stark.jarvis.security.social.client.userinfo;

/**
 * 第三方登录表单存储器。
 * <p>扩展该接口，可实现自己的持久化逻辑。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public interface UserConnectionRepository {
	
	/**
	 * 保存表单。
	 * @param form 第三方登录表单。
	 * @return 保存后的表单。
	 */
	UserConnectionForm saveForm(UserConnectionForm form);

}
