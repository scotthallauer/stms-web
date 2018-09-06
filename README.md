# Student Time Management System
Most students constantly feel under pressure. They typically feel that they don't have sufficient time to
keep up with their coursework, complete their assignments, and study for tests and exams. Often they
lack the skills to adequately manage their time, or they perceive implementing a time management
system as being unnecessarily administratively cumbersome. So the proposed Student Time Management
System will automate much of the work for them and simplify the process. It should allow students to
document their academic deadlines for assignments and tests (on a monthly or semester basis), specify a
weekly study planner (identifying time for studying), and manage a daily to do list. Based on this
information, it should advise students about how they should productively spend their time, so e.g. today
you should be completing your MAM1000W Assignment due on Thursday and studying for the
CSC1015F PracTest on Friday. The system should be web-based so students can access it from anywhere.


Runtime enviroment setup

Required software
	
	IntelliJ Ultimate edition. - purchasable or free with valid student email address
	XAMPP
	MySQL - Must be able to create and store databases

Instructions
	
1) Start XAMPP and click start on the MySQL module. This will connect to port 3306.
2) Create a connection to a MySQL database using XAMPP.	
2.1) Connection must be to port 3306 as seen on XAMPP.
3) Run create_db.sql script found in the database folder of the project.
4) run test_populate_db.sql script found in database. (Optional)
5) Open IntelliJ
6) Click open project
7) select directory for this project.
	Directory should be shown as an IntelliJ project.
8) Click run with configuration Tomcat 7.0.56
9) Become more productive with Monthly Mentor