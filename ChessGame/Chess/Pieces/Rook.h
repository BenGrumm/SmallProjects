#ifndef ROOK_H
#define ROOK_H

#include "HorizontalVertical.h"
#include "PieceType.h"

class Rook : public HorizontalVertical{

	public:
		Rook(Colour ct, int x, int y);
		void setMoved();
		[[nodiscard]] bool getIfMoved() const;
	private:
		bool hasMoved = false;

};

inline Rook::Rook(Colour ct, int x, int y) : HorizontalVertical(ct, PieceType::ROOK, x, y), ChessPiece(ct, PieceType::ROOK, x, y){}

inline void Rook::setMoved(){
	hasMoved = true;
}

inline bool Rook::getIfMoved() const{
	return hasMoved;
}

#endif