#include <memory>

#include "../Coordinates.h"
#include "Diagonal.h"
#include "../Board.h"

inline bool Diagonal::isValid(const Coordinates& newPos, const Board* board) const{

	// If not on the diagonal return false
	if(std::abs(newPos.x - xPosition) != std::abs(newPos.y - yPosition)){
		return false;
	}

	int xDir = (newPos.x - xPosition) / std::abs(newPos.x - xPosition);
	int yDir = (newPos.y - yPosition) / std::abs(newPos.y - yPosition);

	// If a spot between current and final spot is not empty return false
	for(int i = 1; i < std::abs(newPos.x - xPosition); i++){
		if(board->getPosition({(xPosition + (xDir * i)), (yPosition + (yDir * i))}) != NULL){
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