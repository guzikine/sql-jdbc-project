-- Insert data into Users table
INSERT INTO Users (
	personal_id, 
	name, 
	surname, 
	email, 
	phone_number, 
	city, 
	street, 
	house)
VALUES
 	(12345678901, 'John', 'Doe', 'john.doe@example.com', '+1234567890', 'City 1', 'Street 1', 1),
	(23456789012, 'Jane', 'Smith', 'jane.smith@example.com', '+9876543210', 'City 2', 'Street 2', 2),
	(34567890123, 'Alice', 'Johnson', 'alice.johnson@example.com', '+1112223334', 'City 3', 'Street 3', 3),
	(45678901234, 'Bob', 'Williams', 'bob.williams@example.com', '+5554443332', 'City 4', 'Street 4', 4),
	(56789012345, 'Emma', 'Brown', 'emma.brown@example.com', '+7778889990', 'City 5', 'Street 5', 5),
	(67890123456, 'Michael', 'Davis', 'michael.davis@example.com', '+9990001112', 'City 6', 'Street 6', 6);

-- Insert data into Loan table
INSERT INTO Loan (
	loan_number, 
	loan_amount, 
	interest_rate, 
	loan_initial_date, 
	loan_deadline_date, 
	payment_intervals, 
	paid)
VALUES
	(1, 10000, 0.1, '2023-01-01', '2023-06-30', 30, 'no'),
	(2, 20000, 0.2, '2023-02-01', '2023-04-29', 15, 'yes'),
	(3, 30000, 0.3, '2023-02-09', '2023-04-30', 30, 'yes'),
	(4, 40000, 0.4, '2023-05-01', '2023-10-31', 15, 'no'),
	(5, 50000, 0.5, '2022-06-01', '2023-04-08', 30, 'no'),
	(6, 60000, 0.6, '2023-06-01', '2023-11-30', 30, 'no');

-- Insert data into Connection table
INSERT INTO Connection (
	personal_id, 
	loan_number)
VALUES
	(12345678901, 1),
	(23456789012, 2),
	(34567890123, 3),
	(45678901234, 4),
	(56789012345, 5),
	(67890123456, 6);

-- Insert data into Account table
INSERT INTO Account (
	account_id, 
	personal_id)
VALUES
	('LT001', 12345678901),
	('LT002', 12345678901),
	('LT003', 34567890123),
	('LT004', 45678901234),
	('UK005', 56789012345),
	('LT006', 67890123456);

-- Insert data into Transaction table
INSERT INTO Transaction ( 
	sender_account, 
	receiver_account, 
	money_amount, 
	transaction_date)
VALUES
	('LT001', 'LT002', 100, '2022-01-01 12:00:00'),
	('LT004', 'UK005', 100, '2022-02-02 12:00:00'),
	('LT003', 'LT001', 100, '2022-03-03 12:00:00');
