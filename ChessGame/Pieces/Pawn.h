#ifndef PAWN_H
#define PAWN_H

#include "ChessPiece.h"

class Pawn : public ChessPiece{

	public:
		Pawn(Colour ct, int x, int y);
		bool isValid(int newX, int newY, ChessPiece*** board) const;
	private:
		// movedOnMove == -1 when pawn hasnt moved else will == move number the pawn moved on
		int direction, movedOnMove;

};

inline Pawn::Pawn(Colour ct, int x, int y) : ChessPiece(ct, PieceType::PAWN, x, y){
	// If white move up board, else black and move down
	direction = (this->colourType == Colour::WHITE) ? -1 : 1;
	movedOnMove = -1;
}

#endif