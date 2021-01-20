#include "Knight.h"
#include "../Board.h"
#include "../Coordinates.h"

bool Knight::isValid(const Coordinates& newPos, const Board* board) const{

	const ChessPiece* newPiece = board->getPosition(newPos);

	int absXDiff = std::abs(xPosition - newPos.x);
	int absYDiff = std::abs(yPosition - newPos.y);

	// if(absXDiff == 1 && absYDiff == 2 || absXDiff == 2 && absYDiff == 1){
	// Can move 1 square 1 direction 2 the other as 2 is prime can use multiplication
	if(absXDiff * absYDiff == 2){

		if(newPiece == NULL || newPiece->getPieceColour() == !colourType){
			return true;
		}

	}

	return false;

}