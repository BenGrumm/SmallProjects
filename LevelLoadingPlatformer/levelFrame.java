import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

public class levelFrame{

	private JFrame frame;
	private List<List<Character>> levelList;
	private int windowWidth;
	private int windowHeight;
	private int blockSize;
	private int spawnX;
	private int spawnY;
	private int floor = 0;
	private int halfBlock;
	private int finishX;
	private int finishY;	
	private boolean floorSet = false;

	public levelFrame(int theBlockSize){
		blockSize = theBlockSize;
		halfBlock = (int) blockSize / 2;
		loadFileToLevel mainClass = new loadFileToLevel();
		levelList = mainClass.getArrayOfArray();
		//Set windows width to size of the first list multiplied by blocksize
		windowWidth = (levelList.get(0).size() * blockSize);
		//Set window height to be the array size multiplied by blocksize
		windowHeight = (levelList.size() * blockSize);
		startLoop();
	}

	public void startLoop(){
		int xPos = 0;
		int yPos = 0;
		int floorCount = 0;
		for(List<Character> nextArray : levelList){
			for(Character c : nextArray){
				//If the char is the spawn position set the position of it
				if(c == '~'){
					spawnX = xPos;
					spawnY = yPos;
				}
				//If the char is O set the end position
				if(c == 'O'){
					finishX = xPos;
					finishY = yPos;
				}
				//Look for the floor by seeing if the count matches the width
				if(c == '*' || c == '~'){
					floorCount++;
				}
				//Increase by block size
				xPos += blockSize;
			}
			if(floorCount == levelList.get(0).size() && !floorSet){
				floor = yPos;
				floorSet = true;
			}else{
				floorCount = 0;
			}
			//Set x to 0 increase y
			xPos = 0;
			yPos += blockSize;
		}
	}

	public void paintLevel(Graphics g){
		int xPos = 0;
		int yPos = 0;
		//Loop through every char
		for(List<Character> nextArray : levelList){
			for(Character c : nextArray){
				switch(c){
					//If the char is wall char set the color to black fill the block
					case '*':	g.setColor(Color.black);
								g.fillRect(xPos, yPos, blockSize, blockSize);
								// System.out.println(yPos);
								break;
					//If the char is a platform char set the color to red and fill the position
					case 'X':	g.setColor(Color.red);
								g.fillRect(xPos, yPos, blockSize, blockSize);
								break;
					//If the position if the spawn point set the color to pink and fill the square
					case '~':	g.setColor(Color.pink);
								g.fillRect(xPos, yPos, blockSize, blockSize);
								break;
					//If the char is O (finish point) set the color to be a checkered flag
					case 'O':	g.setColor(Color.black);
								g.fillRect(xPos, yPos, blockSize, blockSize);
								g.setColor(Color.white);
								g.fillRect((xPos + 1), (yPos + 1), (halfBlock - 1), (halfBlock - 1));
								g.fillRect((xPos + halfBlock), (yPos + halfBlock), (halfBlock-1), (halfBlock-1));
								break;
				}
				xPos += blockSize;
			}
			xPos = 0;
			yPos += blockSize;
		}
	}

	public List<List<Character>> getMap(){
		return levelList;
	}

	public int getSpawnX(){
		return spawnX;
	}

	public int getSpawnY(){
		return spawnY;
	}

	public int getFinishX(){
		return finishX;
	}

	public int getFinishY(){
		return finishY;
	}

	public int getFloor(){
		return floor;
	}

	public int getWindowHeight(){
		return windowHeight;
	}

	public int getWindowWidth(){
		return windowWidth;
	}

}