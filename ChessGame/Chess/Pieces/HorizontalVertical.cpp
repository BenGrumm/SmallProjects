#include <memory>

#include "HorizontalVertical.h"
#include "../Board.h"
#include "../Coordinates.h"

inline bool HorizontalVertical::isValid(const Coordinates& newPos, const Board* board) const{

	// If is not on a square horizontal or vertical from current
	if(newPos.x != xPosition && newPos.y != yPosition){
		return false;
	}

	// find direction of movement
	int xDir = ((newPos.x - xPosition) == 0) ? 0 : ((newPos.x - xPosition) / std::abs(newPos.x - xPosition));
	int yDir = ((newPos.y - yPosition) == 0) ? 0 : ((newPos.y - yPosition) / std::abs(newPos.y - yPosition));

	// find number of squares moved
	int numberOfPointsMoved = (newPos.x - xPosition) == 0 ? std::abs(newPos.y - yPosition) : std::abs(newPos.x - xPosition);

	// check if squares between current and destination are occupied
	for(int i = 1; i < numberOfPointsMoved; i++){
		if(board->getPosition({(xPosition + xDir * i), (yPosition + yDir * i)}) != NULL){
			return false;
		}	
	}

	const ChessPiece* newPosition = board->getPosition(newPos);

	// if the position moving too is not null & the piece on position type is same as this false
	if(newPosition != NULL && newPosition->getPieceColour() == colourType){
		return false;
	}


	return true;
}