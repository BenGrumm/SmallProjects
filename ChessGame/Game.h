/****************************************************

Game Class that controls board, which sides move

****************************************************/
#ifndef GAME_H
#define GAME_H

#include "Board.h"
#include "Pieces/PieceType.h"
#include <iostream>

class Game{

	public:
		Game();
		bool movePiece(int originX, int originY, int destinationX, int destinationY);
		[[nodiscard]] Colour whosTurn() const;
		friend std::ostream& operator<<(std::ostream& os, Game& g);
	private:
		// moveNumber increased for each sides turn
		int moveNumber;
		Board* board;

};

inline Game::Game(){
	board = new Board();
	moveNumber = 0;
}

inline Colour Game::whosTurn() const{
	return ((moveNumber % 2) == 0) ? Colour::WHITE : Colour::BLACK;
}

inline std::ostream& operator<<(std::ostream& os, Game& g){
	os << "Turn = " << g.whosTurn() << ", Turn Num = " << g.moveNumber << std::endl;
	os << *g.board;
	return os;
}

#endif