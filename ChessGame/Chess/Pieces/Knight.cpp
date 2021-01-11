#include "Knight.h"
#include "../Board.h"

bool Knight::isValid(int newX, int newY, const Board* board) const{

	const ChessPiece* newPos = board->getPosition(newX, newY);

	int absXDiff = std::abs(xPosition - newX);
	int absYDiff = std::abs(yPosition - newY);

	// if(absXDiff == 1 && absYDiff == 2 || absXDiff == 2 && absYDiff == 1){
	// Can move 1 square 1 direction 2 the other as 2 is prime can use multiplication
	if(absXDiff * absYDiff == 2){

		if(newPos == NULL || newPos->getPieceColour() == !colourType){
			return true;
		}

	}

	return false;

}