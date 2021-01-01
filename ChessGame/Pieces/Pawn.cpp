#include "Pawn.h"
#include "../Board.h"

bool Pawn::isValid(int newX, int newY, ChessPiece*** board) const{

	if(!Board::withinBoard(newX, newY)){
		return false;
	}

	ChessPiece* newPosition = board[newY][newX];

	// If move is 1 forward with no piece in position
	if(newX == xPosition && newY == (yPosition + direction) && newPosition == NULL){
		return true;
	}
	// If move is 1 forward and 1 to left / right and there is an piece of the oppsite colour
	else if((newX == xPosition + 1 || newX == xPosition - 1) && newY == (yPosition + direction) && newPosition != NULL && newPosition->getPieceColour() == !colourType){
		return true;
	}
	// If move is 2 forward and is pawn 1st move and nothing in mid position
	else if(newX == xPosition && newY == yPosition + (2 * direction) && newPosition == NULL && movedOnMove == -1 && board[newY - 1][newX] == NULL){
		return true;
	}

	// TODO include En passant takes

	return false;
}