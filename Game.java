
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Game extends Canvas {
	// Makes enemies move faster 
   private BufferStrategy strategy;
	// True if the game is currently "running"
   private boolean gameRunning = true;
	// The list of all the entities that exist 
   private ArrayList entities = new ArrayList();
	// The list of entities that need to be removed from the game
   private ArrayList removeList = new ArrayList();
	// The player
   private Entity ship;
	// Speed of player
   private double moveSpeed = 300;
	// Last time shot was fired
   private long lastFire = 0;
	// Interval between firing shots
   private long firingInterval = 500;
	// Number of Aliens left on the screen
   private int alienCount;
	
	// The message to display which waiting for a key press 
   private String message = "";
	// True if we're holding up game play until a key has been pressed 
   private boolean waitingForKeyPress = true;
	// True if the left cursor key is currently pressed 
   private boolean leftPressed = false;
	// True if the right cursor key is currently pressed
   private boolean rightPressed = false;
	// True if we are firing 
   private boolean firePressed = false;
	// True if game logic needs to be applied this loop, normally as a result of a game event 
   private boolean logicRequiredThisLoop = false;
	
	
   public Game() {
   	//create JFrame
   
      JFrame container = new JFrame("Tank Trouble Invasion");
   	
   	//create JPanel: set dimensions
   
      JPanel panel = (JPanel) container.getContentPane();
      panel.setPreferredSize(new Dimension(800,600));
      panel.setLayout(null);
   	
   	//canvas size
   
      setBounds(0,0,800,600);
      panel.add(this);
   	
      //no recreation of canvas
      setIgnoreRepaint(true);
   	
   	
      //make it visible
      container.pack();
      container.setResizable(false);
      container.setVisible(true);
   
      //Allows closing window
      container.addWindowListener(
            new WindowAdapter() {
               public void windowClosing(WindowEvent e) {
                  System.exit(0);
               }
            });
   	
   	//helps in reading keys and responding to it
   
      addKeyListener(new KeyInputHandler());
   	
   	//key events
      requestFocus();
   
   	// create the buffering strategy which will allow AWT
   
   	// to manage our accelerated graphics
   
      createBufferStrategy(2);
      strategy = getBufferStrategy();
   	
   	// initialise the entities in our game so there's something
   
   	// to see at startup
   
      initEntities();
   }
	
	//start new game
   private void startGame() {
   	// clear out any existing entities and intialise a new set
   
      entities.clear();
      initEntities();
   	
   	// blank out any keyboard settings we might currently have
   
      leftPressed = false;
      rightPressed = false;
      firePressed = false;
   }
	
	//create entities
   private void initEntities() {
   	// create the player ship and place it roughly in the center of the screen
   
      ship = new ShipEntity(this,"Player.png",370,550);
      entities.add(ship);
   	
   	// create a block of aliens (5 rows, by 12 aliens, spaced evenly)
   
      alienCount = 0;
      for (int row=0;row<5;row++) {
         for (int x=0;x<12;x++) {
            Entity alien = new AlienEntity(this,"enemy_idle_left.png",100+(x*50),(50)+row*30);
            entities.add(alien);
            alienCount++;
         }
      }
   }
	
	//Notification from a game entity that the logic of the game should be run at the next opportunity (normally as a result of some game event)
	 
   public void updateLogic() {
      logicRequiredThisLoop = true;
   }
	
	//removes entities (when hit)
   public void removeEntity(Entity entity) {
      removeList.add(entity);
   }
	
	//Player Death
   public void notifyDeath() {
      message = "Oh no! They got you, try again?";
      waitingForKeyPress = true;
   }
	
	//Player Win
   public void notifyWin() {
      message = "Well done! You Win!";
      waitingForKeyPress = true;
   }
	
	//Alien kill
   public void notifyAlienKilled() {
   	// reduce the alient count, if there are none left, the player has won!
   
      alienCount--;
   	
      if (alienCount == 0) {
         notifyWin();
      }
   	
   	// if there are still some aliens left then they all need to get faster, so
   
   	// speed up all the existing aliens
   
      for (int i=0;i<entities.size();i++) {
         Entity entity = (Entity) entities.get(i);
      	
         if (entity instanceof AlienEntity) {
         	// speed up by 2%
         
            entity.setHorizontalMovement(entity.getHorizontalMovement() * 1.02);
         }
      }
   }
	
	//Shooting
   public void tryToFire() {
   	//check that we have waiting long enough to fire
   
      if (System.currentTimeMillis() - lastFire < firingInterval) {
         return;
      }
   	
   	//if we waited long enough, create the shot entity, and record the time.
   
      lastFire = System.currentTimeMillis();
      ShotEntity shot = new ShotEntity(this,"bullet.png",ship.getX()+10,ship.getY()-30);
      entities.add(shot);
   }
	
	//main game loop
   public void gameLoop() {
      long lastLoopTime = System.currentTimeMillis();
   	
   	// keep looping round til the game ends
   
      while (gameRunning) {
      	// work out how long its been since the last update, this
      
      	// will be used to calculate how far the entities should
      
      	// move this loop
      
         long delta = System.currentTimeMillis() - lastLoopTime;
         lastLoopTime = System.currentTimeMillis();
      	
      	// Get hold of a graphics context for the accelerated 
      
      	// surface and blank it out
      
         Graphics2D g = (Graphics2D) strategy.getDrawGraphics(); 
         g.setColor(Color.blue);
         g.fillRect(0,0,800,600);
      	
      	// cycle round asking each entity to move itself
      
         if (!waitingForKeyPress) {
            for (int i=0;i<entities.size();i++) {
               Entity entity = (Entity) entities.get(i);
            	
               entity.move(delta);
            }
         }   
      	
      	// cycle round drawing all the entities we have in the game
      
         for (int i=0;i<entities.size();i++) {
            Entity entity = (Entity) entities.get(i);
         	
            entity.draw(g);
         }
      	
     //bullet and entities collotion
      
         for (int p=0;p<entities.size();p++) {
            for (int s=p+1;s<entities.size();s++) {
               Entity me = (Entity) entities.get(p);
               Entity him = (Entity) entities.get(s);
            	
               if (me.collidesWith(him)) {
                  me.collidedWith(him);
                  him.collidedWith(me);
               }
            }
         }
      	
      	// remove any entity that has been marked for clear up
      
         entities.removeAll(removeList);
         removeList.clear();
     //game logic
      
         if (logicRequiredThisLoop) {
            for (int i=0;i<entities.size();i++) {
               Entity entity = (Entity) entities.get(i);
               entity.doLogic();
            }
         	
            logicRequiredThisLoop = false;
         }
      	
      	// if we're waiting for an "any key" press then draw the current message      
         if (waitingForKeyPress) {
            g.setColor(Color.red);
            g.drawString(message,(800-g.getFontMetrics().stringWidth(message))/2,250);
            g.drawString("Press any letter. Arrow Keys + Spacebar(Shoot)",(800-g.getFontMetrics().stringWidth("Press any letter. Arrow Keys + Spacebar(Shoot)"))/2,300);
         }
      	
      	//clear graphics//buffer      
         g.dispose();
         strategy.show();
      	
      	//movement of player
      
         ship.setHorizontalMovement(0);
      	
         if ((leftPressed) && (!rightPressed)) {
            ship.setHorizontalMovement(-moveSpeed);
         } 
         else if ((rightPressed) && (!leftPressed)) {
            ship.setHorizontalMovement(moveSpeed);
         }
      	
         //shooting      
         if (firePressed) {
            tryToFire();
         }
      	
      //stops due to firing      
         try { Thread.sleep(10); } 
         catch (Exception e) {}
      }
   }
	
	//keyboard input
   private class KeyInputHandler extends KeyAdapter {
      //number times pressed
      private int pressCount = 1;
   	
   	//pressed but not released
      public void keyPressed(KeyEvent e) {
         if (waitingForKeyPress) {
            return;
         }
      	
      	
         if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftPressed = true;
         }
         if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightPressed = true;
         }
         if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            firePressed = true;
         }
      } 
   	
   	//pressed then released
      public void keyReleased(KeyEvent e) {
      	//waiting
         if (waitingForKeyPress) {
            return;
         }
      	
         if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftPressed = false;
         }
         if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightPressed = false;
         }
         if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            firePressed = false;
         }
      }
   
   	//typed
      public void keyTyped(KeyEvent e) {
      	//doesn't allow shooting/moving until press counter changes 
      
         if (waitingForKeyPress) {
            if (pressCount == 1) {
            //start game if pressed
               waitingForKeyPress = false;
               startGame();
               pressCount = 0;
            } 
            else {
               pressCount++;
            }
         }
      	
         //if hit escape, close game      
         if (e.getKeyChar() == 27) {
            System.exit(0);
         }
      }
   }
	
	///starting game
	 
   public static void main(String argv[]) {
      Game g =new Game();
   
   	//start main loop
      g.gameLoop();
   }
}
