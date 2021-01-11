#ifndef KING_H
#define KING_H

#include "ChessPiece.h"
#include "PieceType.h"

class King : public ChessPiece{

	public:
		King(Colour ct, int x, int y);
		King(const King& k);
		~King() = default;
		bool isValid(int newX, int newY, const Board* board) const;
	private:
		bool hasMoved = false;

};

inline King::King(Colour ct, int x, int y) : ChessPiece(ct, PieceType::KING, x, y){}

inline King::King(const King& k) : ChessPiece(k.coulourType, PieceType::KING, k.xPosition, k.yPosition){
	hasMoved = k.hasMoved;
}

#endif