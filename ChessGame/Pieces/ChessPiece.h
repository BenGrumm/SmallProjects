#ifndef CHESSPIECE_H
#define CHESSPIECE_H

#include <iostream>
#include "PieceType.h"

class ChessPiece{

	public:
		ChessPiece(Colour ct, const PieceType& pt, int x, int y);
		virtual bool isValid(int newX, int newY, ChessPiece*** board) const = 0;
		[[nodiscard]] Colour getPieceColour() const;
		void updatePosition(int newX, int newY);
		friend std::ostream& operator<<(std::ostream& os, const ChessPiece& p);
	protected:
		Colour colourType;
		PieceType piece;
		int xPosition, yPosition;
		int move(int newX, int newY);

};

inline ChessPiece::ChessPiece(Colour ct, const PieceType& pt, int x, int y) : piece(pt){
	colourType = ct;
	xPosition = x;
	yPosition = y;
}

inline int ChessPiece::move(int newX, int newY){
	xPosition = newX;
	yPosition = newY;
}

inline Colour ChessPiece::getPieceColour() const{
	return colourType;
}

inline std::ostream& operator<<(std::ostream& os, const ChessPiece& p){
	os << ((p.colourType == Colour::WHITE) ? p.piece.getWhitePiece() : p.piece.getBlackPiece());
	return os;
}

inline void ChessPiece::updatePosition(int newX, int newY){
	xPosition = newX;
	yPosition = newY;
}

#endif