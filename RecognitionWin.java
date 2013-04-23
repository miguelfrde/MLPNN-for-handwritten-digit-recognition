import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;


@SuppressWarnings("serial")
public class RecognitionWin extends JFrame {

	private ImagesPanel imagesPanel;
	private JButton btnReset;
	private BufferedImage image;
	private int[] rectCoords;
	private boolean[][] bits;

	public RecognitionWin() {
		setTitle("Handwritten Digit Recognition");
		setSize(710, 480);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(null);

		ImagesPanel imagesPanel = new ImagesPanel();
		btnReset = new JButton("Reset");
		btnReset.setBounds(565, 400, 100, 25);
		btnReset.setFocusPainted(false);
		btnReset.addActionListener(new ResetButtonListener());
		getContentPane().add(btnReset);
		getContentPane().add(imagesPanel);
	}

	public void loadImage() {
		boolean[][] data = Shared.drawPanel.getData();
		image = new BufferedImage(280, 280, BufferedImage.TYPE_BYTE_BINARY);
		rectCoords = new int[] {280, 280, 0, 0}; // xmin - ymin - xmax - ymax
		for (int i = 0; i < data.length; i++)
			for (int j = 0; j < data[i].length; j++) {
				int color = (data[i][j])? -1 : -16777216;
				if (data[i][j]) {
					if (i < rectCoords[0]) rectCoords[0] = i;
					if (j < rectCoords[1]) rectCoords[1] = j;
					if (i > rectCoords[2]) rectCoords[2] = i;
					if (j > rectCoords[3]) rectCoords[3] = j;
				}
				image.setRGB(i, j, color);
			}
		bits = new boolean[10][10];
		int w = rectCoords[2] - rectCoords[0];
		int h = rectCoords[3] - rectCoords[1];
		// CREATE BITS ARRAY
		int x = -1, y = -1, dx = w/10, dy = h/10;
		for (int i = rectCoords[0]; i <= rectCoords[2]; i++) {
			if ((i - rectCoords[0]) % dx == 0) x++;
			if (x == 10) x = 9;
			y = -1;
			for (int j = rectCoords[1]; j <= rectCoords[3]; j++) {
				if ((j - rectCoords[1]) % dy == 0) y++;
				if (y == 10) y = 9;
				if (bits[x][y]) continue;
				System.out.println(x + " " + y);
				bits[x][y] = data[i][j] || bits[x][y];
			}
		}
		for (boolean[] a : bits) {
			System.out.println(Arrays.toString(a));
		}
		if (imagesPanel != null) imagesPanel.repaint();
	}

	public int[] getRectangleCoords() {
		return rectCoords;
	}

	public boolean[][] getImageBits() {
		return bits;
	}

	public BufferedImage getImage() {
		return image;
	}

	private class ResetButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			dispose();
			Shared.drawWin = new DrawWin();
		}

	}

}
