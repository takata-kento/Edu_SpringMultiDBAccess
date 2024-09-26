CREATE SCHEMA db2testdata;

CREATE TABLE db2testdata.customer_info (
                    customer_id integer,
                    name        character varying(50),
                    age         integer,
                    birthday    date,
                    gender      character varying(1)
);

INSERT INTO
    db2testdata.customer_info (customer_id, name     , age , birthday    , gender)
VALUES
                              ('1'        , 'alice'  , '10', '2014-03-31', '1'),
                              ('2'        , 'bob'    , '27', '1997-08-13', '0'),
                              ('3'        , 'charlie', '72', '1952-11-04', '1'),
                              ('4'        , 'dave'   , '47', '1977-12-14', '0');
