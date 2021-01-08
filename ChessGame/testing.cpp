#include <memory>

#include "Chess/Game.h"
#include "Chess/Board.h"
#include "Chess/Pieces/Queen.h"
#include "Chess/Pieces/ChessPiece.h"

// https://stackoverflow.com/questions/22491174/when-do-we-need-to-define-destructors

int main(int argc, char const *argv[])
{

	Board* boardA = new Board();

	Board* boardB = new Board;

	boardA->movePiece(0, 7, 0, 5);

	*boardB = *boardA;

	std::cout << "Board A = " << *boardA << std::endl;

	delete boardA;

	std::cout << "Board B = " << *boardB << std::endl;

	std::unique_ptr<int> up = std::make_unique<int>(4);

	std::cout << *up << std::endl;

	std::unique_ptr<int> other = std::move(up);

	// std::cout << up << std::endl;

	std::cout << *other << std::endl;

	std::cout << (up == NULL) << std::endl;

	return 0;
}