package engine.graphics.lwjgl;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL33;

public class LWJGLTextureLoader {
    private static final int BYTES_PER_PIXEL = 4;//3 for RGB, 4 for RGBA
    public static void loadTexture(BufferedImage image, LWJGLTexture texture){

       int[] pixels = new int[image.getWidth() * image.getHeight()];
         image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

         ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL); //4 for RGBA, 3 for RGB

         for(int y = 0; y < image.getHeight(); y++){
             for(int x = 0; x < image.getWidth(); x++){
                 int pixel = pixels[y * image.getWidth() + x];
                 buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                 buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                 buffer.put((byte) (pixel & 0xFF));               // Blue component
                 buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
             }
         }

         buffer.flip(); //DO NOT FORGET THIS

         // You now have a ByteBuffer filled with the color data of each pixel.
         // Now just create a texture ID and bind it. Then you can load it using 
         // whatever OpenGL method you want, for example:

         texture.bind();

         //Setup wrap mode
         GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
         GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

         //Setup texture scaling filtering
         GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
         GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

         //Send texel data to OpenGL
         GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

         //Return the texture ID so we can bind it later again
         texture.unbind();
    }
    public static BufferedImage getBuffered(Image image) {
    	BufferedImage buffered = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
    	buffered.getGraphics().drawImage(image, 0, 0 , null);
    	buffered.getGraphics().dispose();
    	return buffered;
    }
}
