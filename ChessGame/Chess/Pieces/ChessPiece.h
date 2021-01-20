#ifndef CHESSPIECE_H
#define CHESSPIECE_H

#include <iostream>
#include "PieceType.h"

#include "../Coordinates.h"

class Board;

class ChessPiece{

	public:
		ChessPiece(Colour ct, const PieceType& pt, const Coordinates& position);
		virtual ~ChessPiece() = default;
		void move(const Coordinates& position);
		virtual bool isValid(const Coordinates& position, const Board* board) const = 0;
		//virtual int** getValidMoved(const Board* board) const = 0;
		[[nodiscard]] Colour getPieceColour() const;
		[[nodiscard]] const PieceType& getPieceType() const;
		friend std::ostream& operator<<(std::ostream& os, const ChessPiece& p);
	protected:
		Colour colourType;
		const PieceType* piece;
		int xPosition, yPosition;
		ChessPiece(){std::cout << "ERROR SHOULD PROBABLY FIX - Shouldnt be called" << std::endl;}

};

inline ChessPiece::ChessPiece(Colour ct, const PieceType& pt, const Coordinates& position){
	colourType = ct;
	piece = &pt;
	xPosition = position.x;
	yPosition = position.y;
}

inline void ChessPiece::move(const Coordinates& position){
	xPosition = position.x;
	yPosition = position.y;
}

inline Colour ChessPiece::getPieceColour() const{
	return colourType;
}

inline const PieceType& ChessPiece::getPieceType() const{
	return *piece;
}

inline std::ostream& operator<<(std::ostream& os, const ChessPiece& p){
	os << ((p.colourType == Colour::WHITE) ? p.piece->getWhitePiece() : p.piece->getBlackPiece());
	return os;
}

#endif