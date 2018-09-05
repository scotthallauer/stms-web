USE stms;

INSERT INTO user (firstName, lastName, email, pwdHash, pwdSalt) 
values ('Scott', 'Hallauer', 'scott.hallauer@gmail.com', '225c245490a5385b2a034f7c1ebcbe7a034e44e26871163a32fb20e2f76a3f01', 'KxZ65ezpqs86tlM'); 
# password is password

INSERT INTO user (firstName, lastName, email, pwdHash, pwdSalt) 
VALUES ('Jonathon', 'Everatt', 'EVRJON003@myuct.ac.za', 'd6a4c76e085cf2f4130ba94188883ec6fff9e91a9546887a0ee8f55066be8d37','JKMCNTAc7xNoR3b');
#password is 12345678

INSERT INTO user (firstName, lastName, email, pwdHash, pwdSalt) 
VALUES ('Jessica','Bourn','BRNJES018@myuct.ac.za', '8eac3fdf96fde1d740de6d1309b94eb8ce80a0d2232b14d1900384fc30202f6b','IjlSlQWnDwbD1Ee');
#password is p3gasuss 

#semester inserts
INSERT INTO semester (userID,semesterName,startDate,endDate) 
VALUES (1,'1st Semester', '2018-01-19', '2018-06-24');

INSERT INTO semester (userID,semesterName,startDate,endDate) 
VALUES (1, '2nd Semester', '2018-07-24', '2018-11-30');

INSERT INTO semester (userID,semesterName,startDate,endDate) 
VALUES (2,'Jan-June', '2018-01-19', '2018-06-24');

INSERT INTO semester (userID,semesterName,startDate,endDate) 
VALUES (2, 'July-Dec', '2018-07-24', '2018-11-30');

INSERT INTO semester (userID,semesterName,startDate,endDate) 
VALUES (3, 'FIRST SEM', '2018-01-19', '2018-06-24');

INSERT INTO semester (userID,semesterName,startDate,endDate) 
VALUES (3, 'SECOND SEM', '2018-07-24', '2018-11-30');

#course inserts
INSERT INTO course (semesterID1,semesterID2,courseName,courseCode,colour) 
VALUES (1, NULL, 'Computer Science', 'CSC3002F', 'blue');
INSERT INTO course (semesterID1,semesterID2,courseName,courseCode,colour) 
VALUES (1, NULL, 'Genetics', 'MCB3026F', 'green');
INSERT INTO course (semesterID1,semesterID2,courseName,courseCode,colour)  
VALUES (2, NULL, 'Computer Science', 'CSC3003S', 'blue');
INSERT INTO course (semesterID1,semesterID2,courseName,courseCode,colour) 
VALUES (2, NULL, 'Genetics', 'MCB3023S', 'green');
INSERT INTO course (semesterID1,semesterID2,courseName,courseCode,colour) 
VALUES (4, NULL, 'Philosophy', 'PHI3023S', 'purple');
INSERT INTO course (semesterID1,semesterID2,courseName,courseCode,colour)  
VALUES (3, NULL, 'Computer Science', 'CSC3002F', 'blue');
INSERT INTO course (semesterID1,semesterID2,courseName,courseCode,colour) 
VALUES (4, NULL, 'Computer Science', 'CSC3003S', 'blue');
INSERT INTO course (semesterID1,semesterID2,courseName,courseCode,colour) 
VALUES (5, NULL, 'Genetics', 'MCB3026F', 'green');
INSERT INTO course (semesterID1,semesterID2,courseName,courseCode,colour) 
VALUES (6, NULL, 'Computer Science :\'(', 'CSC3003S', 'blue');
INSERT INTO course (semesterID1,semesterID2,courseName,courseCode,colour) 
VALUES (6, NULL, 'Genetics :D', 'MCB3023S', 'green');
INSERT INTO course (semesterID1,semesterID2,courseName,courseCode,colour) 
VALUES (2, NULL, 'Genetics', 'MCB3012Z', 'red');

INSERT INTO courseassignment (courseID, description, dueDate, priority, weighting, studyHours, complete) 
VALUES (3, 'Capstone Project', '2018-09-07 09:00:00', 3, 15.0, 0, false);
INSERT INTO courseassignment (courseID, description, dueDate, priority, weighting, studyHours, complete) 
VALUES (6, 'Capstone', '2018-09-07 17:00:00', 3, 15.0, 0, false);
INSERT INTO courseassignment (courseID, description, dueDate, priority, weighting, studyHours, complete) 
VALUES (8, 'gen prac', '2018-08-16 10:00:00', 1, 1.5, 0, true);

INSERT INTO courseSession (sessionPID, courseID, sessionType, startDate, endDate, length, recType, priority, weighting, studyHours)
VALUES (0, 3, 'lecture', '2018-07-23 09:00:00', '2018-10-07 09:45:00', 2700, 'week_1___1,2,3,4,5#', null, null, null);
INSERT INTO courseSession (sessionPID, courseID, sessionType, startDate, endDate, length, recType, priority, weighting, studyHours)
VALUES (0, 4, 'lecture', '2018-07-23 11:00:00', '2018-10-07 11:45:00', 2700, 'week_1___1,2,3,4,5#', null, null, null);
INSERT INTO courseSession (sessionPID, courseID, sessionType, startDate, endDate, length, recType, priority, weighting, studyHours)
VALUES (0, 5, 'lecture', '2018-07-23 15:00:00', '2018-10-07 15:45:00', 2700, 'week_1___1,2,3,4,5#', null, null, null);
INSERT INTO courseSession (sessionPID, courseID, sessionType, startDate, endDate, length, recType, priority, weighting, studyHours)
VALUES (0, 5, 'lecture', '2018-07-23 11:00:00', '2018-10-25 12:00:00', 2700, 'week_1___1,2,3,4,5#', null, null, null);
INSERT INTO courseSession (sessionPID, courseID, sessionType, startDate, endDate, length, recType, priority, weighting, studyHours)
VALUES (0, 5, 'lecture', '2018-07-23 07:00:00', '2018-10-25 8:00:00', 2700, 'week_1___1,2,3,4,5#', null, null, null);
INSERT INTO courseSession (sessionPID, courseID, sessionType, startDate, endDate, length, recType, priority, weighting, studyHours)
VALUES (0, 5, 'lecture', '2018-07-23 09:00:00', '2018-10-25 10:00:00', 2700, 'week_1___1,2,3,4,5#', null, null, null);
INSERT INTO courseSession (sessionPID, courseID, sessionType, startDate, endDate, length, recType, priority, weighting, studyHours)
VALUES (0, 5, 'lecture', '2018-07-23 16:00:00', '2018-10-25 17:00:00', 2700, 'week_1___1,2,3,4,5#', null, null, null);
INSERT INTO courseSession (sessionPID, courseID, sessionType, startDate, endDate, length, recType, priority, weighting, studyHours)
VALUES (1, 3, 'lecture', '2018-09-14 09:00:00', '2018-09-14 09:45:00', 1536908400, 'none', null, null, null);
INSERT INTO courseSession (sessionPID, courseID, sessionType, startDate, endDate, length, recType, priority, weighting, studyHours)
VALUES (0, 3, 'test', '2018-09-14 09:00:00', '2018-09-14 09:45:00', null, null, 3, 7.5, null);

INSERT INTO studysession (semesterID,startTime,endTime,confirmed,note) 
VALUES (3, '2018-09-04 9:00:00', '2018-09-04 10:00:00', 1, 'Auto generated Study Session');
INSERT INTO studysession (semesterID,startTime,endTime,confirmed,note) 
VALUES (5, '2018-09-04 16:00:00', '2018-09-04 17:00:00', 1, 'Auto generated Study Session');

INSERT INTO task (userID, description, dueDate, complete) 
VALUES (1, 'Capstone final project', '2018-09-07 17:00:00', 0);
INSERT INTO task (userID, description, dueDate, complete) 
VALUES (2, 'Capstone final project', '2018-09-07 17:00:00', 0);
INSERT INTO task (userID, description, dueDate, complete) 
VALUES (1, 'Genetics test', '2018-09-10 17:00:00', 0);