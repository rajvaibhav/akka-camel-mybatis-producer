CREATE DEFINER=`root`@`localhost` PROCEDURE `select_persons`()
begin
select * from test.person;
end$$
