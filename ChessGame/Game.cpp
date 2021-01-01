#include "Game.h"

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
	if(pieceToMove == NULL || pieceToMove->getPieceColour() != whosTurn() || !pieceToMove->isValid(destinationX, destinationY, board->getBoard())){
		return false;
	}

	board->movePiece(originX, originY, destinationX, destinationY);

	moveNumber++;

	return true;
}