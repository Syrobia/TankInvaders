//Created by Ammad Hashmi [Syrobia] www.syrobia.github.io
//package com.github.syrobia.tankinvaders.entity;

import java.awt.Graphics;
import java.awt.Rectangle;

//This entity will represent all actors
public abstract class Entity {
   //x location
	protected double x;
	//y location
	protected double y;
	//this.sprite stufff
	protected Sprite sprite;
	//speeed on x-axis(pixels/sec) 
	protected double dx;
	//speeed on y-axis(pixels/sec) */
	protected double dy;
	//square for coollecting and detecting collosions (player) 
	private Rectangle me = new Rectangle();
	// enemy square for collosions
	private Rectangle him = new Rectangle();
	
	//Construct a entity based on a sprite image and a location.
	
	public Entity(String ref,int x,int y) {
		this.sprite = SpriteStore.get().getSprite(ref);
		this.x = x;
		this.y = y;
	}
	
	//move alien entities
	public void move(long delta) {
		// update the location of the entity based on move speeds

		x += (delta * dx) / 1000;
		y += (delta * dy) / 1000;
	}
	
	//horizontal speed of alien entity
	public void setHorizontalMovement(double dx) {
		this.dx = dx;
	}

	//vertical speed
	public void setVerticalMovement(double dy) {
		this.dy = dy;
	}
	
	//return horizontal speed
	public double getHorizontalMovement() {
		return dx;
	}

	//return vertical speed
	public double getVerticalMovement() {
		return dy;
	}
	
	//draw entity
	public void draw(Graphics g) {
		sprite.draw(g,(int) x,(int) y);
	}
	
	//do crap
	public void doLogic() {
	}
	//x location
	public int getX() {
		return (int) x;
	}

   //y location
	public int getY() {
		return (int) y;
	}
	
   //collision checker
	public boolean collidesWith(Entity other) {
		me.setBounds((int) x,(int) y,sprite.getWidth(),sprite.getHeight());
		him.setBounds((int) other.x,(int) other.y,other.sprite.getWidth(),other.sprite.getHeight());

		return me.intersects(him);
	}
	
	//notify collision
	public abstract void collidedWith(Entity other);
}
