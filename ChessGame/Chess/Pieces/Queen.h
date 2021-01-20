#ifndef QUEEN_H
#define QUEEN_H

#include "PieceType.h"
#include "ChessPiece.h"
#include "HorizontalVertical.h"
#include "Diagonal.h"

class Queen : public HorizontalVertical, public Diagonal {

	public:
		Queen(Colour ct, const Coordinates& pos);
		~Queen() = default;
		bool isValid(const Coordinates& newPos, const Board* board) const;

};

inline Queen::Queen(Colour ct, const Coordinates& pos) : ChessPiece(ct, PieceType::QUEEN, pos){}

inline bool Queen::isValid(const Coordinates& newPos, const Board* board) const{
	return Diagonal::isValid(newPos, board) || HorizontalVertical::isValid(newPos, board);
}

#endif