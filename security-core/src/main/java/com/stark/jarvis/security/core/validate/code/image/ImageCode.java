package com.stark.jarvis.security.core.validate.code.image;

import java.awt.image.BufferedImage;

import com.stark.jarvis.security.core.validate.code.ValidateCode;

/**
 * 图形验证码。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public class ImageCode extends ValidateCode {

	private static final long serialVersionUID = -5054781634500723571L;
	
	private BufferedImage image;

	public ImageCode(BufferedImage image, String code, int expiresIn) {
		super(code, expiresIn);
		this.image = image;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

}
