#ifndef BISHOP_H
#define BISHOP_H

#include "Diagonal.h"
#include "PieceType.h"

class Bishop : public Diagonal{

	public:
		Bishop(Colour ct, const Coordinates& position);
		Bishop(const Bishop& p);
		~Bishop() = default;

};

inline Bishop::Bishop(Colour ct, const Coordinates& position) : ChessPiece(ct, PieceType::BISHOP, position){}

inline Bishop::Bishop(const Bishop& p) : ChessPiece(p.colourType, PieceType::BISHOP, {p.xPosition, p.yPosition}){}

#endif