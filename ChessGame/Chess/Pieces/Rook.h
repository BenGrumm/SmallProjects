#ifndef ROOK_H
#define ROOK_H

#include "HorizontalVertical.h"
#include "PieceType.h"

class Rook : public HorizontalVertical{

	public:
		Rook(Colour ct, int x, int y);
		Rook(const Rook& r);
		~Rook() = default;
		void setMoved();
		[[nodiscard]] bool getIfMoved() const;
	private:
		bool hasMoved = false;

};

inline Rook::Rook(Colour ct, int x, int y) : ChessPiece(ct, PieceType::ROOK, x, y){}

inline Rook::Rook(const Rook& r) : ChessPiece(r.colourType, PieceType::ROOK, r.xPosition, r.yPosition){
	hasMoved = r.hasMoved;
}

inline void Rook::setMoved(){
	hasMoved = true;
}

inline bool Rook::getIfMoved() const{
	return hasMoved;
}

#endif