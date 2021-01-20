#include "King.h"

#include <unistd.h>

#include "../Board.h"
#include "Rook.h"
#include "PieceType.h"
#include "../Coordinates.h"

bool King::isValid(const Coordinates& newPos, const Board* board) const{

	const ChessPiece* newPiece = board->getPosition(newPos);

	int absXDiff = std::abs(xPosition - newPos.x);
	int absYDiff = std::abs(yPosition - newPos.y);

	if(absXDiff < 2 && absYDiff < 2){

		// TODO Add check for piece being "checked"
		if(newPiece == NULL || newPiece->getPieceColour() == !colourType){
			return true;
		}

	}
	// If is moving to castle and hasnt moved
	else if(!hasMoved && (xPosition - newPos.x == -2 || xPosition - newPos.x == 2) && yPosition == newPos.y){

		const ChessPiece* rook = board->getPosition({((newPos.x > xPosition) ? 7 : 0), yPosition});

		if(rook != NULL && rook->getPieceType() == PieceType::ROOK && !dynamic_cast<const Rook*>(rook)->getIfMoved()){
			// here only check if there are pieces in the way not if it will be in check maybe idk

			int dir = (newPos.x > xPosition) ? 1 : -1;

			// Check every position between king and rook
			for (int i = xPosition + dir; i != ((newPos.x > xPosition) ? 7 : 0); i += dir){
				if(board->getPosition({i, yPosition}) != NULL){
					return false;
				}
			}

			return true;
		}

	}

	return false;

}