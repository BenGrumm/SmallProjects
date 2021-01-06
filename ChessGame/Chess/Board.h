#ifndef BOARD_H
#define BOARD_H

#include <iostream>

#include "Pieces/ChessPiece.h"
#include "Pieces/PieceType.h"
#include "Pieces/Pawn.h"
#include "Pieces/Queen.h"
#include "Pieces/Rook.h"

class Board{

	public:
		Board();
		~Board();
		// Bounds of board 0-7
		static bool withinBoard(int x, int y);
		[[nodiscard]] const ChessPiece* getPosition(int x, int y) const;
		void movePiece(int originX, int originY, int destinationX, int destinationY);
		void deletePiece(int x, int y);
		friend std::ostream& operator<<(std::ostream& os, const Board& b);
		[[nodiscard]] ChessPiece*** getBoard();
		[[nodiscard]] int getMoveNumber() const;
	private:
		int moveNumber = 0;
		ChessPiece*** chessBoard;
		void populateBoard();

};

inline Board::Board(){
	populateBoard();
}

inline Board::~Board(){
	for(int y = 0; y < 8; y++){
		for(int x = 0; x < 8; x++){
			if(chessBoard[y][x] != NULL){
				delete chessBoard[y][x];
			}
		}

		delete[] chessBoard[y];
	}
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

	chessBoard[7][3] = new Queen(Colour::WHITE, 3, 7);
	chessBoard[0][3] = new Queen(Colour::BLACK, 3, 0);

	for(int x = 0; x < 2; x++){
		chessBoard[7][x * 7] = new Rook(Colour::WHITE, (x * 7), 7);
		chessBoard[0][x * 7] = new Rook(Colour::BLACK, (x * 7), 0);
	}
}

inline const ChessPiece* Board::getPosition(int x, int y) const{
	return chessBoard[y][x];
}

inline int Board::getMoveNumber() const{
	return moveNumber;
}

inline ChessPiece*** Board::getBoard(){
	return chessBoard;
}

inline void Board::movePiece(int originX, int originY, int destinationX, int destinationY){
	moveNumber++;

	// if piece is being taken delete
	if(chessBoard[destinationY][destinationX]){
		delete chessBoard[destinationY][destinationX];
	}

	chessBoard[destinationY][destinationX] = chessBoard[originY][originX];
	chessBoard[originY][originX] = NULL;

	chessBoard[destinationY][destinationX]->move(destinationX, destinationY);

}

inline void Board::deletePiece(int x, int y){
	chessBoard[y][x] = NULL;
}

std::ostream& operator<<(std::ostream& os, const Board& b);

#endif