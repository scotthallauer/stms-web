USE stms;

SET @userID = 1;

SELECT T.type FROM (
(SELECT sessionType AS type
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
    ON courseSession.courseID = C.courseID
    GROUP BY courseSession.sessionType
    ORDER BY courseSession.sessionType ASC)
UNION
(SELECT 'lecture' AS type)
UNION
(SELECT 'tutorial' AS type)
UNION
(SELECT 'practical' AS type)
UNION
(SELECT 'test' AS type)
UNION
(SELECT 'exam' AS type)
) AS T
ORDER BY T.type ASC;