CREATE SCHEMA db2testdata;

CREATE TABLE db2testdata.customer_info (
                    customer_id integer,
                    name        character varying(50),
                    age         integer,
                    birthday    date,
                    gender      character varying(1)
);
