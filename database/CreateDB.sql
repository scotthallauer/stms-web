CREATE TABLE user (
	userID int NOT NULL,
    firstName VARCHAR(25) NOT NULL,
    lastNames VARCHAR(50),
    email VARCHAR(40),
    PRIMARY KEY (userID)
);

CREATE TABLE task (
	taskID int NOT NULL,
    userID int NOT NULL,
    taskName VARCHAR(25),
    taskExplain VARCHAR(200),
    PRIMARY KEY (taskID),
    FOREIGN KEY (userID) REFERENCES user(userID)
);

CREATE TABLE calendarEvent (
	eventID int NOT NULL,
    userID int NOT NULL,
    eventName VARCHAR(25) NOT NULL,
    location VARCHAR(40),
    startTime DATETIME,
    endTime DATETIME,
    notes VARCHAR(200),
    PRIMARY KEY (eventID),
    FOREIGN KEY (userID) REFERENCES user(userID)
);

CREATE TABLE semester (
	semesterID int NOT NULL,
    userID int NOT NULL,
    semesterName VARCHAR(25) NOT NULL,
    startDate DATETIME,
    endDate DATETIME,
    PRIMARY KEY (semesterID),
    FOREIGN KEY (userID) REFERENCES user(userID)
);

CREATE TABLE course (
	courseID int NOT NULL,
    semesterID int NOT NULL,
    name VARCHAR(25) NOT NULL,
    code VARCHAR(10),
    semester1 int NOT NULL,
    semester2 int NULL,
    PRIMARY KEY (courseID),
    FOREIGN KEY (semesterID) REFERENCES semester(semesterID)
);

CREATE TABLE courseSession (
	sessionID int NOT NULL,
    courseID int NOT NULL,
    name VARCHAR(25) NOT NULL,
    type VARCHAR(10),
    startTime DateTime,
    endTime DateTime,
    location VARCHAR(25),
    rrule VARCHAR(25),
    note VARCHAR(200),
    PRIMARY KEY (sessionID),
    FOREIGN KEY (courseID) REFERENCES course(courseID)
);

CREATE TABLE courseAssignment (
	assignmentID int NOT NULL,
    courseID int NOT NULL,
    name VARCHAR(25) NOT NULL,
    dueDate DateTime, 
    PRIMARY KEY (assignmentID),
    FOREIGN KEY (courseID) REFERENCES course(courseID)
);

CREATE TABLE studySession (
	PsessionID int NOT NULL,
    semesterID int NOT NULL,
    startTime DateTime,
    endTime DateTime,
    note VARCHAR(200),
    PRIMARY KEY (PsessionID),
    FOREIGN KEY (semesterID) REFERENCES semester(semesterID)
);

