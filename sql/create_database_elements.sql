CREATE TABLE Users (
	personal_id		BIGINT			NOT NULL
									PRIMARY KEY,
	name 			VARCHAR(30) 	NOT NULL,
	surname 		VARCHAR(40) 	NOT NULL,
	email		 	VARCHAR(100),
	phone_number	VARCHAR(13)		CHECK (SUBSTR(phone_number, 1, 1) = '+'),
	city	 		VARCHAR(23) 	NOT NULL,
	street 			VARCHAR(85) 	NOT NULL,
	house 			SMALLINT 		NOT NULL
									CHECK (house > 0)
);

CREATE UNIQUE INDEX uniq_el ON Users(email);
CREATE UNIQUE INDEX uniq_tlf ON Users(phone_number);
CREATE INDEX surname_indx ON Users(surname);

CREATE TABLE Loan (
	loan_number	 		INTEGER		PRIMARY KEY
									CHECK (loan_number > 0),
	loan_amount 		INTEGER 	NOT NULL
									CHECK (loan_amount >= 0),
	interest_rate 		REAL		NOT NULL
									CHECK (interest_rate BETWEEN 0 AND 1) 
									DEFAULT 0.1,
	loan_initial_date 	DATE 		NOT NULL,
	loan_deadline_date 	DATE 		NOT NULL,
	payment_intervals 	INTEGER 	NOT NULL 
									DEFAULT 30
									CHECK (payment_intervals >= 0),
	paid	 			VARCHAR(4) 	NOT NULL 
									DEFAULT 'no'
									CHECK (paid IN ('yes', 'no'))
);

CREATE TABLE Connection (
	personal_id 		BIGINT 		NOT NULL,
	loan_number	 		INTEGER		NOT NULL
);

ALTER TABLE Connection
	ADD PRIMARY KEY (personal_id, loan_number);

CREATE TABLE Transaction (
	transaction_number 	SERIAL		PRIMARY KEY,
	sender_account	 	VARCHAR(5) 	NOT NULL,
	receiver_account 	VARCHAR(5) 	NOT NULL,
	money_amount 		INTEGER 	NOT NULL
									CHECK (money_amount >= 0),
	transaction_date 	TIMESTAMP 	NOT NULL
);

CREATE TABLE Account (
	account_id	 		VARCHAR(5) 	PRIMARY KEY,
	personal_id 		BIGINT 		NOT NULL
);
	
ALTER TABLE Transaction 
	ADD CONSTRAINT to_sender_account
	FOREIGN KEY (sender_account)
	REFERENCES Account (account_id)
	ON DELETE CASCADE 
	ON UPDATE RESTRICT;

ALTER TABLE Transaction 
	ADD CONSTRAINT to_receiver_account
	FOREIGN KEY (receiver_account)
	REFERENCES Account (account_id)
	ON DELETE CASCADE 
	ON UPDATE RESTRICT;

ALTER TABLE Account
	ADD CONSTRAINT to_account_user
	FOREIGN KEY (personal_id)
	REFERENCES Users (personal_id)
	ON DELETE CASCADE 
	ON UPDATE RESTRICT;
	
ALTER TABLE Connection
	ADD CONSTRAINT to_loan_number
	FOREIGN KEY (loan_number)
	REFERENCES Loan (loan_number)
	ON DELETE CASCADE 
	ON UPDATE RESTRICT;

ALTER TABLE Connection
	ADD CONSTRAINT to_loan_user
	FOREIGN KEY (personal_id)
	REFERENCES Users (personal_id)
	ON DELETE CASCADE 
	ON UPDATE RESTRICT;

CREATE VIEW Unpaid_Loans (
	loan_number,
	loan_initial_date,
	loan_deadline_date,
	paid) 
	AS SELECT 
		loan_number,
		loan_initial_date,
		loan_deadline_date,
		paid
	FROM Loan
	WHERE loan_deadline_date <= CURRENT_DATE AND paid = 'no'
	WITH CHECK OPTION;

CREATE VIEW Foreign_Transactions (
	transaction_number,
	personal_id, 
	sender_account, 
	receiver_account)
	AS SELECT 
		transaction_number,
		personal_id, 
		sender_account, 
		receiver_account
	FROM Transaction
	JOIN Account 
		ON sender_account = account_id
	WHERE SUBSTR(receiver_account, 1, 2) <> 'LT';

CREATE MATERIALIZED VIEW Loan_Profit AS
	SELECT
		SUM(loan_amount * interest_rate) AS "profit_from_loans"
	FROM Loan
	WHERE paid = 'yes';

/*
	Update of the materialized view table.
*/
REFRESH MATERIALIZED VIEW Loan_Profit;

/*
	Checking whether a single person has no more
	that two accounts.  
*/
CREATE FUNCTION check_account_amount()
RETURNS TRIGGER AS $$
BEGIN
	IF (
        	SELECT COUNT(*)
		FROM Account
        	WHERE personal_id = NEW.personal_id
    	) >= 2 THEN
        	RAISE EXCEPTION 'It is possible to have only two account at maximum.';
    	END IF;
    	RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_account_amount_trigger
	BEFORE INSERT ON Account
	FOR EACH ROW
	EXECUTE FUNCTION check_account_amount();
	
/*
	Checking whether there is no current transaction
	with money amount more than 10000 to foreign
	bank accounts. 
*/
CREATE FUNCTION check_receiver_account()
RETURNS TRIGGER AS $$
BEGIN
    	IF SUBSTR(NEW.receiver_account, 1, 2) != 'LT' THEN
        	IF NEW.money_amount > 10000 THEN
            		RAISE EXCEPTION 'Transactions to foreign bank accounts cannot exceed 10000.';
        	END IF;
    	END IF;
    	RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_receiver_account_trigger
	BEFORE INSERT ON Transaction
	FOR EACH ROW
	EXECUTE FUNCTION check_receiver_account();
	
/*
CREATE FUNCTION insert_into_connection()
RETURNS TRIGGER AS $$
BEGIN
    	IF (TG_OP = 'INSERT') THEN
        	INSERT INTO Connection (personal_id, loan_number)
        		VALUES (NEW.personal_id, NEW.loan_number);
        ELSIF (TG_OP = 'UPDATE') THEN
        	INSERT INTO Connection (personal_id, loan_number)
        		VALUES (NEW.personal_id, NEW.loan_number);
        	DELETE FROM Connection 
        		WHERE personal_id = OLD.personal_id 
        		AND loan_number = OLD.loan_number;
    	ELSIF (TG_OP = 'DELETE') THEN
        	DELETE FROM Connection 
        		WHERE personal_id = OLD.personal_id 
        		AND loan_number = OLD.loan_number;
    	END IF;
    	RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER loan_trigger
	AFTER INSERT OR UPDATE OR DELETE ON Loan
	FOR EACH ROW
	EXECUTE FUNCTION insert_into_connection();
*/
