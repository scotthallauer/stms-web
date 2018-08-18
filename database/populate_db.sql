USE stms;

INSERT INTO user (firstName, lastName, email, activated, pwdHash, pwdSalt) 
values ('Scott', 'Hallauer', 'scott.hallauer@gmail.com', 1, '225c245490a5385b2a034f7c1ebcbe7a034e44e26871163a32fb20e2f76a3f01', 'KxZ65ezpqs86tlM'); 
# password is password

INSERT INTO user (firstName, lastName, email, activated, pwdHash, pwdSalt) 
VALUES ('Jonathon', 'Everatt', 'EVRJON003@myuct.ac.za',1,'d6a4c76e085cf2f4130ba94188883ec6fff9e91a9546887a0ee8f55066be8d37','JKMCNTAc7xNoR3b');
#password is 12345678

INSERT INTO user (firstName, lastName, email, activated, pwdHash, pwdSalt) 
VALUES ('Jessica','Bourn','BRNJES018@myuct.ac.za',0,'8eac3fdf96fde1d740de6d1309b94eb8ce80a0d2232b14d1900384fc30202f6b','IjlSlQWnDwbD1Ee');
#password is p3gasuss 

#semester inserts
INSERT INTO semester (userID,semesterName,startDate,endDate) 
VALUES (1,'1st Semester', '2018-01-19', '2018-06-24');

INSERT INTO semester (userID,semesterName,startDate,endDate) 
VALUES (1, '2nd Semester', '2018-07-24', '2018-11-30');

INSERT INTO semester (userID,semesterName,startDate,endDate) 
VALUES (2,'Jan-June 2018', '2018-01-19', '2018-06-24');

INSERT INTO semester (userID,semesterName,startDate,endDate) 
VALUES (2, 'July-Dec 2018', '2018-07-24', '2018-11-30');

INSERT INTO semester (userID,semesterName,startDate,endDate) 
VALUES (3, 'FIRST SEM', '2018-01-19', '2018-06-24');

INSERT INTO semester (userID,semesterName,startDate,endDate) 
VALUES (3, 'SECOND SEM', '2018-07-24', '2018-11-30');

#course inserts
INSERT INTO course (semesterID1,semesterID2,courseName,courseCode,colour) 
VALUES (1, NULL, 'Comptuer Science', 'CSC3002F', '#22A7F0');
INSERT INTO course (semesterID1,semesterID2,courseName,courseCode,colour) 
VALUES (1, NULL, 'Genetics', 'MCB3026F', '#26A65B');
INSERT INTO course (semesterID1,semesterID2,courseName,courseCode,colour)  
VALUES (2, NULL, 'Comptuer Science', 'CSC3003S', '#22A7F0');
INSERT INTO course (semesterID1,semesterID2,courseName,courseCode,colour) 
VALUES (2, NULL, 'Genetics', 'MCB3023S', '#26A65B');
INSERT INTO course (semesterID1,semesterID2,courseName,courseCode,colour) 
VALUES (3, NULL, 'Philosophy', 'PHI3023F', '#875F9A');
INSERT INTO course (semesterID1,semesterID2,courseName,courseCode,colour)  
VALUES (3, NULL, 'Comptuer Science', 'CSC3002F', '#22A7F0');
INSERT INTO course (semesterID1,semesterID2,courseName,courseCode,colour) 
VALUES (4, NULL, 'Comptuer Science', 'CSC3002F', '#22A7F0');
INSERT INTO course (semesterID1,semesterID2,courseName,courseCode,colour) 
VALUES (5, NULL, 'Comptuer Science :\'(', 'CSC3003S', '#22A7F0');
INSERT INTO course (semesterID1,semesterID2,courseName,courseCode,colour) 
VALUES (5, NULL, 'Genetics', 'MCB3023S', '#26A65B');
INSERT INTO course (semesterID1,semesterID2,courseName,courseCode,colour) 
VALUES (4, NULL, 'Genetics', 'MCB3026F', '#26A65B');