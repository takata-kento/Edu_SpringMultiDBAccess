CREATE SCHEMA db1testdata;

CREATE TABLE db1testdata.transaction_log (
                    transaction_id integer,
                    customer_id    integer,
                    total          integer,
                    date           timestamptz
);

INSERT INTO
    db1testdata.transaction_log (transaction_id, customer_id     , total  , date)
VALUES
                                ('24042101'    , '1'             , '400'  , '2024-04-21 10:23:54+09:00'),
                                ('24071101'    , '3'             , '20010', '2024-07-11 11:03:20+09:00'),
                                ('24060101'    , '2'             , '9541' , '2024-06-01 01:41:42+09:00'),
                                ('24102101'    , '4'             , '50021', '2024-10-21 20:38:02+09:00');
