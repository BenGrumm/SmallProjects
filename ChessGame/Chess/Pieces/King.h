#ifndef KING_H
#define KING_H

#include "ChessPiece.h"
#include "PieceType.h"
#include "../Coordinates.h"

class King : public ChessPiece{

	public:
		King(Colour ct, const Coordinates& pos);
		King(const King& k);
		~King() = default;
		bool isValid(const Coordinates& newPos, const Board* board) const;
	private:
		bool hasMoved = false;

};

inline King::King(Colour ct, const Coordinates& pos) : ChessPiece(ct, PieceType::KING, pos){}

inline King::King(const King& k) : ChessPiece(k.colourType, PieceType::KING, {k.xPosition, k.yPosition}){
	hasMoved = k.hasMoved;
}

#endif