#ifndef DIAGONAL_H
#define DIAGONAL_H

#include <cstdlib>
#include "ChessPiece.h"
#include "PieceType.h"
#include "../Coordinates.h"

class Board;

// Diamond pattern c++ multiple inheritence
class Diagonal : public virtual ChessPiece{

	public:
		virtual ~Diagonal() = default;
		virtual bool isValid(const Coordinates& newPos, const Board* board) const;

};

#endif