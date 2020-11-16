import java.util.*;
import java.awt.*; 
import javax.swing.*;

public class Stream{

	private int FONT_SIZE;
	private int column;
	//Set total symbols to be random number between 3 and 10
	private int totalSymbols = (int) Math.round(3 + (Math.random() * 10));
	//Set speed to be random number between 1 and 3
	private int speed = (int)(1 + Math.random() * 3);
	//Set spawn position to be random number between -20  and -120
	private int spawnPos = (-1 * 20) - ((int)(Math.random() * 100));
	//Set position to random number between 0 and 25
	private int positioning = ((int)Math.round(Math.random() * 25));
	//Initialise symbolArray and labels to arrays with size totalSymbols
	private Symbol[] symbolArray = new Symbol[totalSymbols];
	private JLabel[] labels = new JLabel[totalSymbols];

	//Initialise class
	Stream(int fontSize, int streamColumn){
		FONT_SIZE = fontSize;
		column = streamColumn;
	}

	public void generateSymbols(JPanel panel){
		//Loop through 0 to totalSymbols
		for(int i = 0; i < totalSymbols; i++){
			//Create new symbol object passsing Font_Size 
			Symbol singleSymbol = new Symbol(FONT_SIZE);
			//Set position to column and i for position in stream then position for it to spawn
			singleSymbol.setPosition(column, i, positioning);
			//Add object to array
			symbolArray[i] = singleSymbol;
			//Call labelSetUp and set return to labels
			labels[i] = symbolArray[i].labelSetUp(panel);
		}
	}

	public void render(){
		//Loop through 0 to totalSymbols
		for(int i = 0; i < totalSymbols; i++){
			//Call Rain on symbol passing label spawnPos and speed
			symbolArray[i].rain(labels[i], spawnPos, speed);
			//If last symbol in symbol array is equal to 1 generate new spawnPos
			if(symbolArray[totalSymbols-1].getRow() == 1){
				spawnPos = (-1 * FONT_SIZE)	 - ((int)(Math.random() * 100));
			}
		}
	}

}