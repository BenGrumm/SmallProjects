import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.PointerInfo;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

public class main{

	private JFrame frame;
	private levelFrame paintLevel;
	private DrawPanel drawPanel;
	private player mainPlayer;
	private platforms allPlatforms;
	private int blockSize = 10;
	private boolean isRunning = true;
	private int currentFPS = 0;
	private int floor;

	public static void main(String[] args){
		new main();
	}

	public main(){
		//Set paint level to new levelFrame object
		paintLevel = new levelFrame(blockSize);
		//Set allPlatforms to new platforms object geting the 2D array from paintLevel and passing that
		allPlatforms = new platforms(paintLevel.getMap(), blockSize);
		//Set mainPlayer to new player passing wall positions and spawn point
		mainPlayer = new player(blockSize, (allPlatforms.getLeftWall() * blockSize), (allPlatforms.getRightWall() * blockSize), paintLevel.getSpawnX(), paintLevel.getSpawnY(), paintLevel.getFinishX(), paintLevel.getFinishY());
		//Make the jframe
		makeJFrame();
		//Add the key listener
		mainPlayer.addKeyListener(frame);
		//Get the floor level
		floor = paintLevel.getFloor();
		//Call start game function
		startGame();
	}

	/*
	*
	*
	*/
	public void startGame(){
		int tempFPS = 0;
		//Create infinite loop
		while(isRunning){
			//Start the fps counter (all functions and vars are static no need to create an object)
			FPS.StartCounter();
			//Call move player on mainPlayer object
			mainPlayer.moverPlayer();
			//Repaint the frame
			frame.repaint();
			//Check if the player is on a platform passing a 2D array of platform coordinates and the level of the floor
			mainPlayer.playerOnPlatform(allPlatforms.getPlatforms(), floor);
			//Check if player made it to end point
			if(mainPlayer.playerAtWin()){
				//Do something when win
			}
			//Sleep game for 5 miliseconds
			try{
				Thread.sleep(5);
			}catch(Exception e){
				System.out.println(e);
			}
			//Tell FPS counter frame has been completed and to update frame count accordingly
			tempFPS = FPS.StopCountPrintFPS();
			if(tempFPS != 0){
				currentFPS = tempFPS;
			}
		}
	}

	public void makeJFrame(){
		//set frame to new JFrame object setting title in process
		frame = new JFrame("Test BG");
		//set program to exit on close
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//set drawpanel to new drawPanel object
		drawPanel = new DrawPanel();
		//set the prefered size to be height and width of level loaded
		drawPanel.setPreferredSize(new Dimension(paintLevel.getWindowWidth() , paintLevel.getWindowHeight()));

		//add draw panel to jframe
		frame.getContentPane().add(drawPanel);

		//pack everything so JFrame fits jpanel
		frame.pack();

		//set visible
		frame.setVisible(true);
		//set resizable to false
		frame.setResizable(false);
		//set location of JFrame
		frame.setLocation(375, 55);
		//
		frame.repaint();
	}

	class DrawPanel extends JPanel{
		public void paintComponent(Graphics g){
			//Super the paintComponent method
			super.paintComponent(g);
			//Call paint level on level
			paintLevel.paintLevel(g);
			//Call draw player on player object
			mainPlayer.drawPlayer(g);
			//Draw the fps
			g.drawString(Integer.toString(currentFPS), 0, (floor+blockSize));
		}
	}

}