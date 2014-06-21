package edu.calstatela.cs202.qiao.homework4;

import java.awt.Color;

public class Balloon {
	
	private int radius, speed, x_pos, y_pos;
	private boolean status;
	private Color color;
		
	public Balloon(int minSize, int maxSize, int minSpeed, 
			int maxSpeed, int x_pos, int y_pos, String color){
		
		radius = minSize + (int)( Math.random() * (maxSize - minSize) ) + 9;
		speed = minSpeed + (int)( Math.random() * (maxSpeed - minSpeed) );
		this.x_pos = x_pos;
		this.y_pos = y_pos;
		
		if (color.equals("red")){
			this.color = Color.RED;
		}else if (color.equals("white")){
			this.color = Color.WHITE;
		}else if (color.equals("blue")){
			this.color = Color.BLUE;
		}else {
			int r = (int)(Math.random() * 255);
			int g = (int)(Math.random() * 255);
			int b = (int)(Math.random() * 255);
			this.color = new Color( r, g, b);
		}
		status = true;
	}
	
	public int getRadius(){
		return radius;
	}

	public int getX(){
		return x_pos;
	}
	
	public void setY(){
		y_pos -= speed;
	}
	public int getY(){
		return y_pos;
	}
	
	public Color getColor(){
		return color;
	}
	
	public void setStatus(int mouse_x, int mouse_y){
				
		if (Math.sqrt(Math.pow(x_pos + radius - mouse_x, 2) + Math.pow(y_pos + radius - mouse_y, 2)) < radius)
			status =  false;
	}
	
	public boolean getStatus(){		
		return status;
	}
}
