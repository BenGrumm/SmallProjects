#include <iostream>

#include "../Chess/Game.h"
#include "../Chess/Coordinates.h"

using namespace std;

int main(int argc, char const *argv[])
{

	Game* g = new Game();
	
	Coordinates moveFrom;
	Coordinates moveTo;

	while(true){
		moveFrom = Coordinates{};
		moveTo = Coordinates{};

		cout << *g << endl;

		cout << g->whosTurn() << "s turn entry position of piece to move - (x, y): "  << endl;

		cin >> moveFrom;

		cout << endl << g->whosTurn() << "s turn entee position to move piece to - (x, y): " << endl;

		cin >> moveTo;
	
		if(g->movePiece(moveFrom, moveTo)){
			cout << endl << "Piece Moved" << endl;
		}else{
			cout << endl << "Error Moving Piece" << endl;
		}
	}
	
	return 0;
}