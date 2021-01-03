#ifndef CHESSPIECE_H
#define CHESSPIECE_H

#include <iostream>
#include "PieceType.h"

class Board;

class ChessPiece{

	public:
		ChessPiece(Colour ct, const PieceType& pt, int x, int y);
		virtual bool isValid(int newX, int newY, const Board* board) const = 0;
		void move(int newX, int newY);
		[[nodiscard]] Colour getPieceColour() const;
		[[nodiscard]] const PieceType& getPieceType() const;
		friend std::ostream& operator<<(std::ostream& os, const ChessPiece& p);
	protected:
		Colour colourType;
		const PieceType& piece;
		int xPosition, yPosition;

};

inline ChessPiece::ChessPiece(Colour ct, const PieceType& pt, int x, int y) : colourType(ct), piece(pt){
	xPosition = x;
	yPosition = y;
}

inline void ChessPiece::move(int newX, int newY){
	xPosition = newX;
	yPosition = newY;
}

inline Colour ChessPiece::getPieceColour() const{
	return colourType;
}

inline const PieceType& ChessPiece::getPieceType() const{
	return piece;
}

inline std::ostream& operator<<(std::ostream& os, const ChessPiece& p){
	os << ((p.colourType == Colour::WHITE) ? p.piece.getWhitePiece() : p.piece.getBlackPiece());
	return os;
}

#endif