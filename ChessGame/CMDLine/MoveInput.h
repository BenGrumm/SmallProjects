#ifndef MOVE_INPUT_H
#define MOVE_INPUT_H

#include <iostream>

class MoveInput{

	public:
		MoveInput(){}
		friend std::istream& operator>>(std::istream& is, MoveInput& move);
		int x, y;

};

inline std::istream& operator>>(std::istream& is, MoveInput& move){
	char openingBracket, comma, closingBracket;

	std::cin >> openingBracket >> move.x >> comma >> move.y >> closingBracket;

	return is;
}

inline std::ostream& operator<<(std::ostream& os, MoveInput& move){
	os << "(" << move.x << ", " << move.y << ")";
	return os;
}

#endif