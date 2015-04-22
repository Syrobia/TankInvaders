//Created by Ammad Hashmi [Syrobia] www.syrobia.github.io
//package com.github.syrobia.tankinvaders.alienentity;

//alien entity
public class AlienEntity extends Entity {
	//horizontal speed
	private double moveSpeed = 75;
	//entity in this game
	private Game game;
	
	//create entity
   	public AlienEntity(Game game,String ref,int x,int y) {
		super(ref,x,y);
		
		this.game = game;
		dx = -moveSpeed;
	}

	//move with game logic
	public void move(long delta) {
		//when on left side, use game logic to switch movement
		if ((dx < 0) && (x < 10)) {
			game.updateLogic();
		}
		//other way around
		if ((dx > 0) && (x > 750)) {
			game.updateLogic();
		}
		
		//normal movement

		super.move(delta);
	}
	
	//game logic
	public void doLogic() {
		//swap movement and down a row
		dx = -dx;
		y += 10;
		
      //if reach bottom then player death
		if (y > 570) {
			game.notifyDeath();
		}
	}
	
	//collision
	public void collidedWith(Entity other) {
		// collisions with aliens are handled elsewhere

	}
}
