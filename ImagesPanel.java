
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ImagesPanel extends JPanel {
	
	public ImagesPanel() {
		setPreferredSize(new Dimension(710, 430));
		setBounds(0, 0, 710, 430);
	}
	
	public void paint(Graphics g) {
		BufferedImage image = Shared.recognitionWin.getImage();
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
