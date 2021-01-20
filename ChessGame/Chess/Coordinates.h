#ifndef COORDINATES_H
#define COORDINATES_H

#include <iostream>

struct Coordinates
{
	int x;
	int y;
};

inline std::istream& operator>>(std::istream& is, Coordinates& pos){
	char openingBracket, comma, closingBracket;

	std::cin >> openingBracket >> pos.x >> comma >> pos.y >> closingBracket;

	return is;
}

inline std::ostream& operator<<(std::ostream& os, const Coordinates& pos){
	os << "(" << pos.x << ", " << pos.y << ")";
	return os;
}

inline bool operator==(const Coordinates& pos1, const Coordinates& pos2){
	return pos1.x == pos2.x && pos1.y == pos2.y;
}

#endif