package SnakeEatsBeans;

import javax.swing.JFrame;

public class Msnake {

	public static void main(String[] args) {
		JFrame jf = new JFrame();
		jf.setTitle("Ã∞≥‘…ﬂ");
		jf.setSize(900, 720);
		jf.setLocationRelativeTo(null);
		jf.setResizable(false);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		jf.add(new MPanel());
		
		jf.setVisible(true);
	}
}