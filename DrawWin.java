import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class DrawWin extends JFrame {
	
	private JButton btnRecognize;
	
	/**
	 * Creates a window that contains a DrawPanel
	 * and a button to interpret the drawing.
	 */
	public DrawWin() {
		setTitle("Handwritten Digit Recognition");
		setSize(new Dimension(300, 430));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(null);
		
		Shared.drawPanel = new DrawPanel();
		JLabel lblWarning = new JLabel("Don't draw too fast.");
		lblWarning.setBounds(85, 290, 150, 50);
		btnRecognize = new JButton("Recognize");
		btnRecognize.setBounds(100, 340, 100, 50);
		btnRecognize.setFocusPainted(false);
		btnRecognize.addActionListener(new RecognizeListener());
		
		getContentPane().add(Shared.drawPanel);
		getContentPane().add(btnRecognize);
		getContentPane().add(lblWarning);
		setVisible(true);
	}
	
	/**
	 * Called when the user clicks the Recognize button.
	 * Shows the RecognizeWin.
	 */
	private class RecognizeListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dispose();
			Shared.recognitionWin.loadImage();
			Shared.recognitionWin.setVisible(true);
			Shared.recognitionWin.recognize();
		}
	}
}
