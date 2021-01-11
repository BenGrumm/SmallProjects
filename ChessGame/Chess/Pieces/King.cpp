#include "King.h"

bool King::isValid(int newX, int newY, const Board* board) const{

	const ChessPiece* newPos = board->getPosition(newX, newY);

	int absXDiff = std::abs(xPosition - newX);
	int absYDiff = std::abs(yPosition - newY);

	if(absXDiff < 2 && absYDiff < 2){

		if(newPos == NULL || newPos->getPieceColour() == !colourType){
			return true;
		}

	}

	return false;

}