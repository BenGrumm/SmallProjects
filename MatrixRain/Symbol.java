import java.util.*;
import java.awt.*; 
import javax.swing.*;

public class Symbol{

	private int FONT_SIZE;
	private int row = 0;
	private int column = 0;
	private int switchInterval = 0;
	private String value = "";
	//Create new label object with randomly generated char
	private JLabel label = new JLabel(""+((char)(0x30A0 + (Math.random() * 96))));
	private JPanel panels;

	//Initialise symbol
	Symbol(int fontSize){
		FONT_SIZE = fontSize;
	}

	public JLabel labelSetUp(JPanel panel){
		panels = panel;

		//Set the text to Green
		label.setForeground(Color.GREEN);
		//Add the label to the panel
		panels.add(label);
		//Set font to be FONT_SIZE
		label.setFont(new Font("monospaced", Font.PLAIN, FONT_SIZE));

		//Set the labels position
		label.setBounds(column, row, FONT_SIZE, FONT_SIZE);
		return label;
	}

	public void setPosition(int symbolColumn, int position, int yPos){
		//Set the row and column positions
		row = (yPos*(FONT_SIZE / 2)) - (position * FONT_SIZE);
		column = symbolColumn*FONT_SIZE;
	}

	public void setRandomSymbol(){
		//If switchInterval is < 5 set new char in label set the text 5% chance
		if(switchInterval < 5){
			value = "" + (char)(0x30A0 + (Math.random() * 96));
			label.setText(value);
		}
		//Generate new random number between 0 and 100
		switchInterval = (int)(Math.random() * 100);
	}

	public void rain(JLabel label, int resetPos, int speed){
		//If the label leaves the bottom of the panel set is position to resetPos else add speed to its position
		row = row > panels.getHeight() ? resetPos : (row + speed);
		//Set the position
		label.setBounds(column, row, FONT_SIZE, FONT_SIZE);
		//Set new Symbol
		setRandomSymbol();
	}

	public void setJLabel(JLabel inputLabel){
		label = inputLabel;
	}

	public int getRow(){
		return row;
	}

	public int getColumn(){
		return column;
	}

	public JLabel getJLabel(){
		return label;
	}

}
