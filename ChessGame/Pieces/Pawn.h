#ifndef PAWN_H
#define PAWN_H

#include "ChessPiece.h"

class Board;

class Pawn : public ChessPiece{

	public:
		Pawn(Colour ct, int x, int y);
		[[nodiscard]] bool isValid(int newX, int newY, const Board* board) const;
		[[nodiscard]] int getDoubleMoveNum();
		void setDoubleMove(int moveNum);
	private:
		// movedOnMove == -1 when pawn hasnt double jumped for 1st move else will == move number the pawn doubled
		int direction, movedOnMove;

};

inline Pawn::Pawn(Colour ct, int x, int y) : ChessPiece(ct, PieceType::PAWN, x, y){
	// If white move up board, else black and move down
	direction = (this->colourType == Colour::WHITE) ? -1 : 1;
	movedOnMove = -1;
}

inline void Pawn::setDoubleMove(int moveNum){
	movedOnMove = moveNum;
}

inline int Pawn::getDoubleMoveNum(){
	return movedOnMove;
}

#endif