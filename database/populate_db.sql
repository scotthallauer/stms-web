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

#Semester inserts
INSERT INTO semester (userID,semesterName,startDate,endDate) 
VALUES (1,'Jan-June 2018', 20180119, 20180624);

INSERT INTO semester (userID,semesterName,startDate,endDate) 
VALUES (1, 'July-Dec', 20180724, 20181130);

INSERT INTO semester (userID,semesterName,startDate,endDate) 
VALUES (2,'Jan-June 2018', 20180119, 20180624);

INSERT INTO semester (userID,semesterName,startDate,endDate) 
VALUES (3, 'Jan-June 2018', 20180119, 20180624);

INSERT INTO semester (userID,semesterName,startDate,endDate) 
VALUES (3,  'July-Dec', 20180724, 20181130);

#course inserts
INSERT INTO course (semesterID,courseName,courseCode,semester1,semester2) 
VALUES (1,'Comsci', 'CSC3002F',1,NULL);
INSERT INTO course (semesterID,courseName,courseCode,semester1,semester2) 
VALUES (1, 'Genetics', 'MCB 3001 F',1, NULL);
INSERT INTO course (semesterID,courseName,courseCode,semester1,semester2) 
VALUES (2, 'Comsci', 'CSC3003S', 2, NULL);
INSERT INTO course (semesterID,courseName,courseCode,semester1,semester2) 
VALUES (2, 'Genetics', 'MCB3023S',2, NULL);
INSERT INTO course (semesterID,courseName,courseCode,semester1,semester2) 
VALUES (3, 'Philosophy', 'PHI3023F', 3, NULL);
INSERT INTO course (semesterID,courseName,courseCode,semester1,semester2) 
VALUES (3, 'Comsci', 'CSC3002F', 3, NULL);
INSERT INTO course (semesterID,courseName,courseCode,semester1,semester2) 
VALUES (4, 'Comsci', 'CSC3002F', 4, NULL);
INSERT INTO course (semesterID,courseName,courseCode,semester1,semester2) 
VALUES (5, 'Comsci', 'CSC3003S', 5, NULL);
INSERT INTO course (semesterID,courseName,courseCode,semester1,semester2) 
VALUES (5, 'Genetics', 'MCB3023S',5, NULL);
INSERT INTO course (semesterID,courseName,courseCode,semester1,semester2) 
VALUES (4, 'Genetics', 'MCB 3001 F',4, NULL);

#CourseSession inserts
INSERT INTO coursesession (courseID,sessionName,sessionType,startTime,endTime,location,rrule,note)
VALUES (1, 'Lecture', 'Lecture', '2018-03-06 20:11:00',  '2018-03-06 10:00','Menizies 10','1','' );
INSERT INTO coursesession (courseID,sessionName,sessionType,startTime,endTime,location,rrule,note)
VALUES (1, 'Lecture', 'Lecture', '2018-03-07 20:11:00',  '2018-03-07 10:00','Menizies 10','1','' );
INSERT INTO coursesession (courseID,sessionName,sessionType,startTime,endTime,location,rrule,note)
VALUES (1, 'Lecture', 'Lecture', '2018-03-08 20:11:00',  '2018-03-08 10:00','Menizies 10','1','' );
INSERT INTO coursesession (courseID,sessionName,sessionType,startTime,endTime,location,rrule,note)
VALUES (1, 'Lecture', 'Lecture', '2018-03-09 20:11:00',  '2018-03-09 10:00','Menizies 10','1','' );
INSERT INTO coursesession (courseID,sessionName,sessionType,startTime,endTime,location,rrule,note)
VALUES (1, 'Lecture', 'Lecture', '2018-03-12 20:11:00',  '2018-03-12 10:00','Menizies 10','1','' );

INSERT INTO coursesession (courseID,sessionName,sessionType,startTime,endTime,location,rrule,note)
VALUES (3, 'Lecture', 'Lecture', '2018-08-06 20:11:00',  '2018-08-06 10:00','Menizies 10','1','' );
INSERT INTO coursesession (courseID,sessionName,sessionType,startTime,endTime,location,rrule,note)
VALUES (3, 'Lecture', 'Lecture', '2018-08-07 20:11:00',  '2018-08-07 10:00','Menizies 10','1','' );
INSERT INTO coursesession (courseID,sessionName,sessionType,startTime,endTime,location,rrule,note)
VALUES (3, 'Lecture', 'Lecture', '2018-08-08 20:11:00',  '2018-08-08 10:00','Menizies 10','1','' );
INSERT INTO coursesession (courseID,sessionName,sessionType,startTime,endTime,location,rrule,note)
VALUES (3, 'Lecture', 'Lecture', '2018-08-09 20:11:00',  '2018-08-09 10:00','Menizies 10','1','' );
INSERT INTO coursesession (courseID,sessionName,sessionType,startTime,endTime,location,rrule,note)
VALUES (3, 'Lecture', 'Lecture', '2018-08-12 20:11:00',  '2018-08-12 10:00','Menizies 10','1','' );

INSERT INTO coursesession (courseID,sessionName,sessionType,startTime,endTime,location,rrule,note)
VALUES (6, 'Lecture', 'Lecture', '2018-08-06 20:11:00',  '2018-08-06 10:00','Menizies 10','1','' );
INSERT INTO coursesession (courseID,sessionName,sessionType,startTime,endTime,location,rrule,note)
VALUES (6, 'Lecture', 'Lecture', '2018-08-07 20:11:00',  '2018-08-07 10:00','Menizies 10','1','' );
INSERT INTO coursesession (courseID,sessionName,sessionType,startTime,endTime,location,rrule,note)
VALUES (6, 'Lecture', 'Lecture', '2018-08-08 20:11:00',  '2018-08-08 10:00','Menizies 10','1','' );
INSERT INTO coursesession (courseID,sessionName,sessionType,startTime,endTime,location,rrule,note)
VALUES (6, 'Lecture', 'Lecture', '2018-08-09 20:11:00',  '2018-08-09 10:00','Menizies 10','1','' );
INSERT INTO coursesession (courseID,sessionName,sessionType,startTime,endTime,location,rrule,note)
VALUES (6, 'Lecture', 'Lecture', '2018-08-12 20:11:00',  '2018-08-12 10:00','Menizies 10','1','' );

INSERT INTO coursesession (courseID,sessionName,sessionType,startTime,endTime,location,rrule,note)
VALUES (7, 'Lecture', 'Lecture', '2018-03-06 20:11:00',  '2018-03-06 10:00','Menizies 10','1','' );
INSERT INTO coursesession (courseID,sessionName,sessionType,startTime,endTime,location,rrule,note)
VALUES (7, 'Lecture', 'Lecture', '2018-03-07 20:11:00',  '2018-03-07 10:00','Menizies 10','1','' );
INSERT INTO coursesession (courseID,sessionName,sessionType,startTime,endTime,location,rrule,note)
VALUES (7, 'Lecture', 'Lecture', '2018-03-08 20:11:00',  '2018-03-08 10:00','Menizies 10','1','' );
INSERT INTO coursesession (courseID,sessionName,sessionType,startTime,endTime,location,rrule,note)
VALUES (7, 'Lecture', 'Lecture', '2018-03-09 20:11:00',  '2018-03-09 10:00','Menizies 10','1','' );
INSERT INTO coursesession (courseID,sessionName,sessionType,startTime,endTime,location,rrule,note)
VALUES (7, 'Lecture', 'Lecture', '2018-03-12 20:11:00',  '2018-03-12 10:00','Menizies 10','1','' );

INSERT INTO coursesession (courseID,sessionName,sessionType,startTime,endTime,location,rrule,note)
VALUES (8, 'Lecture', 'Lecture', '2018-08-06 20:11:00',  '2018-08-06 10:00','Menizies 10','1','' );
INSERT INTO coursesession (courseID,sessionName,sessionType,startTime,endTime,location,rrule,note)
VALUES (8, 'Lecture', 'Lecture', '2018-08-07 20:11:00',  '2018-08-07 10:00','Menizies 10','1','' );
INSERT INTO coursesession (courseID,sessionName,sessionType,startTime,endTime,location,rrule,note)
VALUES (8, 'Lecture', 'Lecture', '2018-08-08 20:11:00',  '2018-08-08 10:00','Menizies 10','1','' );
INSERT INTO coursesession (courseID,sessionName,sessionType,startTime,endTime,location,rrule,note)
VALUES (8, 'Lecture', 'Lecture', '2018-08-09 20:11:00',  '2018-08-09 10:00','Menizies 10','1','' );
INSERT INTO coursesession (courseID,sessionName,sessionType,startTime,endTime,location,rrule,note)
VALUES (8, 'Lecture', 'Lecture', '2018-08-12 20:11:00',  '2018-08-12 10:00','Menizies 10','1','' );



