package edu.calstatela.cs202.qiao.homework4;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


class DrawingCanvas extends JPanel{
	
	private static final long serialVersionUID = -6415978173041056287L;
	private Balloon[] balloons;
	private boolean isPlay;
	private int numBullet;
	private int timePass;
	private int balloonPicture_x, balloonPicture_y, crow_x;
	Font arialFont, conFont;
	BufferedImage backgroundImage, balloonImage, crowImage;
	
	public DrawingCanvas(int minSize, int maxSize, int minBalloon, int maxBalloon,  
			int minSpeed, int maxSpeed, boolean randomXP, boolean randomYP, String color){
		
		this.setPreferredSize(new Dimension(800,600));
		arialFont = new Font ("Arial", Font.BOLD, 20); 
		conFont = new Font ("Arial", Font.BOLD, 30);
		int numBallons = minBalloon + (int)(Math.random()*(maxBalloon - minBalloon));
		int x_pos, y_pos;
		
		balloons = new Balloon[numBallons];			
		for (int i = 0; i < numBallons; i++) {
			if (randomXP){
				x_pos = (int)(Math.random()*760);
			}else{
				x_pos = (int)((800/numBallons)*i);
			}
			if (randomYP){
				y_pos = 300 + (int)(Math.random()*250);
			}else{
				y_pos = 550;
			}
			
			balloons[i] = new Balloon(minSize, maxSize, minSpeed, maxSpeed, x_pos, y_pos, color);
		}		
		isPlay = false;
		numBullet = 0;
		timePass = 0;
		balloonPicture_x = 0;
		crow_x = 0;
		loadImage();
		this.addMouseListener(new MouseShootListener());
	}
	
	void update(){
		for (Balloon balloon : balloons) {
			balloon.setY();
		}		
		repaint();			
	}
	
	public void setPlayStatus(boolean play){
		isPlay = play;
	}
	
	public void saveImage() {
		BufferedImage bufferedImage = new BufferedImage(this.getSize().width,
				this.getSize().height, BufferedImage.TYPE_INT_ARGB);

		Graphics g = bufferedImage.createGraphics();
		
		this.paint(g);
		timePass--;
		g.dispose();

		try {
			ImageIO.write(bufferedImage, "png", new File("balloon.png"));
			JOptionPane.showMessageDialog(null, "Image saved to disk: balloon.png", 
					"Save Image", JOptionPane.INFORMATION_MESSAGE); 
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e, 
					"Error", JOptionPane.ERROR_MESSAGE); 
		}
	}
	
	public void loadImage() {
		try {
			backgroundImage = ImageIO.read(new File("pictures\\background.png"));
			balloonImage = ImageIO.read(new File("pictures\\balloon.png"));
			crowImage = ImageIO.read(new File("pictures\\crow.png"));
		} catch (IOException e) {
			System.err.println(e);
		} 	
	}	
	
	public boolean gameOver(){
		for (Balloon balloon : balloons){
			if (balloon.getStatus() && (balloon.getY() + 2 * balloon.getRadius() > 0)){
				return false;
			}
		}
		return true;
	}
	
	public int numShoot(){
		int numHit = 0;
		for (Balloon balloon : balloons){
			if (balloon.getStatus() == false){
				numHit += 1;
			}
		}
		return numHit;
	}
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(Color.RED);
		g.setFont(arialFont);
		g.drawImage(backgroundImage, 0, 0, null); 
		if (!gameOver()){
			g.drawString("Time pass: " + timePass, 600, 100);
			timePass++;
			for (Balloon balloon : balloons) {
				g.setColor(balloon.getColor());
				if (balloon.getStatus())
					g.fillOval(balloon.getX(), balloon.getY(), 2*balloon.getRadius(), 2*balloon.getRadius());
			}
		}else{	
			int numShoot = numShoot();
			int numBalloon = balloons.length;			
			for (int i = 0; i < 100; i++){				
				int x = (int)(Math.random() * 800);
				int y = (int)(Math.random() * 600);
				int red = (int)(Math.random() * 255);
				int green = (int)(Math.random() * 255);
				int blue = (int)(Math.random() * 255);
				g.setColor(new Color(red, green, blue));
				if (x < 200 || x > 600 || y < 70 || y > 250){
					g.fillOval(x, y, 10, 10);
				}
			}
			g.setColor(Color.RED);
			if (numShoot == numBalloon){
				g.drawImage(balloonImage, balloonPicture_x, balloonPicture_y, null); 
				balloonPicture_x += 15;
				balloonPicture_y -= 3;
				g.setFont(conFont);			
				g.drawString("Congratulations!", 290, 100);
				g.setFont(arialFont);
				g.drawString("You shoot all the "+numBalloon+" balloons. " +
						"Your score is " + (numShoot * 10) + ".", 200, 150);
			}else{
				g.drawImage(crowImage, crow_x, 10, null);
				crow_x += 15;
				g.drawString("You shoot " + numShoot + " balloons, missing " +
							 (numBalloon - numShoot) + ". Your score is " + (numShoot * 10) + ".", 200, 150);				
			}
			g.drawString("Time use: " + timePass + " seconds", 280, 200);
			g.drawString("Number of bullets use: " + numBullet, 270, 250);
		}
	}
	
	class MouseShootListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent e) {
			
			if(isPlay){
				numBullet += 1;
				for (Balloon balloon : balloons) {
					balloon.setStatus(e.getX(), e.getY());					
				}				
			}
		}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}
	}

}

