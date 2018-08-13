package com.stark.jarvis.security.core.validate.code.image;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import com.stark.jarvis.security.core.validate.code.AbstractValidateCodeProcessor;

/**
 * 图片验证码处理器。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Component
public class ImageCodeProcessor extends AbstractValidateCodeProcessor<ImageCode> {

	@Override
	protected void send(ServletWebRequest request, ImageCode imageCode) throws Exception {
		ImageIO.write(imageCode.getImage(), "JPEG", request.getResponse().getOutputStream());
	}

}
