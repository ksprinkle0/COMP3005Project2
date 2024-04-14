CREATE TABLE Member (
    memberID SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    age INT NOT NULL
);

CREATE TABLE Trainer (
    trainerID SERIAL PRIMARY KEY,
    code VARCHAR(4) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE Admin (
    code VARCHAR(4) PRIMARY KEY
);

CREATE TABLE MemberDashboard (
    memberID INT PRIMARY KEY,
    exercise_routine TEXT,
    fitness_goal TEXT,
    weight INT,
    steps INT,
    calories_burnt INT,
    activity INT,
	FOREIGN KEY (memberID) REFERENCES Member(memberID)
);

CREATE TABLE TrainerAvailability (
    trainerID INT,
	trainer_name VARCHAR(100) NOT NULL,
    hour TIME,
    FOREIGN KEY (trainerID) REFERENCES Trainer(trainerID)
);

CREATE TABLE Bookings (
    member_name VARCHAR(100),
    time TIME,
    day DATE
);

CREATE TABLE Equipment (
    name VARCHAR(100) PRIMARY KEY,
    status VARCHAR(20),
    maintenance_history TEXT[]
);

CREATE TABLE ManageClassSchedule (
    classID SERIAL PRIMARY KEY,
    class_name VARCHAR(100),
    class_day DATE,
    class_time TIME
);

CREATE TABLE ManageSessionSchedule (
    sessionID SERIAL PRIMARY KEY,
    session_time TIME,
    session_day DATE,
    session_availability BOOLEAN DEFAULT TRUE,
	trainerID INT,
	trainer_name VARCHAR(100) NOT NULL, 
    FOREIGN KEY (trainerID) REFERENCES Trainer(trainerID)
);

CREATE TABLE MemberClassSchedule (
    memberID INT,
    classID INT,
    class_day DATE,
    class_time TIME,
    class_name VARCHAR(100),
    FOREIGN KEY (memberID) REFERENCES Member(memberID),
    FOREIGN KEY (classID) REFERENCES ManageClassSchedule(classID)
);

CREATE TABLE MemberSessionSchedule (
    memberID INT,
    sessionID INT,
    session_day DATE,
    session_time TIME,
    trainerID INT,
    trainer_name VARCHAR(100),
    FOREIGN KEY (memberID) REFERENCES Member(memberID),
    FOREIGN KEY (sessionID) REFERENCES ManageSessionSchedule(sessionID),
    FOREIGN KEY (trainerID) REFERENCES Trainer(trainerID)
);

CREATE TABLE TrainerSchedule (
    sessionID SERIAL PRIMARY KEY,
    session_day DATE,
    session_time TIME,
	trainerID INT,
    memberID INT,
	member_name VARCHAR(100) NOT NULL,
    FOREIGN KEY (trainerID) REFERENCES Trainer(trainerID),
    FOREIGN KEY (memberID) REFERENCES Member(memberID)
);

CREATE TABLE MemberBilling (
    memberID INT,
    member_name VARCHAR(100) NOT NULL,
    bill_type VARCHAR(20) NOT NULL,
    cost INT,
	paid BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (memberID) REFERENCES Member(memberID)
);

CREATE TABLE TrainerPayment (
    trainerID INT,
    sessionID INT,
    paycheck INT DEFAULT 20,
	paid BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (trainerID) REFERENCES Trainer(trainerID),
    FOREIGN KEY (sessionID) REFERENCES TrainerSchedule(sessionID)
);