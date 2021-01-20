#include "Board.h"

bool Board::isInCheck(const Coordinates& position, Colour kingColour) const{

	for(int y = 0; y < 8; y++){
		for(int x = 0; x < 8; x++){
			if(chessBoard[y][x] != NULL && chessBoard[y][x]->getPieceColour() == !kingColour && chessBoard[y][x]->isValid(position, this)){
				return true;
			}
		}
	}

	return false;
}

std::ostream& operator<<(std::ostream& os, const Board& b){
	os << "   -------------------------------";
	for(int y = 0; y < 8; y++){
		os << std::endl << y << " | ";
		for(int x = 0; x < 8; x++){
			
			if(b.chessBoard[y][x] == NULL){
				os << " ";
			}else{
				os << *b.chessBoard[y][x];
			}

			os << " | ";
		}
		os << std::endl << "   -------------------------------";
	}

	os << std::endl << "    0   1   2   3   4   5   6   7 ";

	return os;
}