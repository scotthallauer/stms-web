USE stms;

SET @userID = 1;

SELECT startDate, endDate, recType, length
	FROM courseSession
    INNER JOIN
	(
		SELECT courseID
		FROM course
		INNER JOIN semester
		ON course.semesterID1 = semester.semesterID
		WHERE 
		semester.userID = @userID
	) AS C
    ON courseSession.courseID = C.courseID;