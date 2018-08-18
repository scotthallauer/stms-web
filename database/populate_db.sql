USE stms;

INSERT INTO user (firstName, lastName, email, activated, pwdHash, pwdSalt) 
values ('Scott', 'Hallauer', 'scott.hallauer@gmail.com', 1, '7eb9f680bf2c5914ff32d5acca123aa7ed4f82f8572ab6b12a7aec4edc8610e0', 'gh90845hg093hqp'); 
# password is password

INSERT INTO user (firstName, lastName, email, activated, pwdHash, pwdSalt) 
VALUES ('Jonathon', 'Everatt', 'EVRJON003@myuct.ac.za',1,'5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5','5');
#password is 1234

INSERT INTO user (firstName, lastName, email, activated, pwdHash, pwdSalt) 
VALUES ('Jessica','Bourn','BRNJES018@myuct.ac.za',0,'18650c5730d77b32f0910983fdff30c75f91eeaa70378a51c35240249f3fbf81','81552974381271');
#password is p3gasus 

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