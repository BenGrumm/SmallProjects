#ifndef BISHOP_H
#define BISHOP_H

#include "Diagonal.h"
#include "PieceType.h"

class Bishop : public Diagonal{

	public:
		Bishop(Colour ct, int x, int y);
		Bishop(const Bishop& p);
		~Bishop() = default;

};

inline Bishop::Bishop(Colour ct, int x, int y) : ChessPiece(ct, PieceType::BISHOP, x, y){}

inline Bishop::Bishop(const Bishop& p) : ChessPiece(p.colourType, PieceType::BISHOP, p.xPosition, p.yPosition){}

#endif