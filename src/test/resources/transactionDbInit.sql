CREATE SCHEMA db1testdata;

CREATE TABLE db1testdata.transaction_log (
                    transaction_id integer,
                    customer_id    integer,
                    total          integer,
                    date           timestamptz
);
