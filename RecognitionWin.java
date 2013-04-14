import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;


@SuppressWarnings("serial")
public class RecognitionWin extends JFrame {
	
	private BufferedImage image = null;
	
	public RecognitionWin() {
		setTitle("Handwritten Digit Recognition");
		setSize(710, 480);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(null);
		
		
	}
	
	public void loadImage() {
		boolean[][] data = Shared.drawPanel.getData();
		image = new BufferedImage(280, 280, BufferedImage.TYPE_BYTE_BINARY);
		for (int i = 0; i < data.length; i++)
			for (int j = 0; j < data[i].length; j++) {
				int color = (data[i][j])? -1 : -16777216;
				image.setRGB(i, j, color);
			}
		repaint();
	}
	
	public void paint(Graphics g) {
		if (image != null)
			g.drawImage(image, 50, 50, this);
		g.fillRect(379, 49, 281, 281);
		g.setColor(Color.WHITE);
		for (int i = 408; i < 660; i+= 28) {
			g.drawLine(i, 50, i, 330);
			g.drawLine(380, i - 330, 660, i - 330);
		}
		
	}
	
}
