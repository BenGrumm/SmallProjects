package com.bgrummitt;

import java.awt.*;

public class SudokuCanvas extends Canvas {

	private Board sudokuBoard;

	public SudokuCanvas(Board board){
		super();
		sudokuBoard = board;
	}

	private static final int lineThickness = 5;
	private static final int halfLineThickness = lineThickness / 2;
	private static final int quarterLineThickness = halfLineThickness / 2;

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		int boxWidth = (getWidth() - (lineThickness * 4 + (halfLineThickness) * 6)) / 9;

		paintLines((Graphics2D) g, boxWidth, getWidth());
		paintNumbers((Graphics2D) g, boxWidth);
	}

	private void paintLines(Graphics2D g2, int widthPerBox, int width){

		g2.setStroke(new BasicStroke(lineThickness));
		g2.setColor(Color.BLACK);

		// Borders
		int frequent = (width - halfLineThickness);
		g2.drawLine(halfLineThickness, halfLineThickness, frequent, halfLineThickness);
		g2.drawLine(halfLineThickness, halfLineThickness, halfLineThickness, frequent);
		g2.drawLine(halfLineThickness, frequent, frequent, frequent);
		g2.drawLine(frequent, halfLineThickness, frequent, frequent);

		// Thick Left To Right
		int yPos = widthPerBox * 3 + lineThickness * 2 + halfLineThickness;
		int xPos;
		g2.drawLine(halfLineThickness, yPos, frequent, yPos);
		yPos = yPos + 3 * widthPerBox + lineThickness * 2;
		g2.drawLine(halfLineThickness, yPos, frequent, yPos);

		// Thick Top To Bottom
		xPos = widthPerBox * 3 + lineThickness + 3 * halfLineThickness;
		g2.drawLine(xPos, halfLineThickness, xPos, frequent);
		xPos = xPos + widthPerBox * 3 + lineThickness * 2;
		g2.drawLine(xPos, halfLineThickness, xPos, frequent);

		g2.setStroke(new BasicStroke(halfLineThickness));

		// Thin Left To Right
		yPos = widthPerBox + lineThickness + quarterLineThickness;
		xPos = width - halfLineThickness;
		g2.drawLine(halfLineThickness, yPos, xPos, yPos);
		yPos = yPos + widthPerBox + halfLineThickness;
		g2.drawLine(halfLineThickness, yPos, xPos, yPos);
		yPos = yPos + 2 * widthPerBox + halfLineThickness + lineThickness;
		g2.drawLine(halfLineThickness, yPos, xPos, yPos);
		yPos = yPos + widthPerBox + halfLineThickness;
		g2.drawLine(halfLineThickness, yPos, xPos, yPos);
		yPos = yPos + 2 * widthPerBox + halfLineThickness + lineThickness;
		g2.drawLine(halfLineThickness, yPos, xPos, yPos);
		yPos = yPos + widthPerBox + halfLineThickness;
		g2.drawLine(halfLineThickness, yPos, xPos, yPos);

		// Thin Top To Bottom
		xPos = (widthPerBox + lineThickness + quarterLineThickness);
		yPos = width - halfLineThickness;
		g2.drawLine(xPos, halfLineThickness, xPos, yPos);
		xPos = xPos + widthPerBox + halfLineThickness;
		g2.drawLine(xPos, halfLineThickness, xPos, yPos);
		xPos = xPos + 2 * widthPerBox + halfLineThickness + lineThickness;
		g2.drawLine(xPos, halfLineThickness, xPos, yPos);
		xPos = xPos + widthPerBox + halfLineThickness;
		g2.drawLine(xPos, halfLineThickness, xPos, yPos);
		xPos = xPos + 2 * widthPerBox + halfLineThickness + lineThickness;
		g2.drawLine(xPos, halfLineThickness, xPos, yPos);
		xPos = xPos + widthPerBox + halfLineThickness;
		g2.drawLine(xPos, halfLineThickness, xPos, yPos);
	}

	public void paintNumbers(Graphics2D g2d, int boxWidth){
		for(int i = 0; i < 9; i++){
			for(int j = 0; j < 9; j++){
				g2d.drawString(Integer.toString(sudokuBoard.getCell(i, j)), (i * boxWidth) + boxWidth / 2 + ((int)Math.floor(i / 3) + 1) * lineThickness, (j * boxWidth) + boxWidth / 2);
			}
		}
	}

}
