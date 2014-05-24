package engine.gui.border;

import engine.graphics.Renderer;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

import utils.image.ImageUtils;

public class ImageBorder implements Border {
	private BufferedImage m_cornerTL = null;
	private BufferedImage m_cornerTR = null;
	private BufferedImage m_cornerBL = null;
	private BufferedImage m_cornerBR = null;
	
	private BufferedImage m_top = null;
	private BufferedImage m_left = null;
	private BufferedImage m_right = null;
	private BufferedImage m_bottom = null;
	
	public ImageBorder() {}
	public ImageBorder(BufferedImage tl, BufferedImage tr, BufferedImage bl, BufferedImage br,
			BufferedImage top, BufferedImage left, BufferedImage right, BufferedImage bottom) {
		m_cornerTL = tl;
		m_cornerTR = tr;
		m_cornerBL = bl;
		m_cornerBR = br;
		
		m_top = top;
		m_left = left;
		m_right = right;
		m_bottom = bottom;
	}
	public void setLeft(BufferedImage left) { m_left = left; }
	public void setRight(BufferedImage right) { m_right = right; }
	public void setTop(BufferedImage top) { m_top = top; }
	public void setBottom(BufferedImage bottom) { m_bottom = bottom; }
	public void setCornerTL(BufferedImage tl) { m_cornerTL = tl; }
	public void setCornerTR(BufferedImage tr) { m_cornerTR = tr; }
	public void setCornerBL(BufferedImage bl) { m_cornerBL = bl; }
	public void setCornerBR(BufferedImage br) { m_cornerBR = br; }
	
	public BufferedImage getLeft() { return m_left; }
	public BufferedImage getRight() { return m_right; }
	public BufferedImage getTop() { return m_top; }
	public BufferedImage getBottom() { return m_bottom; }
	public BufferedImage getCornerTL() { return m_cornerTL; }
	public BufferedImage getCornerTR() { return m_cornerTR; }
	public BufferedImage getCornerBL() { return m_cornerBL; }
	public BufferedImage getCornerBR() { return m_cornerBR; }
	
	public int getTopBorder() {
		int size = 0;
		if (m_top != null) size = m_top.getHeight();
		if (m_cornerTR != null && m_cornerTR.getHeight() > size) size = m_cornerTR.getHeight();
		if (m_cornerTL != null && m_cornerTL.getHeight() > size) size = m_cornerTL.getHeight();
		return size;
	}
	public int getBottomBorder() {
		int size = 0;
		if (m_bottom != null) size = m_bottom.getHeight();
		if (m_cornerBR != null && m_cornerBR.getHeight() > size) size = m_cornerBR.getHeight();
		if (m_cornerBL != null && m_cornerBL.getHeight() > size) size = m_cornerBL.getHeight();
		return size;
	}
	public int getLeftBorder() {
		int size = 0;
		if (m_left != null) size = m_left.getWidth();
		if (m_cornerTL != null && m_cornerTL.getWidth() > size) size = m_cornerTL.getWidth();
		if (m_cornerBL != null && m_cornerBL.getWidth() > size) size = m_cornerBL.getWidth();
		return size;
	}

	public int getRightBorder() {
		int size = 0;
		if (m_right != null) size = m_right.getWidth();
		if (m_cornerTR != null && m_cornerTR.getWidth() > size) size = m_cornerTR.getWidth();
		if (m_cornerBR != null && m_cornerBR.getWidth() > size) size = m_cornerBR.getWidth();
		return size;
	}
	
	public void render(Renderer renderer, int x, int y, int w, int h) {
		renderer.pushTransform();
		renderer.translate(x, y);
		
		int tlw = 0, trw = 0, blw = 0, brw = 0;
		int tlh = 0, trh = 0, blh = 0, brh = 0;
		
		if (m_cornerTL != null) {
			tlw = m_cornerTL.getWidth();
			tlh = m_cornerTL.getHeight();
		}
		if (m_cornerTR != null) {
			trw = m_cornerTR.getWidth();
			trh = m_cornerTR.getHeight();
		}
		if (m_cornerBL != null) {
			blw = m_cornerBL.getWidth();
			blh = m_cornerBL.getHeight();
		}
		if (m_cornerBR != null) {
			brw = m_cornerBR.getWidth();
			brh = m_cornerBR.getHeight();
		}
		
		//Top
		if (m_top != null) renderer.drawImage(m_top, tlw, 0, w - (tlw + trw), m_top.getHeight());
		
		//TL Corner
		if (m_cornerTL != null) renderer.drawImage(m_cornerTL, 0, 0);
		
 		//Left
		if (m_left != null) renderer.drawImage(m_left, 0, tlh, m_left.getWidth(), h - (tlh + blh));

		//BL Corner
		if (m_cornerBL != null) renderer.drawImage(m_cornerBL, 0, h - m_cornerBL.getHeight());
		
		//Bottom
		if (m_bottom != null) renderer.drawImage(m_bottom, blw, h - m_bottom.getHeight(), 
								w - (blw + brw), m_bottom.getHeight());
		//BRCorner
		if (m_cornerBR != null) renderer.drawImage(m_cornerBR, w - m_cornerBR.getWidth(), h - m_cornerBR.getHeight());
		//Right
		if (m_right != null) renderer.drawImage(m_right, w - m_right.getWidth(), trh, 
								m_right.getWidth(), h - (trh + brh));
		//TR Corner
		if (m_cornerTR != null) renderer.drawImage(m_cornerTR, w - m_cornerTR.getWidth(), 0);
		renderer.popTransform();
	}
	
	public Object copy() {
		return new ImageBorder(ImageUtils.cloneImage(m_cornerTL), ImageUtils.cloneImage(m_cornerTR), 
					ImageUtils.cloneImage(m_cornerBL), ImageUtils.cloneImage(m_cornerBR), 
					ImageUtils.cloneImage(m_top), ImageUtils.cloneImage(m_left), 
					ImageUtils.cloneImage(m_right), ImageUtils.cloneImage(m_bottom));
	}
	public static BufferedImage deepCopy(BufferedImage bi) {
		 ColorModel cm = bi.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = bi.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);		
	}
}
