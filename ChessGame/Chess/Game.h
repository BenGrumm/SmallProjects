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
		bool movePiece(const Coordinates& origin, const Coordinates& destination);
		[[nodiscard]] Colour whosTurn() const;
		friend std::ostream& operator<<(std::ostream& os, Game& g);
	private:
		Board* board;

};

inline Game::Game(){
	board = new Board();
}

inline Colour Game::whosTurn() const{
	return ((board->getMoveNumber() % 2) == 0) ? Colour::WHITE : Colour::BLACK;
}

inline std::ostream& operator<<(std::ostream& os, Game& g){
	os << "Turn = " << g.whosTurn() << ", Turn Num = " << g.board->getMoveNumber() << std::endl;
	os << *g.board;
	return os;
}

#endif