#ifndef BOARD_H
#define BOARD_H

#include <iostream>

#include "Pieces/ChessPiece.h"
#include "Pieces/PieceType.h"
#include "Pieces/Pawn.h"

class Board{

	public:
		Board();
		// Bounds of board 0-7
		static bool withinBoard(int x, int y);
		[[nodiscard]] const ChessPiece* getPosition(int x, int y) const;
		void movePiece(int originX, int originY, int destinationX, int destinationY);
		friend std::ostream& operator<<(std::ostream& os, const Board& b);
		[[nodiscard]] ChessPiece*** getBoard();
	private:
		int moveNumber;
		ChessPiece*** chessBoard;
		void populateBoard();

};

inline Board::Board(){
	populateBoard();
}

inline bool Board::withinBoard(int x, int y){
	return x >= 0 && x <= 7 && y >= 0 && y <= 7;
}

inline void Board::populateBoard(){

	chessBoard = new ChessPiece**[8];

	for(int i = 0; i < 8; i++){
		chessBoard[i] = new ChessPiece*[8];
	}

	// Populate pawns
	for(int x = 0; x < 8; x++){
		// Last rows printed last which is bottom of board
		chessBoard[6][x] = new Pawn(Colour::WHITE, x, 6);
		chessBoard[1][x] = new Pawn(Colour::BLACK, x, 1);
	}
}

inline const ChessPiece* Board::getPosition(int x, int y) const{
	return chessBoard[y][x];
}

inline ChessPiece*** Board::getBoard(){
	return chessBoard;
}

inline void Board::movePiece(int originX, int originY, int destinationX, int destinationY){
	// if piece is being take delete
	if(chessBoard[destinationY][destinationX]){
		delete chessBoard[destinationY][destinationX];
	}

	chessBoard[destinationY][destinationX] = chessBoard[originY][originX];
	chessBoard[originY][originX] = NULL;

	chessBoard[destinationY][destinationX]->updatePosition(destinationX, destinationY);
}

std::ostream& operator<<(std::ostream& os, const Board& b);

#endif