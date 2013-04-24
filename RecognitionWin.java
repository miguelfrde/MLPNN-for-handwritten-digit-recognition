import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class RecognitionWin extends JFrame {

	private JButton btnReset;
	private JLabel lblDigit;
	private BufferedImage image;
	private int[] rectCoords;
	private boolean[][] bits;

	/**
	 * Creates a window where the image drawn in DrawPanel
	 * is shown inside an ImagesPanel. Shows the interpreted
	 * digit that the user drew.
	 */
	public RecognitionWin() {
		setTitle("Handwritten Digit Recognition");
		setSize(710, 480);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(null);

		btnReset = new JButton("Reset");
		btnReset.setBounds(565, 400, 100, 25);
		btnReset.setFocusPainted(false);
		btnReset.addActionListener(new ResetButtonListener());
		lblDigit = new JLabel(new ImageIcon("res/loader.gif"));
		lblDigit.setFont(new Font("Verdana", Font.PLAIN, 100));
		lblDigit.setBounds(280, 340, 150, 100);
		getContentPane().add(btnReset);
		getContentPane().add(lblDigit);
		getContentPane().add(new ImagesPanel());
	}

	/**
	 * Gets the image data that was drawn in the DrawPanel.
	 */
	public void loadImage() {
		boolean[][] data = Shared.drawPanel.getData();
		image = new BufferedImage(280, 280, BufferedImage.TYPE_BYTE_BINARY);
		
		// Obtain the image and the rectangle that contains it
		rectCoords = new int[] {280, 280, 0, 0}; // { xmin, ymin, xmax, ymax }
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
		
		// Obtain the bits for the reduced 10x10 image
		bits = new boolean[10][10];
		int w = rectCoords[2] - rectCoords[0];
		int h = rectCoords[3] - rectCoords[1];
		int x = -1, y = -1, dx = w/10, dy = h/10;
		for (int i = rectCoords[0]; i <= rectCoords[2]; i++) {
			if (x != 9 && (i - rectCoords[0]) % dx == 0) x++;
			y = -1;
			for (int j = rectCoords[1]; j <= rectCoords[3]; j++) {
				if (y != 9 && (j - rectCoords[1]) % dy == 0) y++;
				if (bits[x][y]) continue;
				bits[x][y] = data[i][j] || bits[x][y];
			}
		}
	}

	/**
	 * Use it to obtain the coordinates of the renctangle that
	 * contains the drawing.
	 * @return Array of coordinates of the rectangle in the
	 *         format: { xmin, ymin, xmax, ymax }.
	 */
	public int[] getRectangleCoords() {
		return rectCoords;
	}

	/**
	 * Use it to obtain the bits of the reduced 10x10 image.
	 * @return Matrix of bits.
	 */
	public boolean[][] getImageBits() {
		return bits;
	}

	/**
	 * Use it to get the drawn image.
	 * @return Drawn image.
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * Called when the user clicks the "Reset button"
	 * Takes back to the DrawWin.
	 */
	private class ResetButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			dispose();
			Shared.drawWin = new DrawWin();
		}

	}

}
