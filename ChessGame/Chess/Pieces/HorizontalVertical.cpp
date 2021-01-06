#include "HorizontalVertical.h"
#include "../Board.h"

inline bool HorizontalVertical::isValid(int newX, int newY, const Board* board) const{

	// If is not on a square horizontal or vertical from current
	if(newX != xPosition && newY != yPosition){
		return false;
	}

	// find direction of movement
	int xDir = ((newX - xPosition) == 0) ? 0 : ((newX - xPosition) / std::abs(newX - xPosition));
	int yDir = ((newY - yPosition) == 0) ? 0 : ((newY - yPosition) / std::abs(newY - yPosition));

	// find number of squares moved
	int numberOfPointsMoved = (newX - xPosition) == 0 ? std::abs(newY - yPosition) : std::abs(newX - xPosition);

	// check if squares between current and destination are occupied
	for(int i = 1; i < numberOfPointsMoved; i++){
		if(board->getPosition((xPosition + xDir * i), (yPosition + yDir * i)) != NULL){
			return false;
		}	
	}

	const ChessPiece* newPosition = board->getPosition(newX, newY);

	// if the position moving too is not null & the piece on position type is same as this false
	if(newPosition != NULL && newPosition->getPieceColour() == colourType){
		return false;
	}


	return true;
}