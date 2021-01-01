#ifndef PIECETYPES_H
#define PIECETYPES_H

#include <iostream>

class PieceType{

	public:
		static const PieceType KING;
		static const PieceType QUEEN;
		static const PieceType ROOK;
		static const PieceType BISHOP;
		static const PieceType KNIGHT;
		static const PieceType PAWN;
		
		[[nodiscard]] inline const char* getWhitePiece() const{
			return whiteUnicode;
		}

		[[nodiscard]] inline const char* getBlackPiece() const{
			return blackUnicode;
		}

	private:
		const char* whiteUnicode;
		const char* blackUnicode;

		inline PieceType(const char* unicodeW, const char* unicodeB){
			whiteUnicode = unicodeW;
			blackUnicode = unicodeB;
		}

};

enum class Colour{
	BLACK,
	WHITE
};

// Overload ! operator to return other Colour 
inline Colour operator!(Colour old){
	if(old == Colour::BLACK){
		return Colour::WHITE;
	}

	return Colour::BLACK;
}

inline std::ostream& operator<<(std::ostream& os, const Colour& c){
	os << ((c == Colour::WHITE) ? "White" : "Black");
	return os;
}

#endif