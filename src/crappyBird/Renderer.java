package crappyBird;

import java.awt.Graphics;

import javax.swing.JPanel;

public class Renderer extends JPanel{

	private static final long serialVersionUID = 3451094307592184873L;

	@Override
	protected void paintComponent(Graphics g) {
		
		// TODO Auto-generated method stub
		super.paintComponent(g);
		
		CrappyBird.crappyBird.repaint(g);
	}
	
}
 