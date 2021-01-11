#ifndef QUEEN_H
#define QUEEN_H

#include "PieceType.h"
#include "ChessPiece.h"
#include "HorizontalVertical.h"
#include "Diagonal.h"

class Queen : public HorizontalVertical, public Diagonal {

	public:
		Queen(Colour ct, int x, int y);
		~Queen() = default;
		bool isValid(int newX, int newY, const Board* board) const;

};

inline Queen::Queen(Colour ct, int x, int y) : ChessPiece(ct, PieceType::QUEEN, x, y){}

inline bool Queen::isValid(int newX, int newY, const Board* board) const{
	return Diagonal::isValid(newX, newY, board) || HorizontalVertical::isValid(newX, newY, board);
}

#endif