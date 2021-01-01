#include <iostream>

#include "Game.h"
#include "Board.h"
#include "Pieces/Pawn.h"
#include "MoveInput.h"

using namespace std;

int main(int argc, char const *argv[])
{

	Game* g = new Game();
	
	MoveInput moveFrom;
	MoveInput moveTo;

	while(true){
		moveFrom = MoveInput();
		moveTo = MoveInput();

		cout << *g << endl;

		cout << g->whosTurn() << "s turn entry position of piece to move - (x, y): "  << endl;

		cin >> moveFrom;

		cout << endl << g->whosTurn() << "s turn entee position to move piece to - (x, y): " << endl;

		cin >> moveTo;
	
		if(g->movePiece(moveFrom.x, moveFrom.y, moveTo.x, moveTo.y)){
			cout << endl << "Piece Moved" << endl;
		}else{
			cout << endl << "Error Moving Piece" << endl;
		}
	}
	
	return 0;
}