DROP SCHEMA IF EXISTS stms;

CREATE SCHEMA IF NOT EXISTS stms;

USE stms;

CREATE TABLE user (
	userID int NOT NULL AUTO_INCREMENT,
    firstName VARCHAR(25) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    email VARCHAR(40) NOT NULL UNIQUE,
    activated BIT NOT NULL,
    pwdHash VARCHAR(64) NOT NULL,
    pwdSalt VARCHAR(15) NOT NULL,
    tokenCode VARCHAR(64) NULL UNIQUE,
    tokenDate DATETIME NULL,
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
    semesterID1 int NOT NULL,
    semesterID2 int NULL,
    courseName VARCHAR(25) NOT NULL,
    courseCode VARCHAR(10) NULL,
    colour VARCHAR(7) NULL,
    PRIMARY KEY (courseID),
    FOREIGN KEY (semesterID1) REFERENCES semester(semesterID),
    FOREIGN KEY (semesterID2) REFERENCES semester(semesterID),
    UNIQUE KEY semester_course_uniq (semesterID1, semesterID2, courseName, courseCode) # prevents two of the same courses happening in the same semester
);

CREATE TABLE courseSession (
	sessionID int NOT NULL AUTO_INCREMENT,
	sessionPID int NOT NULL,
    courseID int NOT NULL,
    sessionName VARCHAR(25) NOT NULL,
    sessionType VARCHAR(10) NOT NULL,
    startDate DateTime NOT NULL,
    endDate DateTime NOT NULL,
    length int NULL, # duration of session in seconds (only required for recurring events) 
    recType VARCHAR(40) NOT NULL,
    location VARCHAR(25) NULL,
    note VARCHAR(500) NULL,
    possibleMark double NULL,
    earnedMark double NULL,
    PRIMARY KEY (sessionID),
    FOREIGN KEY (courseID) REFERENCES course(courseID),
    UNIQUE KEY course_session_uniq (courseID, sessionName, sessionType, startDate, endDate, recType)
);

CREATE TABLE courseAssignment (
	assignmentID int NOT NULL AUTO_INCREMENT,
    courseID int NOT NULL,
    Assignmentname VARCHAR(25) NOT NULL,
    dueDate DateTime NOT NULL,
    priority int NOT NULL,
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

