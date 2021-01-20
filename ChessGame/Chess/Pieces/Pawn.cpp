#include <memory>

#include "Pawn.h"
#include "PieceType.h"
#include "../Board.h"
#include "../Coordinates.h"

bool Pawn::isValid(const Coordinates& newPos, const Board* board) const{

	const ChessPiece* newPosition = board->getPosition(newPos);

	// If move is 1 forward with no piece in position
	if(newPos.x == xPosition && newPos.y == (yPosition + direction) && newPosition == NULL){
		return true;
	}
	// If move is 1 forward and 1 to left / right and there is an piece of the oppsite colour
	else if((newPos.x == xPosition + 1 || newPos.x == xPosition - 1) && newPos.y == (yPosition + direction) && newPosition != NULL && newPosition->getPieceColour() == !colourType){
		return true;
	}
	// If move is 2 forward and is pawn 1st move and nothing in mid position (- direction in new poisition want square behind)
	else if(newPos.x == xPosition && newPos.y == yPosition + (2 * direction) && newPosition == NULL && movedOnMove == -1 && board->getPosition({newPos.x, (newPos.y - direction)}) == NULL){
		return true;
	}else if((newPos.x == xPosition + 1 || newPos.x == xPosition - 1) && newPos.y == (yPosition + direction) && newPosition == NULL && board->getPosition({newPos.x, yPosition}) != NULL){
		// Check if piece is pawn and just double jumped
		const ChessPiece* enPassantPiece = board->getPosition({newPos.x, yPosition});

		std::cout << "En Passent Pawn - " << board->getPosition({newPos.x, yPosition}) << ", New Pos - " << newPosition << std::endl;
		std::cout << "X = " << newPos.x << ", Y = " << yPosition << ", Pawn Moved On - " << ((Pawn*)(enPassantPiece))->getDoubleMoveNum() << ", Move Num - " << "move num" << std::endl;

		// Need to get move number
		if(enPassantPiece->getPieceColour() == !colourType && enPassantPiece->getPieceType() == PieceType::PAWN && ((Pawn*)(enPassantPiece))->getDoubleMoveNum() == (board->getMoveNumber() - 1)){
			return true;
		}
	}

	return false;
}