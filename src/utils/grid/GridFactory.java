package utils.grid;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;

public class GridFactory {
	public static Grid s_createGridFromImage(BufferedImage image, int cellSize) {
		Grid grid = new Grid(image.getWidth(), image.getHeight(), cellSize);

		int[] pixels = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				int rgb = pixels[(y * image.getWidth() + x)];
				Color color = new Color(rgb);
				Cell cell = grid.getCell(x, y);
				cell.setSelectedColor(color);
				cell.setSelected(true);
			}
		}
		return grid;
	}
	
	public static void main(String[] args) throws IOException, URISyntaxException {
		File file = new File(GridFactory.class.getResource("/images/tank.jpg").toURI());
		BufferedImage image = ImageIO.read(file);
		Grid grid = s_createGridFromImage(image, 50);
		GridSelector selector = new GridSelector(0, 0, 0);
		selector.setGrid(grid);
		selector.select(1024, 1024);
	}
}
