#ifndef HORIZONTAL_VERTICAL_H
#define HORIZONTAL_VERTICAL_H

#include <cstdlib>
#include "ChessPiece.h"
#include "PieceType.h"
#include "../Coordinates.h"

class Board;

// Diamond pattern c++ multiple inheritence
class HorizontalVertical : public virtual ChessPiece{

	public:
		virtual ~HorizontalVertical() = default;
		virtual bool isValid(const Coordinates& newPos, const Board* board) const;

};

#endif