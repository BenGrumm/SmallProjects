#ifndef KNIGHT_H
#define KNIGHT_H

#include "ChessPiece.h"
#include "PieceType.h"

#include "../Coordinates.h"

class Knight : public ChessPiece{
	public:
		Knight(Colour ct, const Coordinates& pos);
		Knight(const Knight& p);
		~Knight() = default;
		bool isValid(const Coordinates& newPos, const Board* board) const;
};

inline Knight::Knight(Colour ct, const Coordinates& pos) : ChessPiece(ct, PieceType::KNIGHT, pos){}

inline Knight::Knight(const Knight& p) : ChessPiece(p.colourType, PieceType::KNIGHT, {p.xPosition, p.yPosition}){}

#endif