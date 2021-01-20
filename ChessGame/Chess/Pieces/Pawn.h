#ifndef PAWN_H
#define PAWN_H

#include "ChessPiece.h"
#include "../Coordinates.h"

class Board;

class Pawn : public ChessPiece{

	public:
		Pawn(Colour ct, const Coordinates& pos);
		Pawn(const Pawn& p);
		~Pawn() = default;
		[[nodiscard]] bool isValid(const Coordinates& newPos, const Board* board) const;
		[[nodiscard]] int getDoubleMoveNum();
		void setDoubleMove(int moveNum);
	private:
		// movedOnMove == -1 when pawn hasnt double jumped for 1st move else will == move number the pawn doubled
		int direction, movedOnMove;

};

inline Pawn::Pawn(Colour ct, const Coordinates& pos) : ChessPiece(ct, PieceType::PAWN, pos){
	// If white move up board, else black and move down
	direction = (this->colourType == Colour::WHITE) ? -1 : 1;
	movedOnMove = -1;
}

inline Pawn::Pawn(const Pawn& p) : ChessPiece(p.colourType, PieceType::PAWN, {p.xPosition, p.yPosition}){
	direction = p.direction;
	movedOnMove = p.movedOnMove;
}

inline void Pawn::setDoubleMove(int moveNum){
	movedOnMove = moveNum;
}

inline int Pawn::getDoubleMoveNum(){
	return movedOnMove;
}

#endif