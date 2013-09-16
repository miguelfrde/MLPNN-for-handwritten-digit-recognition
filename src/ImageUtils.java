

import java.awt.image.BufferedImage;

public class ImageUtils {
	
	public static BufferedImage getImage(boolean[][] data) {
		BufferedImage image = new BufferedImage(280, 280, BufferedImage.TYPE_BYTE_BINARY);
		for (int i = 0; i < data.length; i++)
			for (int j = 0; j < data[i].length; j++) {
				int color = (data[i][j])? -1 : -16777216;
				image.setRGB(i, j, color);
			}
		return image;
	}
	
	public static int[] getRectangle(boolean[][] data) {
		int[] rectCoords = new int[] {280, 280, 0, 0}; // { xmin, ymin, xmax, ymax }
		for (int i = 0; i < data.length; i++)
			for (int j = 0; j < data[i].length; j++)
				if (data[i][j]) {
					if (i < rectCoords[0]) rectCoords[0] = i;
					if (j < rectCoords[1]) rectCoords[1] = j;
					if (i > rectCoords[2]) rectCoords[2] = i;
					if (j > rectCoords[3]) rectCoords[3] = j;
				}
		return rectCoords;
	}
	
	public static int[] getSquare(boolean[][] data) {
		int[] rectCoords = new int[] {280, 280, 0, 0}; // { xmin, ymin, xmax, ymax }
		for (int i = 0; i < data.length; i++)
			for (int j = 0; j < data[i].length; j++)
				if (data[i][j]) {
					if (i < rectCoords[0]) rectCoords[0] = i;
					if (j < rectCoords[1]) rectCoords[1] = j;
					if (i > rectCoords[2]) rectCoords[2] = i;
					if (j > rectCoords[3]) rectCoords[3] = j;
				}
		
		int sqrlen = rectCoords[3] - rectCoords[2] - rectCoords[1] + rectCoords[0]; 
		if (sqrlen > 0) {
			rectCoords[2] += sqrlen / 2;
			rectCoords[0] -= sqrlen / 2;
		} else {
			rectCoords[3] -= sqrlen / 2;
			rectCoords[1] += sqrlen / 2;
		}
		
		return rectCoords;
	}

	public static boolean[][] getBits(int[] rectCoords, boolean[][] data) {
		boolean bits[][] = new boolean[10][10];
		int w = rectCoords[2] - rectCoords[0];
		int h = rectCoords[3] - rectCoords[1];
		int x = -1, y = -1, dx = w/10, dy = h/10;
		if (dx == 0) dx = 1;
		if (dy == 0) dy = 1;
		for (int i = rectCoords[0]; i <= rectCoords[2]; i++) {
			if (x != 9 && (i - rectCoords[0]) % dx == 0) x++;
			y = -1;
			for (int j = rectCoords[1]; j <= rectCoords[3]; j++) {
				if (y != 9 && (j - rectCoords[1]) % dy == 0) y++;
				if (bits[x][y]) continue;
				bits[x][y] = data[i][j] || bits[x][y];
			}
		}
		return bits;
	}
	
}
