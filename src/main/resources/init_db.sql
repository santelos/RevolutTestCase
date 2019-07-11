--
-- Sequence for wallet identifiers
--
CREATE SEQUENCE wallet_ids
  START WITH 1
  INCREMENT BY 1;

--
-- Wallet table creation
--
CREATE TABLE wallet(
  -- identifier
  id          BIGINT    NOT NULL DEFAULT wallet_ids.nextval PRIMARY KEY ,
  -- amount of the money
  money       DECIMAL   NOT NULL,
  -- Currency type
  currency    INTEGER   NOT NULL,
  -- Status
  status      INTEGER   NOT NULL
)