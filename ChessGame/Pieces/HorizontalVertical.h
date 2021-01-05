#ifndef HORIZONTAL_VERTICAL_H
#define HORIZONTAL_VERTICAL_H

#include <cstdlib>
#include "ChessPiece.h"
#include "PieceType.h"

class Board;

// Diamond pattern c++ multiple inheritence
class HorizontalVertical : public virtual ChessPiece{

	public:
		HorizontalVertical(Colour ct, const PieceType& pt, int x, int y);
		virtual bool isValid(int newX, int newY, const Board* board) const;

};

inline HorizontalVertical::HorizontalVertical(Colour ct, const PieceType& pt, int x, int y) : ChessPiece(ct, pt, x, y){}

#endif