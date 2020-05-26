CREATE TABLE Departments(
	dep_id int PRIMARY KEY,
	dep_name varchar(51)
);

CREATE TABLE BenefTypes(
	type_id int PRIMARY KEY,
	type varchar(20)
);

CREATE TABLE FlightInfo(
	dep_airport_code varchar(3),
	arr_airport_code varchar(3),
	flight_time decimal,
	distance decimal
);

CREATE TABLE Transactions (
	traveler_id int,
	booking_num varchar(8),
	dep_airport_code varchar(3),
	arr_airport_code varchar(3),
	dep_time timestamp,
	ret_time timestamp,
	booking_time timestamp,
	line varchar(8)
);

CREATE TABLE Employees(
	staff_number int PRIMARY KEY,
	name varchar(36),
	gender int,
	dob date,
	hire date,
	status int,
	dep_id int,
	FOREIGN KEY (dep_id) REFERENCES Departments (dep_id)
);

CREATE TABLE Beneficiaries(
	benef_id int PRIMARY KEY,
	name varchar(256),
	gender int,
	dob date,
	is_active int,
	type_id int,
	staff_number int,
	FOREIGN KEY (type_id) REFERENCES BenefTypes (type_id),
	FOREIGN KEY (staff_number) REFERENCES Employees (staff_number)
);
