package utils.image;

import java.awt.image.BufferedImage;

public class Texture {
	private int m_imageID;
	private double m_width;
	private double m_height;

	public Texture(int imageID, double width, double height) {
		m_imageID = imageID;
		m_width = width;
		m_height = height;
	}

	public Texture(Texture texture) {
		m_imageID = texture.getImageID();
		m_width = texture.getWidth();
		m_height = texture.getHeight();
	}

	public BufferedImage getImage() {
		return ImageUtils.getImage(m_imageID);
	}

	public void setImageID(int imageID) {
		m_imageID = imageID;
	}

	public int getImageID() {
		return m_imageID;
	}

	public void setWidth(double width) {
		m_width = width;
	}

	public double getWidth() {
		return m_width;
	}

	public void setHeight(double height) {
		m_height = height;
	}

	public double getHeight() {
		return m_height;
	}
}
