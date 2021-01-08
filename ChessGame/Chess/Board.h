#ifndef BOARD_H
#define BOARD_H

#include <iostream>
#include <memory>

#include "Pieces/ChessPiece.h"
#include "Pieces/PieceType.h"
#include "Pieces/Pawn.h"
#include "Pieces/Queen.h"
#include "Pieces/Rook.h"

class Board{

	public:
		Board();
		Board(const Board& other);
		// Bounds of board 0-7
		static bool withinBoard(int x, int y);
		[[nodiscard]] const ChessPiece* getPosition(int x, int y) const;
		void movePiece(int originX, int originY, int destinationX, int destinationY);
		void deletePiece(int x, int y);
		bool setPawnDouble(int x, int y);
		[[nodiscard]] int getMoveNumber() const;
		
		Board& operator=(const Board& other);

		friend std::ostream& operator<<(std::ostream& os, const Board& b);
	private:
		int moveNumber = 0;
		std::unique_ptr<ChessPiece> chessBoard[8][8];

		void populateBoard();
		void copyBoard(const Board& board);

};

inline Board::Board(){
	std::cout << "defualt constructor" << std::endl;
	populateBoard();
}

inline bool Board::withinBoard(int x, int y){
	return x >= 0 && x <= 7 && y >= 0 && y <= 7;
}

inline void Board::populateBoard(){

	// Populate pawns
	for(int x = 0; x < 8; x++){
		// Last rows printed last which is bottom of board
		chessBoard[6][x] = std::make_unique<Pawn>(Pawn(Colour::WHITE, x, 6));
		chessBoard[1][x] = std::make_unique<Pawn>(Pawn(Colour::BLACK, x, 1));
	}

	chessBoard[7][3] = std::make_unique<Queen>(Queen(Colour::WHITE, 3, 7));
	chessBoard[0][3] = std::make_unique<Queen>(Queen(Colour::BLACK, 3, 0));

	for(int x = 0; x < 2; x++){
		chessBoard[7][x * 7] = std::make_unique<Rook>(Rook(Colour::WHITE, (x * 7), 7));
		chessBoard[0][x * 7] = std::make_unique<Rook>(Rook(Colour::BLACK, (x * 7), 0));
	}
}

inline const ChessPiece* Board::getPosition(int x, int y) const{
	return chessBoard[y][x].get();
}

inline int Board::getMoveNumber() const{
	return moveNumber;
}

inline bool Board::setPawnDouble(int x, int y){
	if(chessBoard[y][x]->getPieceType() == PieceType::PAWN){
		((Pawn*)chessBoard[y][x].get())->setDoubleMove(moveNumber);
		return true;
	}

	return false;
}

inline void Board::movePiece(int originX, int originY, int destinationX, int destinationY){
	moveNumber++;

	chessBoard[destinationY][destinationX] = std::move(chessBoard[originY][originX]);

	chessBoard[destinationY][destinationX]->move(destinationX, destinationY);

}

inline void Board::deletePiece(int x, int y){
	chessBoard[y][x] = NULL;
}

inline Board::Board(const Board& other){

	copyBoard(other);

}

inline Board& Board::operator=(const Board& other){
	
	copyBoard(other);

	return *this;
}

inline void Board::copyBoard(const Board& other){

	for(int y = 0; y < 8; y++){
		for(int x = 0; x < 8; x++){

			this->chessBoard[y][x] = NULL;

			if(other.chessBoard[y][x] != NULL){

				if(other.chessBoard[y][x]->getPieceType() == PieceType::PAWN){

					this->chessBoard[y][x] = std::make_unique<Pawn>( Pawn( *(dynamic_cast<Pawn*>(other.chessBoard[y][x].get())) ) );

				}else if(other.chessBoard[y][x]->getPieceType() == PieceType::ROOK){

					this->chessBoard[y][x] = std::make_unique<Rook>( Rook( *(dynamic_cast<Rook*>(other.chessBoard[y][x].get())) ) );

				}else if(other.chessBoard[y][x]->getPieceType() == PieceType::QUEEN){

					this->chessBoard[y][x] = std::make_unique<Queen>( Queen( *(dynamic_cast<Queen*>(other.chessBoard[y][x].get())) ) );

				}

			}

		}
	}

	this->moveNumber = other.moveNumber;
}

std::ostream& operator<<(std::ostream& os, const Board& b);

#endif