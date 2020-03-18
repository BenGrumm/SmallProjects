SET Search_path to CCABooking, public;

CREATE OR REPLACE FUNCTION create_flight_booking(INTEGER, INTEGER, INTEGER, INTEGER) RETURNS VOID 
AS $$

	INSERT INTO FlightBooking (BookingID, CustomerID, FlightID, NumSeats, BookingTime, TotalCost) 
	VALUES ($1, $2, $3, $4, current_timestamp, ((SELECT PricePerSeat FROM Flight WHERE Flight.FlightID = $3) * $4));

$$
LANGUAGE SQL;

INSERT INTO Flight VALUES (0001, '2020-02-20 07:00:00', 'Norwich', 'London', 200, 70);
INSERT INTO Flight VALUES (0002, '2020-02-20 08:00:00', 'Norwich', 'Belfast', 50, 100);
INSERT INTO Flight VALUES (0003, '2020-02-20 09:00:00', 'Norwich', 'Dublin', 100, 90);
INSERT INTO Flight VALUES (0004, '2020-02-20 10:00:00', 'Norwich', 'Paris', 200, 70);
INSERT INTO Flight VALUES (0005, '2020-02-20 11:00:00', 'Norwich', 'Manchester', 150, 40);
INSERT INTO Flight VALUES (0006, '2020-02-20 12:00:00', 'Norwich', 'Cardif', 100, 30);
INSERT INTO Flight VALUES (0007, '2020-02-20 08:00:00', 'Norwich', 'Edinburgh', 200, 80);

INSERT INTO LeadCustomer VALUES (0001, 'James', 'Cornwall', '123 Road Way', 'JCornwall@gmail.com');
INSERT INTO LeadCustomer VALUES (0002, 'John', 'Raxley', '124 Road Way', 'JRaxley@gmail.com');
INSERT INTO LeadCustomer VALUES (0003, 'Amy', 'Coolper', '125 Road Way', 'ACoolper@gmail.com');
INSERT INTO LeadCustomer VALUES (0004, 'Peter', 'Trisnem', '126 Road Way', 'PTrisnem@gmail.com');
INSERT INTO LeadCustomer VALUES (0005, 'Lucy', 'Rilexum', '127 Road Way', 'LRilexum@gmail.com');
INSERT INTO LeadCustomer VALUES (0006, 'Alan', 'Petron', '35 Lone Road', 'APetron@gmail.com');

INSERT INTO Passenger VALUES (0001, 'James', 'Cornwall', 'X4FC3C42F', 'British', '1980-04-12');
INSERT INTO Passenger VALUES (0002, 'Ashley', 'Barnes', 'CSE4234FSFJ', 'Canadian', '1993-02-03');
INSERT INTO Passenger VALUES (0003, 'Richard', 'Igalo', 'DSFKJ21323FDS', 'Irish', '1963-12-28');
INSERT INTO Passenger VALUES (0004, 'Lucy', 'Rilexum', 'DSND34SFKJ42', 'British', '1978-07-18');
INSERT INTO Passenger VALUES (0005, 'Alex', 'Privalem', 'FD4345SDKJF3', 'French', '1998-04-04');
INSERT INTO Passenger VALUES (0006, 'James', 'Praxley', 'SJKLF4322SA', 'British', '1990-02-28');
INSERT INTO Passenger VALUES (0007, 'Julie', 'Litmen', 'SDHJ2312KLSJD', 'American', '1966-02-12');


-- INSERT INTO FlighBooking VALUES (0001, 0001, 0001, 3, 'R', '2020-02-19 15:12:32', 210);
-- Booking ID - Customer ID / LeadCustomer ID - Flight ID - Number Of Seats
SELECT create_flight_booking(0001, 0001, 0001, 3);
SELECT create_flight_booking(0002, 0002, 0002, 4);
SELECT create_flight_booking(0003, 0003, 0003, 1);
SELECT create_flight_booking(0004, 0004, 0006, 2);
SELECT create_flight_booking(0005, 0005, 0003, 3);
SELECT create_flight_booking(0006, 0007, 0005, 2);

-- Booking ID - Passenger ID - Seat Number
INSERT INTO SeatBooking VALUES (0001, 0001, 85);
INSERT INTO SeatBooking VALUES (0002, 0002, 3);
INSERT INTO SeatBooking VALUES (0003, 0003, 12);
INSERT INTO SeatBooking VALUES (0003, 0004, 34);
INSERT INTO SeatBooking VALUES (0004, 0005, 44);
INSERT INTO SeatBooking VALUES (0005, 0005, 93);
INSERT INTO SeatBooking VALUES (0006, 0006, 1);
INSERT INTO SeatBooking VALUES (0006, 0007, 2);