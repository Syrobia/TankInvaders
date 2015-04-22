//Created by Ammad Hashmi [Syrobia] www.syrobia.github.io
//package com.github.syrobia.tankinvaders.shipentity;

//player actor
public class ShipEntity extends Entity {
	//game of this entity
	private Game game;
	
	//create entity
	public ShipEntity(Game game,String ref,int x,int y) {
		super(ref,x,y);
		
		this.game = game;
	}
	
	//ship move with game logic
	public void move(long delta) {
		//if on edge of left side, don't proceed (stay within window)
		if ((dx < 0) && (x < 10)) {
			return;
		}
		//same for right side
		if ((dx > 0) && (x > 750)) {
			return;
		}
		//normal movement
		super.move(delta);
	}
	
	//collision
	public void collidedWith(Entity other) {
		//if with alien, then death
		if (other instanceof AlienEntity) {
			game.notifyDeath();
		}
	}
}
