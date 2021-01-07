#include <memory>

#include "Chess/Game.h"
#include "Chess/Board.h"
#include "Chess/Pieces/Queen.h"
#include "Chess/Pieces/ChessPiece.h"

// https://stackoverflow.com/questions/22491174/when-do-we-need-to-define-destructors

int main(int argc, char const *argv[])
{

	Board* boardA = new Board();

	std::cout << "Address = " << boardA << std::endl << *boardA << std::endl;

	Board* boardB = new Board;

	*boardB = *boardA;

	const std::shared_ptr<ChessPiece> p = boardB->getPosition(0, 0);

	delete boardA;

	std::cout << "Rook = ? " << p << std::endl;

	std::cout << "Address = " << boardB << std::endl << *boardB << std::endl;

	return 0;
}