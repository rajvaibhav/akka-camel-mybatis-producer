CREATE DEFINER=`root`@`localhost` PROCEDURE `getPerson`(IN email VARCHAR(45))
BEGIN
   SELECT * FROM test.person
   WHERE PERSON_EMAIL like email;
END$$
