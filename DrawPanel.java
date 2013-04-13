import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DrawPanel extends JPanel implements MouseMotionListener, MouseListener{

	private boolean p;
	private boolean painting;
	private int px, py;

	public DrawPanel() {
		setPreferredSize(new Dimension(280, 280));
		setBounds(10, 10, 280, 280);
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		p = false;
		painting = false;
		px = 0; py = 0;
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public void mousePressed(MouseEvent e) {
		p = true;
		painting = true;
	}

	public void mouseDragged(MouseEvent e) {
		int x = e.getX(), y = e.getY();
		Graphics graphics = getGraphics();
		graphics.setColor(Color.BLACK);
		if (painting && p) {
			graphics.drawLine(x, y, x, y);
			p = false;
		} else if (painting)
			graphics.drawLine(px,py,x,y);	
		px = x;
		py = y;
	}
	
	public void mouseExited(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}

}
