all: cmdgame

test: Game.o Board.o Diagonal.o HorizontalVertical.o Pawn.o Knight.o PieceType.o
	g++ testing.cpp Chess/Game.o Chess/Board.o Chess/Pieces/Diagonal.o Chess/Pieces/HorizontalVertical.o Chess/Pieces/Pawn.o Chess/Pieces/PieceType.o Chess/Pieces/Knight.o -o testing.exe

cmdgame: Game.o Board.o Diagonal.o HorizontalVertical.o Pawn.o Knight.o PieceType.o cmdmain.o
	g++ CMDLine/cmdmain.o Chess/Game.o Chess/Board.o Chess/Pieces/Diagonal.o Chess/Pieces/HorizontalVertical.o Chess/Pieces/Knight.o Chess/Pieces/Pawn.o Chess/Pieces/PieceType.o -o CMDLine/cmdgame.exe

cmdmain.o:
	g++ -c CMDLine/main.cpp -o CMDLine/cmdmain.o

Game.o:
	g++ -c Chess/Game.cpp -o Chess/Game.o

Board.o:
	g++ -c Chess/Board.cpp -o Chess/Board.o

Diagonal.o:
	g++ -c Chess/Pieces/Diagonal.cpp -o Chess/Pieces/Diagonal.o

HorizontalVertical.o:
	g++ -c Chess/Pieces/HorizontalVertical.cpp -o Chess/Pieces/HorizontalVertical.o

Pawn.o:
	g++ -c Chess/Pieces/Pawn.cpp -o Chess/Pieces/Pawn.o

Knight.o:
	g++ -c Chess/Pieces/Knight.cpp -o Chess/Pieces/Knight.o

PieceType.o:
	g++ -c Chess/Pieces/PieceType.cpp -o Chess/Pieces/PieceType.o

clean:
	rm CMDLine/*.o Chess/*.o Chess/Pieces/*.o *.exe CMDLine/*.exe