package SnakeEatsBeans;

import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import javax.swing.Timer;

import javax.sound.sampled.*;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.io.IOException;
import java.io.InputStream;

public class MPanel extends JPanel implements KeyListener, ActionListener {
	ImageIcon title;
	ImageIcon body;
	ImageIcon up;
	ImageIcon down;
	ImageIcon left;
	ImageIcon right;
	ImageIcon food;
	
	int len;
	int score;
	int[] snakex = new int[750];
	int[] snakey = new int[750];
	String fx = "R";
	boolean isStarted = false;
	boolean isFailed = false;
	Timer timer = new Timer(171, this);
	int foodx;
	int foody;
	Random rd = new Random();
	
	Clip bgm;
	
	public MPanel() {
		loadImages();
		
		initSnake();
		
		this.setFocusable(true);
		this.addKeyListener(this);
		
		timer.start();
		
		loadBGM();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setBackground(Color.WHITE);
		title.paintIcon(this, g, 25, 11);
		g.fillRect(25, 75, 850, 600);
		
		g.setColor(Color.WHITE);
		g.drawString("Len: " + len, 750, 35);
		g.drawString("Score: " + score, 750, 50);
		
		if(fx == "U") {
			up.paintIcon(this, g, snakex[0], snakey[0]);
		}else if(fx == "D") {
			down.paintIcon(this, g, snakex[0], snakey[0]);
		}else if(fx == "L") {
			left.paintIcon(this, g, snakex[0], snakey[0]);
		}else if(fx == "R") {
			right.paintIcon(this, g, snakex[0], snakey[0]);
		}
		for(int i=1; i<len; i++) {
			body.paintIcon(this, g, snakex[i], snakey[i]);
		}
		
		food.paintIcon(this, g, foodx, foody);
		
		if(isStarted == false) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("arial", Font.BOLD, 40));
			g.drawString("Press Space to Start", 260, 280);
		}
		
		if(isFailed) {
			g.setColor(Color.RED);
			g.setFont(new Font("arial", Font.BOLD, 40));
			g.drawString("Failed: Press Space to Restart", 170, 280);
		}
	}
	
	public void initSnake() {
		len = 3;
		snakex[0] = 100;
		snakey[0] = 100;
		snakex[1] = 75;
		snakey[1] = 100;
		snakex[2] = 50;
		snakey[2] = 100;
		
		foodx = 25 + 25*rd.nextInt(34);
		foody = 75 + 25*rd.nextInt(24);
		
		fx = "R";
		
		score = 0;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(isStarted && !isFailed) {
			for(int i=len-1; i>0; i--) {
				snakex[i] = snakex[i-1];
				snakey[i] = snakey[i-1];
			}
			
			if(fx == "R") {
				snakex[0] += 25;
				if(snakex[0] > 850) {
					snakex[0] = 25;
				}
			}else if(fx == "L") {
				snakex[0] -= 25;
				if(snakex[0] < 25) {
					snakex[0] = 850;
				}
			}else if(fx == "U") {
				snakey[0] -= 25;
				if(snakey[0] < 75) {
					snakey[0] = 650;
				}
			}else if(fx == "D") {
				snakey[0] += 25;
				if(snakey[0] > 650) {
					snakey[0] = 75;
				}
			}
			
			if(snakex[0] == foodx && snakey[0] == foody) {
				len++;
				score += 10;
				foodx = 25 + 25*rd.nextInt(34);
				foody = 75 + 25*rd.nextInt(24);
			}
			
			for(int i=1; i<len; i++) {
				if(snakex[i]==snakex[0] && snakey[i]==snakey[0]) {
					isFailed = true;
				}
			}
			
			repaint();
		}
		
		timer.start();
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyCode == KeyEvent.VK_SPACE) {
			if(isFailed) {
				isFailed = false;
				initSnake();
			}else {
				isStarted = !isStarted;
			}
			repaint();
			if(isStarted) {
				playBGM();
			}else {
				stopBGM();
			}
		}else if(keyCode == KeyEvent.VK_LEFT) {
			fx = "L";
		}else if(keyCode == KeyEvent.VK_RIGHT) {
			fx = "R";
		}else if(keyCode == KeyEvent.VK_UP) {
			fx = "U";
		}else if(keyCode == KeyEvent.VK_DOWN) {
			fx = "D";
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
	
	private void loadBGM() {
		try {
			bgm = AudioSystem.getClip();
			InputStream is = this.getClass().getClassLoader().getResourceAsStream("sound/bgm2.wav");
			AudioInputStream ais = AudioSystem.getAudioInputStream(is);
			bgm.open(ais);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void playBGM() {
		bgm.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	private void stopBGM() {
		bgm.stop();
	}
	
	private void loadImages() {
		InputStream is;
		try {
			is = getClass().getClassLoader().getResourceAsStream("imagess/title.jpg");
			title = new ImageIcon(ImageIO.read(is));
			
			is = getClass().getClassLoader().getResourceAsStream("imagess/body.png");
			body = new ImageIcon(ImageIO.read(is));
			
			is = getClass().getClassLoader().getResourceAsStream("imagess/up.png");
			up = new ImageIcon(ImageIO.read(is));
			
			is = getClass().getClassLoader().getResourceAsStream("imagess/down.png");
			down = new ImageIcon(ImageIO.read(is));
			
			is = getClass().getClassLoader().getResourceAsStream("imagess/left.png");
			left = new ImageIcon(ImageIO.read(is));
			
			is = getClass().getClassLoader().getResourceAsStream("imagess/right.png");
			right = new ImageIcon(ImageIO.read(is));
			
			is = getClass().getClassLoader().getResourceAsStream("imagess/food.png");
			food = new ImageIcon(ImageIO.read(is));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}