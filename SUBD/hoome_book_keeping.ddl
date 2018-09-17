-- Generated by Oracle SQL Developer Data Modeler 18.2.0.179.0756
--   at:        2018-09-11 00:38:08 MSK
--   site:      Oracle Database 11g
--   type:      Oracle Database 11g



CREATE TABLE currency_types (
    id     INTEGER NOT NULL,
    name   VARCHAR2(5) NOT NULL
);

ALTER TABLE currency_types ADD CONSTRAINT currency_types_pk PRIMARY KEY ( id );

CREATE TABLE family_members (
    id        INTEGER NOT NULL,
    surname   VARCHAR2(20) NOT NULL,
    name      VARCHAR2(20) NOT NULL
);

ALTER TABLE family_members ADD CONSTRAINT family_members_pk PRIMARY KEY ( id );

CREATE TABLE main_operation_types (
    id                       INTEGER NOT NULL,
    name                     VARCHAR2(25) NOT NULL,
    top_operation_types_id   INTEGER NOT NULL
);

ALTER TABLE main_operation_types ADD CONSTRAINT main_operation_types_pk PRIMARY KEY ( id );

CREATE TABLE operation_types (
    id                        INTEGER NOT NULL,
    name                      VARCHAR2(25) NOT NULL,
    main_operation_types_id   INTEGER NOT NULL
);

ALTER TABLE operation_types ADD CONSTRAINT operation_types_pk PRIMARY KEY ( id );

--  ERROR: Table name length exceeds maximum allowed length(30) 
CREATE TABLE operation_types_to_currency_types (
    id                   INTEGER NOT NULL,
    exchange_rate        FLOAT NOT NULL,
    operation_types_id   INTEGER NOT NULL,
    currency_types_id    INTEGER NOT NULL
);

--  ERROR: PK name length exceeds maximum allowed length(30) 
ALTER TABLE operation_types_to_currency_types ADD CONSTRAINT operation_types_to_currency_types_pk PRIMARY KEY ( id );

CREATE TABLE operations (
    id                   INTEGER NOT NULL,
    "date"               DATE,
    description          VARCHAR2(25),
    cash                 CHAR(1),
    amount               FLOAT,
    family_members_id    INTEGER NOT NULL,
    operation_types_id   INTEGER NOT NULL
);

ALTER TABLE operations ADD CONSTRAINT operations_pk PRIMARY KEY ( id );

CREATE TABLE top_operation_types (
    id     INTEGER NOT NULL,
    name   VARCHAR2(10) NOT NULL
);

ALTER TABLE top_operation_types ADD CONSTRAINT top_operation_types_pk PRIMARY KEY ( id );

--  ERROR: FK name length exceeds maximum allowed length(30) 
ALTER TABLE main_operation_types
    ADD CONSTRAINT main_operation_types_top_operation_types_fk FOREIGN KEY ( top_operation_types_id )
        REFERENCES top_operation_types ( id );

--  ERROR: FK name length exceeds maximum allowed length(30) 
ALTER TABLE operation_types
    ADD CONSTRAINT operation_types_main_operation_types_fk FOREIGN KEY ( main_operation_types_id )
        REFERENCES main_operation_types ( id );

--  ERROR: FK name length exceeds maximum allowed length(30) 
ALTER TABLE operation_types_to_currency_types
    ADD CONSTRAINT operation_types_to_currency_types_currency_types_fk FOREIGN KEY ( currency_types_id )
        REFERENCES currency_types ( id );

--  ERROR: FK name length exceeds maximum allowed length(30) 
ALTER TABLE operation_types_to_currency_types
    ADD CONSTRAINT operation_types_to_currency_types_operation_types_fk FOREIGN KEY ( operation_types_id )
        REFERENCES operation_types ( id );

ALTER TABLE operations
    ADD CONSTRAINT operations_family_members_fk FOREIGN KEY ( family_members_id )
        REFERENCES family_members ( id );

ALTER TABLE operations
    ADD CONSTRAINT operations_operation_types_fk FOREIGN KEY ( operation_types_id )
        REFERENCES operation_types ( id );



-- Oracle SQL Developer Data Modeler Summary Report: 
-- 
-- CREATE TABLE                             7
-- CREATE INDEX                             0
-- ALTER TABLE                             13
-- CREATE VIEW                              0
-- ALTER VIEW                               0
-- CREATE PACKAGE                           0
-- CREATE PACKAGE BODY                      0
-- CREATE PROCEDURE                         0
-- CREATE FUNCTION                          0
-- CREATE TRIGGER                           0
-- ALTER TRIGGER                            0
-- CREATE COLLECTION TYPE                   0
-- CREATE STRUCTURED TYPE                   0
-- CREATE STRUCTURED TYPE BODY              0
-- CREATE CLUSTER                           0
-- CREATE CONTEXT                           0
-- CREATE DATABASE                          0
-- CREATE DIMENSION                         0
-- CREATE DIRECTORY                         0
-- CREATE DISK GROUP                        0
-- CREATE ROLE                              0
-- CREATE ROLLBACK SEGMENT                  0
-- CREATE SEQUENCE                          0
-- CREATE MATERIALIZED VIEW                 0
-- CREATE MATERIALIZED VIEW LOG             0
-- CREATE SYNONYM                           0
-- CREATE TABLESPACE                        0
-- CREATE USER                              0
-- 
-- DROP TABLESPACE                          0
-- DROP DATABASE                            0
-- 
-- REDACTION POLICY                         0
-- 
-- ORDS DROP SCHEMA                         0
-- ORDS ENABLE SCHEMA                       0
-- ORDS ENABLE OBJECT                       0
-- 
-- ERRORS                                   6
-- WARNINGS                                 0
