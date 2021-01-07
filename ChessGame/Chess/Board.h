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
		// Bounds of board 0-7
		static bool withinBoard(int x, int y);
		[[nodiscard]] const std::shared_ptr<ChessPiece> getPosition(int x, int y) const;
		void movePiece(int originX, int originY, int destinationX, int destinationY);
		void deletePiece(int x, int y);
		[[nodiscard]] int getMoveNumber() const;
		
		Board& operator=(const Board& other);

		friend std::ostream& operator<<(std::ostream& os, const Board& b);
	private:
		int moveNumber = 0;
		std::shared_ptr<ChessPiece> chessBoard[8][8];

		void populateBoard();

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
		chessBoard[6][x] = std::make_shared<Pawn>(Pawn(Colour::WHITE, x, 6));
		chessBoard[1][x] = std::make_shared<Pawn>(Pawn(Colour::BLACK, x, 1));
	}

	chessBoard[7][3] = std::make_shared<Queen>(Queen(Colour::WHITE, 3, 7));
	chessBoard[0][3] = std::make_shared<Queen>(Queen(Colour::BLACK, 3, 0));

	for(int x = 0; x < 2; x++){
		chessBoard[7][x * 7] = std::make_shared<Rook>(Rook(Colour::WHITE, (x * 7), 7));
		chessBoard[0][x * 7] = std::make_shared<Rook>(Rook(Colour::BLACK, (x * 7), 0));
	}
}

inline const std::shared_ptr<ChessPiece> Board::getPosition(int x, int y) const{
	return chessBoard[y][x];
}

inline int Board::getMoveNumber() const{
	return moveNumber;
}

inline void Board::movePiece(int originX, int originY, int destinationX, int destinationY){
	moveNumber++;

	// if piece is being taken delete
	if(chessBoard[destinationY][destinationX]){
		chessBoard[destinationY][destinationX] = std::shared_ptr<ChessPiece>(nullptr);
	}

	chessBoard[destinationY][destinationX] = chessBoard[originY][originX];
	chessBoard[originY][originX] = NULL;

	chessBoard[destinationY][destinationX]->move(destinationX, destinationY);

}

inline void Board::deletePiece(int x, int y){
	chessBoard[y][x] = std::shared_ptr<ChessPiece>(nullptr);
}

inline Board& Board::operator=(const Board& other){
	
	for(int y = 0; y < 8; y++){
		for(int x = 0; x < 8; x++){

			if(other.chessBoard[y][x] != NULL){

				if(other.chessBoard[y][x]->getPieceType() == PieceType::PAWN){

					this->chessBoard[y][x] = std::make_shared<Pawn>( Pawn( *std::dynamic_pointer_cast<Pawn>(other.chessBoard[y][x]) ) );
				
				}else if(other.chessBoard[y][x]->getPieceType() == PieceType::ROOK){

					this->chessBoard[y][x] = std::make_shared<Rook>( Rook( *std::dynamic_pointer_cast<Rook>(other.chessBoard[y][x]) ) );

				}else if(other.chessBoard[y][x]->getPieceType() == PieceType::QUEEN){

					this->chessBoard[y][x] = std::make_shared<Queen>( Queen( *std::dynamic_pointer_cast<Queen>(other.chessBoard[y][x]) ) );
					
				}

			}

		}
	}

	this->moveNumber = other.moveNumber;

	return *this;
}

std::ostream& operator<<(std::ostream& os, const Board& b);

#endif