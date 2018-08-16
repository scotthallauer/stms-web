DROP SCHEMA IF EXISTS stms;

CREATE SCHEMA IF NOT EXISTS stms;

USE stms;

CREATE TABLE user (
	userID int NOT NULL AUTO_INCREMENT,
    firstName VARCHAR(25) NOT NULL,
    lastNames VARCHAR(50) NOT NULL,
    email VARCHAR(40) NOT NULL,
    activated BIT NOT NULL,
    pwdHash VARCHAR(64) NOT NULL,
    pwdSalt VARCHAR(15) NOT NULL,
    PRIMARY KEY (userID)
);

CREATE TABLE task (
	taskID int NOT NULL AUTO_INCREMENT,
    userID int NOT NULL,
    taskName VARCHAR(25) NOT NULL,
    taskExplain VARCHAR(200) NOT NULL,
    PRIMARY KEY (taskID),
    FOREIGN KEY (userID) REFERENCES user(userID)
);

CREATE TABLE calendarEvent (
	eventID int NOT NULL AUTO_INCREMENT,
    userID int NOT NULL,
    eventName VARCHAR(25) NOT NULL,
    location VARCHAR(40) NOT NULL,
    startTime DATETIME NOT NULL,
    endTime DATETIME NOT NULL,
    notes VARCHAR(200) NULL,
    PRIMARY KEY (eventID),
    FOREIGN KEY (userID) REFERENCES user(userID)
);

CREATE TABLE semester (
	semesterID int NOT NULL AUTO_INCREMENT,
    userID int NOT NULL,
    semesterName VARCHAR(25) NOT NULL,
    startDate DATETIME NOT NULL,
    endDate DATETIME NOT NULL,
    PRIMARY KEY (semesterID),
    FOREIGN KEY (userID) REFERENCES user(userID)
);

CREATE TABLE course (
	courseID int NOT NULL AUTO_INCREMENT,
    semesterID int NOT NULL,
    courseName VARCHAR(25) NOT NULL,
    courseCode VARCHAR(10) NOT NULL,
    semester1 int NOT NULL,
    semester2 int NULL,
    PRIMARY KEY (courseID),
    FOREIGN KEY (semesterID) REFERENCES semester(semesterID)
);

CREATE TABLE courseSession (
	sessionID int NOT NULL AUTO_INCREMENT,
    courseID int NOT NULL,
    sessionName VARCHAR(25) NOT NULL,
    sessionType VARCHAR(10) NOT NULL,
    startTime DateTime NOT NULL,
    endTime DateTime NOT NULL,
    location VARCHAR(25) NULL,
    rrule VARCHAR(25) NOT NULL,
    note VARCHAR(200) NULL,
    PRIMARY KEY (sessionID),
    FOREIGN KEY (courseID) REFERENCES course(courseID)
);

CREATE TABLE courseAssignment (
	assignmentID int NOT NULL AUTO_INCREMENT,
    courseID int NOT NULL,
    Assignmentname VARCHAR(25) NOT NULL,
    dueDate DateTime NOT NULL,
    PRIMARY KEY (assignmentID),
    FOREIGN KEY (courseID) REFERENCES course(courseID)
);

CREATE TABLE studySession (
	PsessionID int NOT NULL AUTO_INCREMENT,
    semesterID int NOT NULL,
    startTime DateTime NOT NULL,
    endTime DateTime NOT NULL,
    note VARCHAR(200) NULL,
    PRIMARY KEY (PsessionID),
    FOREIGN KEY (semesterID) REFERENCES semester(semesterID)
);

