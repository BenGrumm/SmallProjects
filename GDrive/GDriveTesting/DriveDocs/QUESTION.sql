SET Search_path TO CCABooking, public;


-- 1. Insert a new record. This could be
-- 			a. Given a lead customer ID number name, and contact details, create a new
-- 				customer record.
-- 			b. Given a flight ID number, origin, destination, flight date, capacity of the
-- 				aircraft, and price per seat create a new flight record.
-- 			c. Given a passenger with an ID, name date of birth, etc., create a new
-- 				passenger record.

-- a.
-- 	CustomerID
-- 	FirstName
-- 	Surname
-- 	BillingAddress
-- 	email
CREATE OR REPLACE FUNCTION insert_lead_customer(INTEGER, TEXT, TEXT, TEXT, TEXT) RETURNS VOID
AS $$

	INSERT INTO LeadCustomer VALUES ($1, $2, $3, $4, $5);

$$
LANGUAGE SQL;

-- b.
-- 	FlightID
-- 	FlightDate
-- 	Origin
-- 	Destination 
-- 	MaxCapacity 
-- 	PricePerSeat
CREATE OR REPLACE FUNCTION insert_flight(INTEGER, TIMESTAMP, TEXT, TEXT, INTEGER, DECIMAL) RETURNS VOID
AS $$

	INSERT INTO Flight VALUES ($1, $2, $3, $4, $5, $6);

$$
LANGUAGE SQL;

-- c.
-- 	PassengerID
-- 	FirstName
-- 	Surname
-- 	PassportNo 
-- 	Nationality
-- 	Dob
CREATE OR REPLACE FUNCTION insert_passenger(INTEGER, TEXT, TEXT, TEXT, TEXT, DATE) RETURNS VOID
AS $$

	INSERT INTO Passenger VALUES ($1, $2, $3, $4, $5, $6);

$$
LANGUAGE SQL;

-- 2. Given a customer ID number, remove the record for that customer. It should not be
-- 	  possible to remove customers that have active (i.e. reserved) flight bookings. A
-- 	  customer that has only cancelled bookings could be removed; the associated
-- 	  bookings should also be removed along with all the seat bookings.
-- CREATE LANGUAGE PLPGSQL;
CREATE OR REPLACE FUNCTION remove_customer(INTEGER) RETURNS VOID
AS $$

	BEGIN

		IF (SELECT count(*) FROM FlightBooking WHERE CustomerID = $1 AND Status = 'R') = 0 THEN
			DELETE FROM LeadCustomer WHERE LeadCustomer.CustomerID = $1; 
		END IF;

	END;

$$
LANGUAGE PGPSQL;

-- 3. Check the availability of seats on flights by showing the flight ID number, flight date
-- 	  along with the number of booked seats, number of available seats and maximum
-- 	  capacity. As a minimum you should be able check availability for all flights. Better
-- 	  solutions will be able to seach for a specific flight by flightID; by destination; or by
-- 	  date. Note that seats are booked (not available) as soon as the flight booking is
-- 	  entered and regardless of whether they have been associated with a particular
-- 	  passenger.

-- CHECK ALL
SELECT Flight.FLightID, Flight.FlightDate, Sum(COALESCE(FlightBooking.NumSeats, 0)) AS NumBooked, (Flight.MaxCapacity - Sum(COALESCE(FlightBooking.NumSeats, 0))) AS RemainingSeats, Flight.MaxCapacity
FROM Flight LEFT JOIN FlightBooking ON Flight.FlightID = FlightBooking.FlightID
GROUP BY Flight.FlightID;

-- CHECK BY DESTINATION
SELECT Flight.FLightID, Flight.FlightDate, Sum(COALESCE(FlightBooking.NumSeats, 0)) AS NumBooked, (Flight.MaxCapacity - Sum(COALESCE(FlightBooking.NumSeats, 0))) AS RemainingSeats, Flight.MaxCapacity
FROM Flight LEFT JOIN FlightBooking ON Flight.FlightID = FlightBooking.FlightID
WHERE Flight.Destination = ''
GROUP BY Flight.FlightID;

-- CHECK BY Flight ID
SELECT Flight.FLightID, Flight.FlightDate, Sum(COALESCE(FlightBooking.NumSeats, 0)) AS NumBooked, (Flight.MaxCapacity - Sum(COALESCE(FlightBooking.NumSeats, 0))) AS RemainingSeats, Flight.MaxCapacity
FROM Flight LEFT JOIN FlightBooking ON Flight.FlightID = FlightBooking.FlightID
WHERE Flight.FlightID = 1
GROUP BY Flight.FlightID;

-- CHECK BY Flight Date
SELECT Flight.FLightID, Flight.FlightDate, Sum(COALESCE(FlightBooking.NumSeats, 0)) AS NumBooked, (Flight.MaxCapacity - Sum(COALESCE(FlightBooking.NumSeats, 0))) AS RemainingSeats, Flight.MaxCapacity
FROM Flight LEFT JOIN FlightBooking ON Flight.FlightID = FlightBooking.FlightID
WHERE Flight.FlightDate = ''
GROUP BY Flight.FlightID;

-- 4. Given a flight ID number, check the status of all seats currently allocated to that
-- 	  flight, i.e. return the total number of reserved/ cancelled/ available seats.

-- DROP TYPE FlightInfo CASCADE;
CREATE TYPE FlightInfo AS (

	reserved INTEGER,
	cancelled INTEGER,
	available INTEGER

);

CREATE OR REPLACE FUNCTION get_num_seates(INTEGER, StatusType) RETURNS INTEGER
AS $$

	SELECT COALESCE(Sum(FlightBooking.NumSeats), 0)::INTEGER FROM FlightBooking WHERE FlightBooking.FlightID = $1 AND FlightBooking.Status = $2;

$$

CREATE OR REPLACE FUNCTION check_flight_status(INTEGER) RETURNS FlightInfo
AS $$

	SELECT  get_num_seates($1, 'R') AS NumReserverd,
		    get_num_seates($1, 'C') AS NumCancelled, 
		    (SELECT (Flight.MaxCapacity - Sum(COALESCE(FlightBooking.NumSeats, 0))) AS NumBooked
			FROM Flight LEFT JOIN FlightBooking ON Flight.FlightID = FlightBooking.FlightID
			WHERE Flight.FlightID = $1 AND FlightBooking.Status = 'R'
			GROUP BY Flight.FlightID)::INTEGER AS NumLeft;

$$
LANGUAGE SQL;

SELECT * FROM check_flight_status(2);

--5. Produce a ranked list of all lead customers, showing their ID, their full name, the
--	 total number of bookings made and the total spend made for all bookings. The list
--	 should be sorted by decreasing total value. 

SELECT * FROM (SELECT LeadCustomer.CustomerID, LeadCustomer.FirstName, LeadCustomer.Surname, Count(*) AS NumberBookings, Sum(FlightBooking.NumSeats * Flight.PricePerSeat) AS totalSpend
				FROM 	LeadCustomer 
						JOIN FlightBooking 
						ON LeadCustomer.CustomerID = FlightBooking.CustomerID
						JOIN Flight 
						ON FlightBooking.FlightID = Flight.FlightID
				GROUP BY LeadCustomer.CustomerID
		
			UNION
		
				SELECT LeadCustomer.CustomerID, LeadCustomer.FirstName, LeadCustomer.Surname, 0, 0
				FROM LeadCustomer
				WHERE LeadCustomer.CustomerID NOT IN (SELECT LeadCustomer.CustomerID FROM LeadCustomer JOIN FlightBooking ON LeadCustomer.CustomerID = FlightBooking.CustomerID) 
	    		GROUP BY LeadCustomer.CustomerID) AS allVeh ORDER BY totalSpend;

--6. Create a new flight booking. This procedure can be broken down into a number of
-- 	 steps as follows:
-- 	 a. Provide a facility to search for existing customers (as a minimum by
-- 	    customerid but better solutions may provide other search options, e.g. by
-- 	    surname). If lead customer exists, we can use existing record. If new, we can
-- 	    enter in the database using query developed for 1a.
-- 	 b. Enter flight booking as requested, including total cost for the booking. The
-- 	    entering of a new booking should work as an atomic operation so either the
-- 	    whole booking (insert of lead customer if required, insert of flight booking
-- 	    itself) succeeds or it fails. Hence lead customers should not be added if the
-- 	    flight booking does not get added. If any problems occur during the booking
-- 	    (e.g. the seats are not available or any of the insert fails) the booking should
-- 	    be cancelled and all of the operations undone.

CREATE OR REPLACE FUNCTION book_flight(INTEGER) RETURNS VOID
AS $$

	BEGIN

		IF NOT customer_exists($1) THEN
			 
			 -- Need Customer Info insert_lead_customer();

		END IF;

	END;

$$
LANGUAGE PGPSQL;

CREATE OR REPLACE FUNCTION customer_exists(INTEGER) RETURNS BOOLEAN
AS $$

	SELECT (SELECT Count(*) FROM LeadCustomer WHERE CustomerID = $1) > 0;

$$
LANGUAGE SQL;

BEGIN;

COMMIT;

--7. Allocate seats to each passenger associated with a booking. Passengers need to be
--   entered using the query developed in 1 c. There should be no more seat bookings
--   that those associated with the flight booking, e.g. if the flight booking was for 3
--   passengers only 3 seats should be allocated to 3 passengers.

CREATE OR REPLACE FUNCTION allocate_seats(INTEGER) RETURNS VOID
AS $$

	BEGIN

		-- FOR SEAT IN QUERY LOOP
		-- 

		FOR counter IN 1...MaxCapacity LOOP
			IF (SELECT count(*) FROM  WHERE FlightID = givenflightid AND SeatNumber = counter) = 0:
				-- Assign Seat

	END;

$$
LANGUAGE PGPSQL;

--8. Given a booking ID number, cancel the booking. Note that cancelling a booking only
--   changes the status and should not delete the historical details of the original
--   booking. However, cancelled seats should be viewed as available.

SELECT * FROM FlightBooking 

CREATE OR REPLACE FUNCTION cancel_booking(INTEGER) RETURNS VOID
AS $$

	UPDATE FlightBooking SET Status = 'C' WHERE BookingID = $1;
	DELETE FROM SeatBooking WHERE BookingID = $1;

$$
LANGUAGE SQL;




























