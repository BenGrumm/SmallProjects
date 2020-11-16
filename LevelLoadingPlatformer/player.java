import java.awt.PointerInfo;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.Graphics;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JFrame;

public class player{

	private int playerXDirection = 0;
	private int playerYDirection = 0;
	private int x = 100;
	private int y = 100;
	private int speed = 1;
	private int playerSize;
	private int countJump = 0;
	private int leftWall;
	private int rightWall;
	private int blockSize;
	private int finishX;
	private int finishY;
	private int jumpHeight = 50;
	private boolean jump = false;

	public player(int blockSize, int leftWall, int rightWall, int spawnX, int spawnY, int finishX, int finishY){
		//set player size to be half of blockSize
		playerSize = (int) blockSize/2;
		//Set vars to arguments
		this.blockSize = blockSize;
		this.leftWall = leftWall;
		//Set right wall to right wall + playerSize
		this.rightWall = rightWall + playerSize;
		//Set the coordinates for the finish block
		this.finishX = finishX;
		this.finishY = finishY;
		//Set x to spawn position plus half player size to be in middle of block
		x = spawnX + ((int) playerSize / 2);
		//Set y to spawn position - player size
		y = spawnY - playerSize;
	}

	public void addKeyListener(JFrame frame){
		//add a key listener to frame with a new playerMovement object for controls
		frame.addKeyListener(new playerMovement());
	}

	public void moverPlayer(){
		//if x is between the walls add the direction
		if(x > leftWall && x < rightWall){
			x+=playerXDirection;
		}
		//else if x is at right wall check to see if player direction is going left then add playerDirection
		else if(x == rightWall && playerXDirection == -1){
			x+=playerXDirection;
		}
		//else if x is at left wall check to see if player direction is going right then add playerDirection
		else if(x == leftWall && playerXDirection == 1){
			x+=playerXDirection;
		}
		//add playerYDirection to y
		y+=playerYDirection;
	}

	class playerMovement implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			//If UP Arrow is pressed set direction to speed
			if(e.getKeyCode() == KeyEvent.VK_RIGHT){playerXDirection = speed;}
			//If Down Arrow is pressed set direction to negative speed
			if(e.getKeyCode() == KeyEvent.VK_LEFT){playerXDirection = -speed;}
			//If Space button is pressed set jump to true
			if(e.getKeyCode() == KeyEvent.VK_SPACE){
				jump = true;
			}
		}

		//Set the direction to 0 after the release of the key.
		@Override
		public void keyReleased(KeyEvent e) {
			playerXDirection = 0;
			jump = false;
		}

		//Need For Compilation without errors
		@Override
		public void keyTyped(KeyEvent e){}
	}

	public void playerOnPlatform(List<List<Integer>> platformCoords, int floor){
		//If jump is set to true and the player is on a platform or player has not been jump for longer than jumpHeight loops
		if(jump && ((countJump > 0 && countJump < jumpHeight) || playerYDirection == 0)){
			//Add -1 which is going towards the top of the screen
			playerYDirection = -1;
			//Add 1 to the countJump
			countJump++;
			//return from function
			return;
		}else{
			//set the count to 0
			countJump = 0;
		}
		//Loop through the second dimension of the array
		for(List<Integer> secondDimension : platformCoords){
			//if player is between on the platform and on at the y position of the platform or is on the floor of the level
			if(((x+playerSize) >= secondDimension.get(0) && x <= secondDimension.get(1) && (y + playerSize) == secondDimension.get(2)) || (y + playerSize) == floor){
				//Set the y direction to 0
				playerYDirection = 0;
				//return from function
				return;
			}
		}
		//set the y direction to 1
		playerYDirection = 1;
	}

	public boolean playerAtWin(){
		if(x > finishX && x < (finishX + blockSize) && y == (finishY - playerSize)){
			return true;
		}else{
			return false;
		}
	}

	//paint the player
	public void drawPlayer(Graphics g){
		// System.out.println(String.format("X = %d Y = %d PlayerSize = %d", x, y, playerSize));
		g.setColor(Color.orange);
		g.fillRect(x, y, playerSize, playerSize);
	}

	//get player x direction
	public int getPlayerXDirection(){
		return playerXDirection;
	}

	//get player x position
	public int getX(){
		return x;
	}

	//get player y position
	public int getY(){
		return y;
	}

}