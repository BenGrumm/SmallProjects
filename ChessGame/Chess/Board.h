#ifndef BOARD_H
#define BOARD_H

#include <iostream>
#include <memory>
#include <vector>

#include "Pieces/ChessPiece.h"
#include "Pieces/PieceType.h"
#include "Pieces/Pawn.h"
#include "Pieces/Queen.h"
#include "Pieces/Rook.h"
#include "Pieces/Bishop.h"
#include "Pieces/Knight.h"
#include "Pieces/King.h"

#include "Coordinates.h"

class Board{

	public:
		Board();
		Board(const Board& other);
		// Bounds of board 0-7
		static bool withinBoard(const Coordinates& position);
		[[nodiscard]] const ChessPiece* getPosition(const Coordinates& position) const;
		[[nodiscard]] bool isInCheck(const Coordinates& position, Colour kingColour) const;
		void movePiece(const Coordinates& origin, const Coordinates& destination);
		void deletePiece(const Coordinates& position);
		bool setPawnDouble(const Coordinates& position);
		[[nodiscard]] int getMoveNumber() const;
		
		Board& operator=(const Board& other);

		friend std::ostream& operator<<(std::ostream& os, const Board& b);
	private:
		int moveNumber = 0;
		std::shared_ptr<ChessPiece> chessBoard[8][8];
		std::vector<std::shared_ptr<ChessPiece>> whitePieces;
		std::vector<std::shared_ptr<ChessPiece>> blackPieces;

		void populateBoard();
		void copyBoard(const Board& board);

};

inline Board::Board(){
	std::cout << "defualt constructor" << std::endl;
	populateBoard();
}

inline bool Board::withinBoard(const Coordinates& position){
	return position.x >= 0 && position.x <= 7 && position.y >= 0 && position.y <= 7;
}

inline void Board::populateBoard(){

	// Populate pawns
	for(int x = 0; x < 8; x++){
		// Last rows printed last which is bottom of board
		chessBoard[6][x] = std::make_shared<Pawn>(Pawn(Colour::WHITE, {x, 6}));
		chessBoard[1][x] = std::make_shared<Pawn>(Pawn(Colour::BLACK, {x, 1}));
	}

	chessBoard[7][4] = std::make_shared<King>(King(Colour::WHITE, {4, 7}));
	chessBoard[0][4] = std::make_shared<King>(King(Colour::BLACK, {4, 0}));

	chessBoard[7][3] = std::make_shared<Queen>(Queen(Colour::WHITE, {3, 7}));
	chessBoard[0][3] = std::make_shared<Queen>(Queen(Colour::BLACK, {3, 0}));

	for(int x = 0; x < 2; x++){
		chessBoard[7][x * 7] = std::make_shared<Rook>(Rook(Colour::WHITE, {(x * 7), 7}));
		chessBoard[0][x * 7] = std::make_shared<Rook>(Rook(Colour::BLACK, {(x * 7), 0}));

		chessBoard[7][2 + (x * 3)] = std::make_shared<Bishop>(Bishop(Colour::WHITE, {(2 + (x * 3)), 7}));
		chessBoard[0][2 + (x * 3)] = std::make_shared<Bishop>(Bishop(Colour::BLACK, {(2 + (x * 3)), 0}));

		chessBoard[7][1 + (x * 5)] = std::make_shared<Knight>(Knight(Colour::WHITE, {(1 + (x * 5)), 7}));
		chessBoard[0][1 + (x * 5)] = std::make_shared<Knight>(Knight(Colour::BLACK, {(1 + (x * 5)), 0}));;
	}

}

inline const ChessPiece* Board::getPosition(const Coordinates& position) const{
	return chessBoard[position.y][position.x].get();
}

inline int Board::getMoveNumber() const{
	return moveNumber;
}

inline bool Board::setPawnDouble(const Coordinates& position){
	if(chessBoard[position.y][position.x]->getPieceType() == PieceType::PAWN){
		((Pawn*)chessBoard[position.y][position.x].get())->setDoubleMove(moveNumber);
		return true;
	}

	return false;
}

inline void Board::movePiece(const Coordinates& origin, const Coordinates& destination){
	moveNumber++;

	chessBoard[destination.y][destination.x] = std::move(chessBoard[origin.y][origin.x]);

	chessBoard[destination.y][destination.x]->move(destination);

}

inline void Board::deletePiece(const Coordinates& position){
	chessBoard[position.y][position.x] = NULL;
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

					this->chessBoard[y][x] = std::make_shared<Pawn>( Pawn( *(dynamic_cast<Pawn*>(other.chessBoard[y][x].get())) ) );

				}else if(other.chessBoard[y][x]->getPieceType() == PieceType::ROOK){

					this->chessBoard[y][x] = std::make_shared<Rook>( Rook( *(dynamic_cast<Rook*>(other.chessBoard[y][x].get())) ) );

				}else if(other.chessBoard[y][x]->getPieceType() == PieceType::QUEEN){

					this->chessBoard[y][x] = std::make_shared<Queen>( Queen( *(dynamic_cast<Queen*>(other.chessBoard[y][x].get())) ) );

				}

			}

		}
	}

	this->moveNumber = other.moveNumber;
}

std::ostream& operator<<(std::ostream& os, const Board& b);

#endif