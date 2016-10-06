-- Indexes for primary keys have been explicitly created.

-- ---------- Table for validation queries from the connection pool. ----------

DROP TABLE PingTable;
CREATE TABLE PingTable (foo CHAR(1));

-- ------------------------------ UserProfile ----------------------------------

DROP TABLE Bet;
DROP TABLE BetOption;
DROP TABLE BetType;
DROP TABLE UserProfile;
DROP TABLE Event;
DROP TABLE Category;

CREATE TABLE UserProfile (
    usrId BIGINT NOT NULL AUTO_INCREMENT,
    loginName VARCHAR(30) COLLATE latin1_bin NOT NULL,
    enPassword VARCHAR(13) NOT NULL, 
    firstName VARCHAR(30) NOT NULL,
    lastName VARCHAR(40) NOT NULL, 
    email VARCHAR(60) NOT NULL,
    CONSTRAINT UserProfile_PK PRIMARY KEY (usrId),
    CONSTRAINT LoginNameUniqueKey UNIQUE (loginName)) 
    ENGINE = InnoDB;

CREATE INDEX UserProfileIndexByLoginName ON UserProfile (loginName);

CREATE TABLE Category(
	categoryId BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(20) NOT NULL,
	CONSTRAINT Category_PK PRIMARY KEY (categoryId),
	CONSTRAINT NameUniqueKey UNIQUE (name))
	ENGINE = InnoDB;
	
CREATE TABLE Event(
	eventId BIGINT NOT NULL AUTO_INCREMENT,
	name VARCHAR(40) NOT NULL,
	eventStart TIMESTAMP NOT NULL,
	categoryId BIGINT,
	CONSTRAINT Event_PK PRIMARY KEY (eventId),
	CONSTRAINT EventCategoryIdFK FOREIGN KEY (categoryId)
        REFERENCES Category (categoryId))
    ENGINE = InnoDB;

CREATE TABLE BetType(
	betTypeId BIGINT NOT NULL AUTO_INCREMENT,
	question VARCHAR(80) NOT NULL,
	multiple BOOLEAN NOT NULL,
	eventId BIGINT NOT NULL,
	CONSTRAINT BetType_PK PRIMARY KEY (betTypeId),
	CONSTRAINT EventBetTypeIdFK FOREIGN KEY (eventId)
        REFERENCES Event (eventId))
    ENGINE = InnoDB;  

CREATE TABLE BetOption(
	betOptionId BIGINT NOT NULL AUTO_INCREMENT,
	answer VARCHAR(80) NOT NULL,
	rate DOUBLE DEFAULT 0 NOT NULL,
	betState BOOLEAN DEFAULT NULL,
	betTypeId BIGINT NOT NULL,
	CONSTRAINT BetOption_PK PRIMARY KEY (betOptionId),
	CONSTRAINT BetOptionBetTypeIdFK FOREIGN KEY (betTypeId)
        REFERENCES BetType (betTypeId))
    ENGINE = InnoDB;

CREATE TABLE Bet(
	betId BIGINT NOT NULL AUTO_INCREMENT,
	betedMoney DOUBLE DEFAULT 0 NOT NULL,
	date TIMESTAMP NOT NULL,
	usrId BIGINT NOT NULL,
	eventId BIGINT NOT NULL,
	betOptionId BIGINT NOT NULL,
	CONSTRAINT Bet_PK PRIMARY KEY (betId),
	CONSTRAINT BetUserProfileIdFK FOREIGN KEY (usrId)
        REFERENCES UserProfile (usrId),
	CONSTRAINT BetEventIdFK FOREIGN KEY (eventId)
        REFERENCES Event (eventId),
	CONSTRAINT BetBetOptionIdFK FOREIGN KEY (betOptionId)
        REFERENCES BetOption (betOptionId))
    ENGINE = InnoDB;

  
