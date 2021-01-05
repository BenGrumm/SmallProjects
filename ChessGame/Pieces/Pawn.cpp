#include "Pawn.h"
#include "PieceType.h"
#include "../Board.h"

bool Pawn::isValid(int newX, int newY, const Board* board) const{

	std::cout << "Pawn Valid" << std::endl;

	if(!Board::withinBoard(newX, newY)){
		return false;
	}

	const ChessPiece* newPosition = board->getPosition(newX, newY);

	// If move is 1 forward with no piece in position
	if(newX == xPosition && newY == (yPosition + direction) && newPosition == NULL){
		return true;
	}
	// If move is 1 forward and 1 to left / right and there is an piece of the oppsite colour
	else if((newX == xPosition + 1 || newX == xPosition - 1) && newY == (yPosition + direction) && newPosition != NULL && newPosition->getPieceColour() == !colourType){
		return true;
	}
	// If move is 2 forward and is pawn 1st move and nothing in mid position (- direction in new poisition want square behind)
	else if(newX == xPosition && newY == yPosition + (2 * direction) && newPosition == NULL && movedOnMove == -1 && board->getPosition(newX, (newY - direction)) == NULL){
		return true;
	}else if((newX == xPosition + 1 || newX == xPosition - 1) && newY == (yPosition + direction) && newPosition == NULL && board->getPosition(newX, yPosition) != NULL){
		// Check if piece is pawn and just double jumped
		const ChessPiece* enPassantPiece = board->getPosition(newX, yPosition);

		std::cout << "En Passent Pawn - " << board->getPosition(newX, yPosition) << ", New Pos - " << newPosition << std::endl;
		std::cout << "X = " << newX << ", Y = " << yPosition << ", Pawn Moved On - " << ((Pawn*)enPassantPiece)->getDoubleMoveNum() << ", Move Num - " << "move num" << std::endl;

		// Need to get move number
		if(enPassantPiece->getPieceColour() == !colourType && enPassantPiece->getPieceType() == PieceType::PAWN && ((Pawn*)enPassantPiece)->getDoubleMoveNum() == (board->getMoveNumber() - 1)){
			return true;
		}
	}

	return false;
}