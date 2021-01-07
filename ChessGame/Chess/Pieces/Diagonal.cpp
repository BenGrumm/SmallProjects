#include <memory>

#include "Diagonal.h"
#include "../Board.h"

inline bool Diagonal::isValid(int newX, int newY, const Board* board) const{

	// If not on the diagonal return false
	if(std::abs(newX - xPosition) != std::abs(newY - yPosition)){
		return false;
	}

	int xDir = (newX - xPosition) / std::abs(newX - xPosition);
	int yDir = (newY - yPosition) / std::abs(newY - yPosition);

	// If a spot between current and final spot is not empty return false
	for(int i = 1; i < std::abs(newX - xPosition); i++){
		if(board->getPosition((xPosition + (xDir * i)), (yPosition + (yDir * i))) != NULL){
			return false;
		}
	}

	const std::shared_ptr<ChessPiece> newPosition = board->getPosition(newX, newY);

	// if the position moving too is not null & the piece on position type is same as this false
	if(newPosition != NULL && newPosition->getPieceColour() == colourType){
		return false;
	}

	return true;
}