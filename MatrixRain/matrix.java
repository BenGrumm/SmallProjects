import java.util.*;
import java.awt.*; 
import javax.swing.*;

public class matrix{

	//Declare Font size as 20
	private static final int FONT_SIZE = 20;
	//Create empty jpanel
	private static JPanel panel = new JPanel(null);
	private static JFrame frame;
	private static int panelWidth;
	//Initialise stream array
	private static Stream streamClasses[];//Symbol symbols[] = new Symbol[((int) panel.getWidth()/FONT_SIZE)];

	public static void main(String[] args){

		//Create new object matrix
		matrix main = new matrix();

		//Call setup function on object
		main.setup();

		//Create Infinite loop
		while(true){
			//Sleep for 30 miliseconds
			try{Thread.sleep(30);}catch(Exception exe){}
			//Loop through stream classes call render on the classes
			for(int i = 0; i < panelWidth; i++){
				streamClasses[i].render();
			}
		}

	}

	private void setup() {
		//Call jframeSetup
		jframeSetup();

		//Add the empty panel to the frame
		frame.add(panel);
		//Set the panel to hava a black background
		panel.setBackground(Color.BLACK);

		//Set the window to be visible
		frame.setVisible(true);

		//Get the panel width then divide by FontSize to get ammount of lines that can fit into window
		panelWidth = ((int) panel.getWidth()/FONT_SIZE);
		//Create the stream class array with the width of panel
		streamClasses = new Stream[panelWidth];

		//Call set up labels
		setUpLabels();
	}

	public void jframeSetup(){
		//Create new JFrame object
		frame = new JFrame();
		//Set the window size to 600, 400 px
		frame.setSize(600, 400);
		//Make it so window cannot be resized
		frame.setResizable(false);
		//?
		frame.setMinimumSize(new Dimension(300, 200));
		//Should appear at center of screen
		frame.setLocationRelativeTo(null);
		//Set the title of the window to "MRain"
		frame.setTitle("MRain");
		//Make it so close exits program
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void setUpLabels(){
		//Loop through panel width setting up Stream for each position
		for(int i = 0; i < panelWidth; i++){
			//Pass font size and position to streams
			Stream streams = new Stream(FONT_SIZE, i);
			//Generate symbols passing panel
			streams.generateSymbols(panel);
			//Set the posision in streamClasses array to current object
			streamClasses[i] = streams;
		}
	}

}