all: cmdgame

cmdgame: Game.o Board.o Diagonal.o HorizontalVertical.o Pawn.o PieceType.o cmdmain.o
	g++ CMDLine/cmdmain.o Chess/Game.o Chess/Board.o Chess/Pieces/Diagonal.o Chess/Pieces/HorizontalVertical.o Chess/Pieces/Pawn.o Chess/Pieces/PieceType.o -o CMDLine/cmdgame

cmdmain.o:
	g++ -c CMDLine/main.cpp -o CMDLine/cmdmain.o

# chessgame.o: Game.o Board.o Diagonal.o HorizontalVertical.o Pawn.o PieceType.o
# 	g++ Chess/Game.o Chess/Board.o Chess/Pieces/Diagonal.o Chess/Pieces/HorizontalVertical.o Chess/Pieces/Pawn.o Chess/Pieces/PieceType.o -o Chess/chessgame.o

Game.o:
	g++ -c Chess/Game.cpp -o Chess/Game.o

Board.o:
	g++ -c Chess/Board.cpp -o Chess/Board.o

# Pieces.o: Diagonal.o HorizontalVertical.o Pawn.o PieceType.o
# 	g++ -c Chess/Pieces/Diagonal.o Chess/Pieces/HorizontalVertical.o Chess/Pieces/Pawn.o Chess/Pieces/PieceType.o -o Chess/Pieces/Pieces.o

Diagonal.o:
	g++ -c Chess/Pieces/Diagonal.cpp -o Chess/Pieces/Diagonal.o

HorizontalVertical.o:
	g++ -c Chess/Pieces/HorizontalVertical.cpp -o Chess/Pieces/HorizontalVertical.o

Pawn.o:
	g++ -c Chess/Pieces/Pawn.cpp -o Chess/Pieces/Pawn.o

PieceType.o:
	g++ -c Chess/Pieces/PieceType.cpp -o Chess/Pieces/PieceType.o

clean:
	rm CMDLine/*.o Chess/*.o Chess/Pieces/*.o