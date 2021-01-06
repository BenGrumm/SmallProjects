#include "Game.h"
#include "Pieces/PieceType.h"

/***
 * Function to control if chosen square is valid piece and can move to given square
 *
 * @param originX x coordinate of piece to move
 * @param originY y coordinate of piece to move
 * @param destinationX x coordinate of destination square
 * @param destinationY y coordinate of destination square
 * 
 * @return if move is valid true else false
 **/
bool Game::movePiece(int originX, int originY, int destinationX, int destinationY){

	const ChessPiece* pieceToMove = board->getPosition(originX, originY);

	// Check square of which piece to move is not null, its that sides turn, and the move is valid
	if(pieceToMove == NULL || pieceToMove->getPieceColour() != whosTurn() || !pieceToMove->isValid(destinationX, destinationY, board)){
		return false;
	}

	// If pawn double jumped set move it did so or if its taking enPassant delete piece behind
	if(pieceToMove->getPieceType() == PieceType::PAWN){
		if(destinationX == originX && (originY == (destinationY + 2) || originY == (destinationY - 2))){
			((Pawn*) pieceToMove)->setDoubleMove(board->getMoveNumber());
		}else if(board->getPosition(destinationX, destinationY) == NULL && originX != destinationX){
			board->deletePiece(destinationX, originY);
		}
		
	}

	board->movePiece(originX, originY, destinationX, destinationY);

	return true;
}