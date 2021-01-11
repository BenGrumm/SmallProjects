#ifndef KNIGHT_H
#define KNIGHT_H

#include "ChessPiece.h"
#include "PieceType.h"

class Knight : public ChessPiece{
	public:
		Knight(Colour ct, int x, int y);
		Knight(const Knight& p);
		~Knight() = default;
		bool isValid(int newX, int newY, const Board* board) const;
};

inline Knight::Knight(Colour ct, int x, int y) : ChessPiece(ct, PieceType::KNIGHT, x, y){}

inline Knight::Knight(const Knight& p) : ChessPiece(p.colourType, PieceType::KNIGHT, p.xPosition, p.yPosition){}

#endif