----------------------------------------
-- Name: Sample Student
-- PittID:
----------------------------------------


----------------------------------------
--- Sample solution for Question #1  ---
----------------------------------------

--CREATE DOMAIN FOR EMAIL ATTRIBUTE
DROP DOMAIN IF EXISTS EMAIL_DOMAIN CASCADE;
CREATE DOMAIN EMAIL_DOMAIN AS varchar(30) CHECK (VALUE ~ '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$');
--CREATE DOMAIN FOR MUTUAL FUND CATEGORY
DROP DOMAIN IF EXISTS CATEGORY_DOMAIN CASCADE;
CREATE DOMAIN CATEGORY_DOMAIN AS varchar(10) CHECK (VALUE IN ('fixed', 'bonds', 'mixed', 'stocks'));
--CREATE DOMAIN FOR ACTION CATEGORY
DROP DOMAIN IF EXISTS ACTION_DOMAIN CASCADE;
CREATE DOMAIN ACTION_DOMAIN AS varchar(10) CHECK (VALUE IN ('deposit', 'buy', 'sell'));

/**********************************************************************************
                        Assumptions:
1) Assume that Customers and Administrators are disjoint.
- That is, no administrator is a customer, vice versa.
2) Assume 'Name' from the Administrator and Customer tables are the Full Name.
3) Assume 'Balance' cannot be negative.
4) Assume that Emails can come from different websites.
- Ex: gol6@pitt.edu, gol6@cmu.edu
5) Assume customers and administrators can come from the same household.
6) Assume 'Password' can be the same for different users
- Ex: abcdefg
7) Assume that p_date in Allocation and Closing_Price tables are unrelated.
8) For the Closing_Price table, assume that price is recorded every day.
**********************************************************************************/

--Drop all tables to make sure the Schema is clear!
DROP TABLE IF EXISTS MUTUAL_DATE CASCADE;
DROP TABLE IF EXISTS CUSTOMER CASCADE;
DROP TABLE IF EXISTS ADMINISTRATOR CASCADE;
DROP TABLE IF EXISTS MUTUAL_FUND CASCADE;
DROP TABLE IF EXISTS OWNS CASCADE;
DROP TABLE IF EXISTS TRXLOG CASCADE;
DROP TABLE IF EXISTS ALLOCATION CASCADE;
DROP TABLE IF EXISTS PREFERS CASCADE;
DROP TABLE IF EXISTS CLOSING_PRICE CASCADE;

---CREATING MUTUAL DATE TABLE
-- The c_date is initialized once using INSERT and updated subsequently
CREATE TABLE MUTUAL_DATE
(
    p_date date,
    CONSTRAINT S_DATE_PK PRIMARY KEY (p_date)
);


---CREATING CUSTOMER TABLE
-- Assume emails are unique -> no two users registered on the same site can share an email address
CREATE TABLE CUSTOMER
(
    login    varchar(10),
    name     varchar(20)    NOT NULL,
    email    EMAIL_DOMAIN,
    address  varchar(30)    NOT NULL,
    password varchar(10)    NOT NULL,
    balance  decimal(10, 2) NOT NULL,
    CONSTRAINT LOGIN_PK PRIMARY KEY (login),
    CONSTRAINT EMAIL_UNIQUE UNIQUE (email),
    CONSTRAINT BALANCE_CK CHECK ( balance >= 0 )
);

---CREATING ADMINISTRATOR TABLE
CREATE TABLE ADMINISTRATOR
(
    login    varchar(10),
    name     varchar(20) NOT NULL,
    email    EMAIL_DOMAIN,
    address  varchar(30) NOT NULL,
    password varchar(10) NOT NULL,
    CONSTRAINT ADMIN_LOGIN_PK PRIMARY KEY (login),
    CONSTRAINT ADMIN_EMAIL_UNIQUE UNIQUE (email)
);

---CREATING MUTUAL FUND TABLE
--Assume p_date from MutualDate is not same as c_date in MutualFund table
CREATE TABLE MUTUAL_FUND
(
    symbol      varchar(20),
    name        varchar(30)  NOT NULL,
    description varchar(100) NOT NULL,
    category    CATEGORY_DOMAIN,
    c_date      date         NOT NULL,
    CONSTRAINT MUTUAL_FUND_PK PRIMARY KEY (symbol),
    CONSTRAINT MF_NAME_UQ UNIQUE (name),
    CONSTRAINT MF_DESC_UQ UNIQUE (description)
);

---CREATING OWNS TABLE
CREATE TABLE OWNS
(
    login  varchar(10),
    symbol varchar(20),
    shares integer NOT NULL,
    CONSTRAINT OWNS_PK PRIMARY KEY (login, symbol),
    CONSTRAINT LOGIN_FK FOREIGN KEY (login) REFERENCES CUSTOMER (login),
    CONSTRAINT SYMBOL_FK FOREIGN KEY (symbol) REFERENCES MUTUAL_FUND (symbol),
    CONSTRAINT SHARES_CK CHECK ( shares > 0 )
);

---CREATING TRXLOG TABLE
--Symbol/Shares/Price can be null if the user simply wants to deposit
--No need to make a FK to PK from CLosingPrice or Allocation, in case of a deposit
CREATE TABLE TRXLOG
(
    trx_id     serial,
    login      varchar(10)    NOT NULL,
    symbol     varchar(20),
    t_date     date           NOT NULL,
    action     ACTION_DOMAIN,
    num_shares integer,
    price      decimal(10, 2),
    amount     decimal(10, 2) NOT NULL,
    CONSTRAINT TRXLOG_PK PRIMARY KEY (trx_id),
    CONSTRAINT LOGIN_FK FOREIGN KEY (login) REFERENCES CUSTOMER (login),
    CONSTRAINT SYMBOL_FK FOREIGN KEY (symbol) REFERENCES MUTUAL_FUND (symbol),
    CONSTRAINT AMOUNT_CK CHECK ( amount > 0),
    CONSTRAINT NUM_SHARES_CK CHECK ( num_shares > 0),
    CONSTRAINT PRICE_CK CHECK ( price > 0)
);

---CREATING ALLOCATION TABLE
CREATE TABLE ALLOCATION
(
    allocation_no integer,
    login         varchar(10) NOT NULL,
    p_date        date        NOT NULL, --processing date
    CONSTRAINT ALLOCATION_PK PRIMARY KEY (allocation_no),
    CONSTRAINT ALLOC_LOGIN_FK FOREIGN KEY (login) REFERENCES CUSTOMER (login)
);

---CREATING PREFERS TABLE
CREATE TABLE PREFERS
(
    allocation_no integer     NOT NULL,
    symbol        varchar(20) NOT NULL,
    percentage    decimal(3, 2)       NOT NULL,
    CONSTRAINT PREFERS_PK PRIMARY KEY (allocation_no, symbol),
    CONSTRAINT PREFERS_ALLOCATION_NO_FK FOREIGN KEY (allocation_no) REFERENCES ALLOCATION (allocation_no),
    CONSTRAINT PREFERS_ALLOCATION_SYMBOL_FK FOREIGN KEY (symbol) REFERENCES MUTUAL_FUND (symbol),
    CONSTRAINT PERCENTAGE_CK CHECK ( percentage > 0)
);

---CREATING CLOSING_PRICE TABLE
CREATE TABLE CLOSING_PRICE
(
    symbol varchar(20) NOT NULL,
    price  decimal(10, 2)       NOT NULL,
    p_date date        NOT NULL, --processing date
    CONSTRAINT CLOSING_PRICE_PK PRIMARY KEY (symbol, p_date),
    CONSTRAINT CLOSING_PRICE_SYMBOL_FK FOREIGN KEY (symbol) REFERENCES MUTUAL_FUND (symbol),
    CONSTRAINT CLOSING_PRICE_CK CHECK ( price > 0)
);

----------------------------------------
--- Sample Data  for Assignment #5   ---
----------------------------------------

----------------------------------------
-- Name: Sample Student
-- PittID:
----------------------------------------

--INSERT VALUES INTO THE MUTUAL DATE TABLE
INSERT INTO MUTUAL_DATE (p_date)
VALUES (TO_DATE('04-APR-20', 'DD-MON-YY'));


--INSERT VALUES INTO THE CUSTOMER TABLE
INSERT INTO CUSTOMER
VALUES ('mike', 'Mike Costa', 'mike@betterfuture.com', '1st street', 'pwd', 750);
INSERT INTO CUSTOMER
VALUES ('mary', 'Mary Chrysanthis', 'mary@betterfuture.com', '2nd street', 'pwd', 0);

--INSERT VALUES INTO THE ADMINISTRATOR TABLE
INSERT INTO ADMINISTRATOR
VALUES ('admin', 'Administrator', 'admin@betterfuture.com', '5th Ave, Pitt', 'root');

--INSERT VALUES INTO THE MUTUAL FUND TABLE
INSERT INTO MUTUAL_FUND
VALUES ('MM', 'money-market', 'money market, conservative', 'fixed', TO_DATE('06-JAN-20', 'DD-MON-YY'));
INSERT INTO MUTUAL_FUND
VALUES ('RE', 'real-estate', 'real estate', 'fixed', TO_DATE('09-JAN-20', 'DD-MON-YY'));
INSERT INTO MUTUAL_FUND
VALUES ('STB', 'short-term-bonds', 'short term bonds', 'bonds', TO_DATE('10-JAN-20', 'DD-MON-YY'));
INSERT INTO MUTUAL_FUND
VALUES ('LTB', 'long-term-bonds', 'long term bonds', 'bonds', TO_DATE('11-JAN-20', 'DD-MON-YY'));
INSERT INTO MUTUAL_FUND
VALUES ('BBS', 'balance-bonds-stocks', 'balance bonds and stocks', 'mixed', TO_DATE('12-JAN-20', 'DD-MON-YY'));
INSERT INTO MUTUAL_FUND
VALUES ('SRBS', 'social-response-bonds-stocks', 'social responsibility and stocks', 'mixed',
        TO_DATE('12-JAN-20', 'DD-MON-YY'));
INSERT INTO MUTUAL_FUND
VALUES ('GS', 'general-stocks', 'general stocks', 'stocks', TO_DATE('16-JAN-20', 'DD-MON-YY'));
INSERT INTO MUTUAL_FUND
VALUES ('AS', 'aggressive-stocks', 'aggressive stocks', 'stocks', TO_DATE('23-JAN-20', 'DD-MON-YY'));
INSERT INTO MUTUAL_FUND
VALUES ('IMS', 'international-markets-stock', 'international markets stock, risky', 'stocks',
        TO_DATE('30-JAN-20', 'DD-MON-YY'));

--INSERT VALUES INTO THE OWNS TABLE
INSERT INTO OWNS
VALUES ('mike', 'RE', 50);
INSERT INTO OWNS
VALUES ('mike', 'MM', 15);

--INSERT VALUES INTO THE TRXLOG TABLE
INSERT INTO TRXLOG
VALUES (DEFAULT, 'mike', NULL, TO_DATE('29-MAR-20', 'DD-MON-YY'), 'deposit', NULL, NULL, 1000);
INSERT INTO TRXLOG
VALUES (DEFAULT, 'mike', 'MM', TO_DATE('29-MAR-20', 'DD-MON-YY'), 'buy', 50, 10, 500);
INSERT INTO TRXLOG
VALUES (DEFAULT, 'mike', 'RE', TO_DATE('29-MAR-20', 'DD-MON-YY'), 'buy', 50, 10, 500);
INSERT INTO TRXLOG
VALUES (DEFAULT, 'mike', 'MM', TO_DATE('01-APR-20', 'DD-MON-YY'), 'sell', 50, 15, 750);

--INSERT VALUES INTO THE ALLOCATION TABLE
INSERT INTO ALLOCATION
VALUES (0, 'mike', TO_DATE('28-MAR-20', 'DD-MON-YY'));
INSERT INTO ALLOCATION
VALUES (1, 'mary', TO_DATE('29-MAR-20', 'DD-MON-YY'));
INSERT INTO ALLOCATION
VALUES (2, 'mike', TO_DATE('03-MAR-20', 'DD-MON-YY'));

--INSERT VALUES INTO THE PREFERS TABLE
INSERT INTO PREFERS
VALUES (0, 'MM', .5);
INSERT INTO PREFERS
VALUES (0, 'RE', .5);
INSERT INTO PREFERS
VALUES (1, 'STB', .2);
INSERT INTO PREFERS
VALUES (1, 'LTB', .4);
INSERT INTO PREFERS
VALUES (1, 'BBS', .4);
INSERT INTO PREFERS
VALUES (2, 'GS', .3);
INSERT INTO PREFERS
VALUES (2, 'AS', .3);
INSERT INTO PREFERS
VALUES (2, 'IMS', .4);

--INSERT INTO THE CLOSING PRICE TABLE
INSERT INTO CLOSING_PRICE
VALUES ('MM', 10, TO_DATE('28-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('MM', 11, TO_DATE('29-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('MM', 12, TO_DATE('30-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('MM', 15, TO_DATE('31-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('MM', 14, TO_DATE('01-APR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('MM', 15, TO_DATE('02-APR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('MM', 16, TO_DATE('03-APR-20', 'DD-MON-YY'));

INSERT INTO CLOSING_PRICE
VALUES ('RE', 10, TO_DATE('28-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('RE', 12, TO_DATE('29-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('RE', 15, TO_DATE('30-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('RE', 14, TO_DATE('31-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('RE', 16, TO_DATE('01-APR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('RE', 17, TO_DATE('02-APR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('RE', 15, TO_DATE('03-APR-20', 'DD-MON-YY'));

INSERT INTO CLOSING_PRICE
VALUES ('STB', 10, TO_DATE('28-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('STB', 9, TO_DATE('29-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('STB', 10, TO_DATE('30-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('STB', 12, TO_DATE('31-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('STB', 14, TO_DATE('01-APR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('STB', 10, TO_DATE('02-APR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('STB', 12, TO_DATE('03-APR-20', 'DD-MON-YY'));

INSERT INTO CLOSING_PRICE
VALUES ('LTB', 10, TO_DATE('28-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('LTB', 12, TO_DATE('29-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('LTB', 13, TO_DATE('30-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('LTB', 15, TO_DATE('31-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('LTB', 12, TO_DATE('01-APR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('LTB', 9, TO_DATE('02-APR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('LTB', 10, TO_DATE('03-APR-20', 'DD-MON-YY'));

INSERT INTO CLOSING_PRICE
VALUES ('BBS', 10, TO_DATE('28-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('BBS', 11, TO_DATE('29-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('BBS', 14, TO_DATE('30-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('BBS', 18, TO_DATE('31-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('BBS', 13, TO_DATE('01-APR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('BBS', 15, TO_DATE('02-APR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('BBS', 16, TO_DATE('03-APR-20', 'DD-MON-YY'));

INSERT INTO CLOSING_PRICE
VALUES ('SRBS', 10, TO_DATE('28-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('SRBS', 12, TO_DATE('29-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('SRBS', 12, TO_DATE('30-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('SRBS', 14, TO_DATE('31-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('SRBS', 17, TO_DATE('01-APR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('SRBS', 20, TO_DATE('02-APR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('SRBS', 20, TO_DATE('03-APR-20', 'DD-MON-YY'));

INSERT INTO CLOSING_PRICE
VALUES ('GS', 10, TO_DATE('28-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('GS', 12, TO_DATE('29-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('GS', 13, TO_DATE('30-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('GS', 15, TO_DATE('31-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('GS', 14, TO_DATE('01-APR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('GS', 15, TO_DATE('02-APR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('GS', 12, TO_DATE('03-APR-20', 'DD-MON-YY'));

INSERT INTO CLOSING_PRICE
VALUES ('AS', 10, TO_DATE('28-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('AS', 15, TO_DATE('29-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('AS', 14, TO_DATE('30-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('AS', 16, TO_DATE('31-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('AS', 14, TO_DATE('01-APR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('AS', 17, TO_DATE('02-APR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('AS', 18, TO_DATE('03-APR-20', 'DD-MON-YY'));

INSERT INTO CLOSING_PRICE
VALUES ('IMS', 10, TO_DATE('28-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('IMS', 12, TO_DATE('29-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('IMS', 12, TO_DATE('30-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('IMS', 14, TO_DATE('31-MAR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('IMS', 13, TO_DATE('01-APR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('IMS', 12, TO_DATE('02-APR-20', 'DD-MON-YY'));
INSERT INTO CLOSING_PRICE
VALUES ('IMS', 11, TO_DATE('03-APR-20', 'DD-MON-YY'));

--Question 2:
DROP FUNCTION IF EXISTS search_mutual_funds(keyword_1 varchar(30), keyword_2 varchar(30));
CREATE OR REPLACE FUNCTION search_mutual_funds(keyword_1 varchar(30), keyword_2 varchar(30))
    RETURNS text AS
$$
DECLARE
    res text; -- local variable for string of symbols
    i   text; -- current record
BEGIN
    res := '';
    FOR i IN
        SELECT symbol
        FROM mutual_fund
        WHERE description LIKE '%' || keyword_1 || '%'
          AND description LIKE '%' || keyword_2 || '%'
        LOOP
            res := res || ',' || i;
        END LOOP;
    res := res || ']';
    res := ltrim(res, ',');  -- trim extra ',' at beginning
    res := '[' || res;
    return res;
END;
$$ LANGUAGE plpgsql;
--Sanity Check:
SELECT symbol
FROM mutual_fund
WHERE description LIKE CONCAT('%', 'bonds', '%')
  AND description LIKE CONCAT('%', 'term', '%');
--test function: should be like: [STB, LTB]
-- SELECT search_mutual_funds('bonds', 'term');

--Question 3:
CREATE OR REPLACE PROCEDURE deposit_for_investment(login varchar(10), deposit decimal(10, 2))
AS
$$
DECLARE
    mutual_date_value date;
    row_count         int;
    alloc_no          int;
    pref              record;
    percentage        decimal(10, 2);
    symbol_price      decimal(10, 2);
    amount_to_buy     decimal(10, 2);
    num_of_shares     int;
    total_amount      decimal(10, 2);
    txc_amount        decimal(10, 2);
    remaining         decimal(10, 2);
    suffient_amount   boolean;
BEGIN
    --  Get the current date
    SELECT p_date
    INTO mutual_date_value
    FROM MUTUAL_DATE
    ORDER BY p_date DESC
    LIMIT 1;

    -- Check if user exists
    SELECT count(*)
    INTO row_count
    FROM CUSTOMER
    WHERE CUSTOMER.login = deposit_for_investment.login; -- The name of the procedure is used as a prefix (i.e., scope)
    IF row_count = 0 THEN
        RAISE EXCEPTION 'User % not found.', login;
    END IF;

    --  Total amount of all transaction
    total_amount = 0;

    --  Find the newest allocation_no for the user
    SELECT ALLOCATION.allocation_no
    INTO alloc_no
    FROM ALLOCATION
    WHERE ALLOCATION.login = deposit_for_investment.login
    ORDER BY ALLOCATION.p_date DESC
    LIMIT 1;

    -- Check if the deposit is enough to buy all symbols in the allocation
    SELECT SUM(P.percentage * deposit) > SUM(CP.price)
    INTO suffient_amount
    FROM PREFERS P
             JOIN CLOSING_PRICE CP ON CP.symbol = P.symbol
             JOIN (SELECT CLOSING_PRICE.symbol, max(p_date) AS max_date
                   FROM CLOSING_PRICE
                   GROUP BY CLOSING_PRICE.symbol) AS MOST_RECENT_CP
                  ON cp.symbol = MOST_RECENT_CP.symbol AND CP.p_date = MOST_RECENT_CP.max_date
    WHERE allocation_no = alloc_no;

    IF not suffient_amount THEN -- FALSE if deposit is not enough for a symbol
        RAISE NOTICE 'Partial allocation purchase is not allowed. The amount of % will be deposited to the account.', deposit;
    ELSE
        -- Buy the shares
        FOR pref in (SELECT * FROM PREFERS WHERE allocation_no = alloc_no)
            LOOP
                percentage = pref.percentage;
                amount_to_buy = deposit * percentage;

                -- Find latest mutual fund price associated with the preference symbol
                SELECT CLOSING_PRICE.price
                INTO symbol_price
                FROM CLOSING_PRICE
                WHERE CLOSING_PRICE.symbol = pref.symbol
                ORDER BY CLOSING_PRICE.p_date DESC
                LIMIT 1;

                -- Number of shares that we have to buy for the user for this symbol
                num_of_shares = FLOOR(amount_to_buy / symbol_price);

                -- transaction total amount
                txc_amount = num_of_shares * symbol_price;
                total_amount = total_amount + txc_amount;

                -- The transaction id will be generated automatically the sequence 'trx_sequence'
                INSERT INTO TRXLOG(login, symbol, action, num_shares, price, amount, t_date)
                VALUES (deposit_for_investment.login, pref.symbol, 'buy', num_of_shares,
                        symbol_price, txc_amount, mutual_date_value);

                -- Check if the user already own some shares of this symbol
                SELECT count(*)
                INTO row_count
                FROM OWNS
                WHERE OWNS.login = deposit_for_investment.login
                  AND OWNS.symbol = pref.symbol;
                IF row_count = 0 THEN
                    -- Create a new row
                    INSERT INTO OWNS(login, symbol, shares)
                    VALUES (deposit_for_investment.login, pref.symbol, num_of_shares);
                ELSE
                    -- Update the existing row
                    UPDATE OWNS
                    SET shares = shares + num_of_shares
                    WHERE OWNS.login = deposit_for_investment.login
                      AND OWNS.symbol = pref.symbol;
                END IF;
            END LOOP;
    END IF;

    -- deposit the remaining amount to user's balance
    remaining = deposit - total_amount;
    UPDATE CUSTOMER
    SET balance = balance + remaining
    WHERE CUSTOMER.login = deposit_for_investment.login;
END;
$$ LANGUAGE PLPGSQL;

-- CALL deposit_for_investment('mary', 5);
-- CALL deposit_for_investment('mike', 100);

--Question 4:
CREATE OR REPLACE FUNCTION buy_shares(login varchar(10), symbol varchar(20), number_of_shares int)
    RETURNS BOOLEAN AS
$$
DECLARE
    mutual_date_value date;
    row_count         int;
    symbol_price      decimal(10, 2);
    customer_balance  decimal(10, 2);
BEGIN

    --  Get the current date
    SELECT p_date
    INTO mutual_date_value
    FROM MUTUAL_DATE
    ORDER BY p_date DESC
    LIMIT 1;

    -- Check if customer exists
    SELECT count(*)
    INTO row_count
    FROM CUSTOMER
    WHERE CUSTOMER.login = buy_shares.login; -- The name of the procedure is used as a prefix (i.e., scope)
    IF row_count = 0 THEN
        RAISE EXCEPTION 'User % not found.', login;
    END IF;

    -- Check if the symbol exists
    SELECT count(*)
    INTO row_count
    FROM MUTUAL_FUND
    WHERE MUTUAL_FUND.symbol = buy_shares.symbol; -- The name of the procedure is used as a prefix (i.e., scope)
    IF row_count = 0 THEN
        RAISE EXCEPTION 'Symbol % not found.', symbol;
    END IF;

    -- get the customer's balance
    SELECT balance
    INTO customer_balance
    FROM CUSTOMER
    WHERE CUSTOMER.login = buy_shares.login;

    -- Get the latest price for the desired symbol
    SELECT CLOSING_PRICE.price
    INTO symbol_price
    FROM CLOSING_PRICE
    WHERE CLOSING_PRICE.symbol = buy_shares.symbol
    ORDER BY CLOSING_PRICE.p_date DESC
    LIMIT 1;

    -- no sufficient funds to buy the shares
    IF (symbol_price * number_of_shares) > customer_balance THEN
        RETURN FALSE;
    END IF;

    -- buy shares section ==

    -- The transaction id will be generated automatically the sequence 'trx_sequence'
    INSERT INTO TRXLOG(login, symbol, action, num_shares, price, amount, t_date)
    VALUES (login, symbol, 'buy', number_of_shares, symbol_price, (symbol_price * number_of_shares),
            mutual_date_value);

    -- Check if the user already own some shares of this symbol
    SELECT count(*)
    INTO row_count
    FROM OWNS
    WHERE OWNS.login = buy_shares.login
      AND OWNS.symbol = buy_shares.symbol;

    -- if no shares are owned of the same symbol
    IF row_count = 0 THEN
        -- add an ownership of shares
        INSERT INTO OWNS(login, symbol, shares)
        VALUES (buy_shares.login, buy_shares.symbol, buy_shares.number_of_shares);
    ELSE
        -- Update the existing row
        UPDATE OWNS
        SET shares = shares + buy_shares.number_of_shares
        WHERE OWNS.login = buy_shares.login
          AND OWNS.symbol = buy_shares.symbol;
    END IF;
    -- end buy shares section ==

    -- update the customer's balance section ==
    UPDATE CUSTOMER
    SET balance = balance - (symbol_price * number_of_shares)
    WHERE CUSTOMER.login = buy_shares.login;
    -- end update customer's balance section ==

    -- shares are bought
    RETURN TRUE;

END;
$$ LANGUAGE PLPGSQL;

-- SELECT buy_shares('mary', 'MM', 1);

-- Question 5:
CREATE OR REPLACE FUNCTION buy_on_date_helper()
    RETURNS trigger AS
$$
DECLARE
    num_of_shares   int;
    customer_symbol varchar(20);
    symbol_price    decimal(10, 2);
    c_customer      record;
BEGIN

    FOR c_customer in (SELECT * FROM CUSTOMER)
        LOOP
            -- Get the symbol with the minimum shares
            SELECT symbol
            into customer_symbol
            FROM OWNS
            WHERE login = c_customer.login
            ORDER BY shares
            LIMIT 1;

            IF customer_symbol IS NOT NULL THEN
                -- Get the latest price for the desired symbol
                SELECT CLOSING_PRICE.price
                INTO symbol_price
                FROM CLOSING_PRICE
                WHERE CLOSING_PRICE.symbol = customer_symbol
                ORDER BY CLOSING_PRICE.p_date DESC
                LIMIT 1;

                num_of_shares = FLOOR(c_customer.balance / symbol_price);

                RAISE NOTICE 'Did the customer % buy % shares of % symbol? (%).',c_customer.login,  num_of_shares,customer_symbol,
                    buy_shares(c_customer.login, customer_symbol, num_of_shares);
            END IF;

        END LOOP;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$ LANGUAGE PLPGSQL;

CREATE TRIGGER buy_on_date
    AFTER UPDATE OR INSERT
    ON mutual_date
    FOR EACH ROW
EXECUTE FUNCTION buy_on_date_helper();

-- QUESTION 6:
CREATE OR REPLACE FUNCTION buy_on_price_helper()
    RETURNS trigger AS
$$
DECLARE
    num_of_shares   int;
    customer_symbol varchar(20);
    symbol_price    decimal(10, 2);
    c_customer      record;
BEGIN

    FOR c_customer in (SELECT * FROM CUSTOMER)
        LOOP
            -- Get the symbol with the minimum shares
            SELECT symbol
            into customer_symbol
            FROM OWNS
            WHERE login = c_customer.login AND symbol = NEW.symbol -- This can be optimize
            ORDER BY shares
            LIMIT 1;

            IF customer_symbol IS NOT NULL THEN
                -- Get the latest price for the desired symbol
                SELECT CLOSING_PRICE.price
                INTO symbol_price
                FROM CLOSING_PRICE
                WHERE CLOSING_PRICE.symbol = customer_symbol
                ORDER BY CLOSING_PRICE.p_date DESC
                LIMIT 1;

                num_of_shares = FLOOR(c_customer.balance / symbol_price);

                RAISE NOTICE 'Did the customer % buy % shares of % symbol? (%).',c_customer.login,  num_of_shares,customer_symbol,
                    buy_shares(c_customer.login, customer_symbol, num_of_shares);
            END IF;

        END LOOP;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$ LANGUAGE PLPGSQL;

CREATE TRIGGER buy_on_price
    AFTER UPDATE OF PRICE
    ON closing_price
    FOR EACH ROW
EXECUTE FUNCTION buy_on_price_helper();

DROP TABLE IF EXISTS temp3 CASCADE;
                    CREATE TABLE temp3 as (SELECT o.symbol, cp.price, o.login, o.shares, cp.p_date FROM
                        (SELECT * FROM closing_price where p_date = (SELECT p_date FROM closing_price ORDER BY p_date DESC FETCH FIRST ROW ONLY))
                            as cp JOIN owns as o ON cp.symbol = o.symbol)
SELECT * FROM temp3;

CREATE OR REPLACE FUNCTION price_initialization_helper()
    RETURNS trigger AS
$$
DECLARE
    -- num_of_shares   int;
    -- customer_symbol varchar(20);
    symbol_price    decimal(10, 2);
    -- c_customer      record;
    mutual_date_value date;
    curr date;
BEGIN

        --  Get the current date
    SELECT p_date
    INTO mutual_date_value
    FROM MUTUAL_DATE
    ORDER BY p_date DESC
    LIMIT 1;
    -- get most recent closing date
    SELECT p_date
    INTO curr
    FROM CLOSING_PRICE
    ORDER BY p_date DESC
    LIMIT 1;

    -- get current lowest closing price of most recent date
    SELECT price
    INTO symbol_price
    FROM CLOSING_PRICE
    WHERE p_date = curr
    ORDER BY price ASC;

    INSERT INTO CLOSING_PRICE(symbol, price, p_date)
        VALUES (NEW.symbol, symbol_price, mutual_date_value);
RETURN NULL;
END;
$$ LANGUAGE PLPGSQL;

CREATE TRIGGER price_initialization
    AFTER UPDATE OR INSERT
    ON mutual_fund
    FOR EACH ROW
EXECUTE FUNCTION price_initialization_helper();

-- testing price_initialization trigger
-- INSERT INTO MUTUAL_FUND
-- VALUES ('PS', 'pretty-stocks', 'pretty stocks', 'stocks', TO_DATE('25-JAN-20', 'DD-MON-YY'));


CREATE OR REPLACE FUNCTION sell_rebalance_helper()
    RETURNS trigger AS
$$
DECLARE
    -- num_of_shares   int;
    -- customer_symbol varchar(20);
    symbol_price    decimal(10, 2);
    -- c_customer      record;
    mutual_date_value date;
    -- curr date;
BEGIN

        --  Get the current date
    SELECT p_date
    INTO mutual_date_value
    FROM MUTUAL_DATE
    ORDER BY p_date DESC
    LIMIT 1;

    -- get current price for symbol
    SELECT CLOSING_PRICE.price
    INTO symbol_price
    FROM CLOSING_PRICE
    WHERE CLOSING_PRICE.symbol = NEW.symbol
    ORDER BY CLOSING_PRICE.p_date DESC
    LIMIT 1;

    -- update balance of customer
    UPDATE CUSTOMER
        SET balance = balance+(OLD.shares-NEW.shares)*symbol_price
        WHERE CUSTOMER.login = NEW.login;
RETURN NULL;
END;
$$ LANGUAGE PLPGSQL;

CREATE TRIGGER sell_rebalance
    AFTER UPDATE
    ON OWNS
    FOR EACH ROW
    WHEN (OLD.shares > NEW.shares)
EXECUTE FUNCTION sell_rebalance_helper();

-- testing sell_rebalance
-- UPDATE OWNS
-- SET shares = shares-2
-- WHERE login='mike' AND symbol='RE';

CREATE OR REPLACE FUNCTION return_ROI(symbol_curr varchar(20), user_curr varchar(10))
    RETURNS decimal AS
$$
DECLARE
    num_of_shares   int;
    current_val     int;
    recent_symbol_price    decimal(10, 2);
    shares_bought_total     int;
    shares_sold_total       int;
BEGIN

    -- get current price for symbol
    SELECT CLOSING_PRICE.price
    INTO recent_symbol_price
    FROM CLOSING_PRICE
    WHERE CLOSING_PRICE.symbol = symbol_curr
    ORDER BY CLOSING_PRICE.p_date DESC
    LIMIT 1;

    -- update balance of customer
    SELECT shares
    into num_of_shares
    FROM owns
    WHERE login = return_ROI.user_curr AND owns.symbol = symbol_curr;

    -- set current val of all shares he owns at their most recent price
    current_val = num_of_shares*recent_symbol_price;

        -- get total price of shares bought
        SELECT SUM(amount)
        INTO shares_bought_total
        FROM TRXLOG
        WHERE login='mike' AND symbol = 'RE' AND action='buy'
        GROUP BY login;

        -- get total price of shares sold
        SELECT SUM(amount)
        INTO shares_sold_total
        FROM TRXLOG
        WHERE login='mike' AND symbol = 'RE' AND action='sell'
        GROUP BY login;

        IF shares_sold_total IS NOT NULL THEN
            RETURN floor((CAST(current_val-(shares_bought_total-shares_sold_total) as decimal)/(shares_bought_total-shares_sold_total))*100);
        ELSE
            RETURN floor((CAST(current_val-(shares_bought_total) as decimal)/(shares_bought_total))*100);
        END IF;

END;
$$ LANGUAGE PLPGSQL;

-- SELECT return_ROI('RE', 'mike');

-- Task 9
CREATE OR REPLACE FUNCTION predict_gain_loss(user_curr varchar(10), symbol_curr varchar(20), action varchar(10),num_shares integer, amount integer)
    RETURNS varchar(30) AS
$$
DECLARE
    predicted     int;
    recent_symbol_price    decimal(10, 2);
    difference  int;
    status      varchar(10);
BEGIN
    -- get current price for symbol
    SELECT CLOSING_PRICE.price
    INTO recent_symbol_price
    FROM CLOSING_PRICE
    WHERE CLOSING_PRICE.symbol = symbol_curr
    ORDER BY CLOSING_PRICE.p_date DESC
    LIMIT 1;

    -- set current val of the transaction
    predicted = num_shares*recent_symbol_price;
    difference = predicted - amount;

    --if its a buy/sell update the status according to the difference
    IF (action='buy') THEN
        IF(difference < 0) THEN
            status = 'loss';
        ELSIF (difference > 0) THEN
            status = 'profit';
        ELSE
            status = 'hold';
        END IF;
    ELSIF (action='sell') THEN
        IF(difference > 0) THEN
            status = 'loss';
        ELSIF (difference < 0) THEN
            status = 'profit';
        ELSE
            status = 'hold';
        END IF;
    END IF;


    RETURN difference || ' ' || status;

END;
$$ LANGUAGE PLPGSQL;

 -- SELECT predict_gain_loss('mike', 'RE', 'buy',50, 1000)

 CREATE OR REPLACE FUNCTION sell_shares(user_curr varchar(10), symbol_curr varchar(10), num_shares_to_sell integer)
    RETURNS boolean AS
$$
DECLARE
    shares_owned   int;
    -- customer_symbol varchar(20);
    symbol_price    decimal(10, 2);
    -- c_customer      record;
    mutual_date_value date;
    curr date;
BEGIN

        --  Get the current date
    SELECT p_date
    INTO mutual_date_value
    FROM MUTUAL_DATE
    ORDER BY p_date DESC
    LIMIT 1;

    -- get current price for symbol
    SELECT CLOSING_PRICE.price
    INTO symbol_price
    FROM CLOSING_PRICE
    WHERE CLOSING_PRICE.symbol = sell_shares.symbol_curr
    ORDER BY CLOSING_PRICE.p_date DESC
    LIMIT 1;

    -- get amount of shares they own
    SELECT shares
    INTO shares_owned
    FROM owns
    WHERE login = user_curr AND symbol = sell_shares.symbol_curr;

    IF(shares_owned < sell_shares.num_shares_to_sell) THEN
        RETURN FALSE;
    END IF;

    -- update how many they own
    UPDATE OWNS
    SET shares = (shares_owned-sell_shares.num_shares_to_sell)
        WHERE login = sell_shares.user_curr AND symbol = sell_shares.symbol_curr;

    -- insert a sell transaction
    INSERT INTO trxlog(login, symbol,action, num_shares, price, amount, t_date)
        VALUES(sell_shares.user_curr, sell_shares.symbol_curr, 'sell', sell_shares.num_shares_to_sell, symbol_price, (sell_shares.num_shares_to_sell*symbol_price), mutual_date_value);

    RETURN TRUE;
END;
$$ LANGUAGE PLPGSQL;

-- SELECT sell_shares('mike', 'RE', 5);

--price jump helper
CREATE OR REPLACE FUNCTION price_jump_helper()
    RETURNS trigger AS
$$
DECLARE
    num_of_shares   int;
    customer_symbol varchar(20);
    symbol_price    decimal(10, 2);
    c_customer      record;
BEGIN

    FOR c_customer in (SELECT * FROM CUSTOMER)
        LOOP
            -- Get the symbol with the minimum shares
            SELECT symbol
            into customer_symbol
            FROM OWNS
            WHERE login = c_customer.login AND symbol = NEW.symbol -- This can be optimize
            ORDER BY shares
            LIMIT 1;

            IF customer_symbol IS NOT NULL THEN
                -- Get the latest price for the desired symbol
                SELECT CLOSING_PRICE.price
                INTO symbol_price
                FROM CLOSING_PRICE
                WHERE CLOSING_PRICE.symbol = customer_symbol
                ORDER BY CLOSING_PRICE.p_date DESC
                LIMIT 1;

                -- get the amount they own
                SELECT shares
                INTO num_of_shares
                FROM OWNS
                WHERE login = c_customer.login AND symbol = NEW.symbol;



                RAISE NOTICE 'Price changed by more than $10 for %, selling all % shares owned by %.',customer_symbol, num_of_shares,c_customer.login;
                -- sell_shares(c_customer.login, customer_symbol, num_of_shares);
            END IF;

        END LOOP;
    RETURN NULL; -- result is ignored since this is an AFTER trigger
END;
$$ LANGUAGE PLPGSQL;

--price jump doesnt work
/*CREATE TRIGGER price_jump
    AFTER INSERT
    ON CLOSING_PRICE
    FOR EACH ROW
EXECUTE FUNCTION price_jump_helper();*/

