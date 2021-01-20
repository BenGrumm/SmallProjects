#include <memory>

#include "Game.h"
#include "Pieces/PieceType.h"
#include "Coordinates.h"

/***
 * Function to control if chosen square is valid piece and can move to given square
 *
 * @param origin struct with origin coordinates of piece to move
 * @param destination struct with destination coordinates of piece to move
 * 
 * @return if move is valid true else false
 **/
bool Game::movePiece(const Coordinates& origin, const Coordinates& destination){

	// If one of coordinates is not within board
	if(!Board::withinBoard(origin) || !Board::withinBoard(destination)){
		return false;
	}

	const ChessPiece* pieceToMove = board->getPosition(origin);

	// Check square of which piece to move is not null, its that sides turn, and the move is valid
	if(pieceToMove == NULL || pieceToMove->getPieceColour() != whosTurn() || !pieceToMove->isValid(destination, board)){
		return false;
	}

	// If pawn double jumped set move it did so or if its taking enPassant delete piece behind
	if(pieceToMove->getPieceType() == PieceType::PAWN){
		if(destination.x == origin.x && (origin.y == (destination.y + 2) || origin.y == (destination.y - 2))){
			board->setPawnDouble(origin);
		}else if(board->getPosition(destination) == NULL && origin.x != destination.x){
			board->deletePiece({destination.x, origin.y});
		}
	}else if(pieceToMove->getPieceType() == PieceType::KING && std::abs(origin.x - destination.x) > 1){
		// Swap rook position
		board->movePiece({(destination.x > origin.x ? 7 : 0), origin.y}, {(destination.x > origin.x ? 5 : 3), origin.y});
	}

	board->movePiece(origin, destination);

	return true;
}