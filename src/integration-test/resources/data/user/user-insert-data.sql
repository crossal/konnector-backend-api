INSERT INTO `user` (`email`, `username`, `password`, `first_name`, `last_name`, `is_email_verified`)
VALUES
	('test@email.com', 'username', 'password', 'first_name', 'last_name', true),
	('test2@email.com', 'username2', 'password2', 'first_name2', 'last_name3', true),
	('test3@email.com', 'username3', 'password3', 'first_name3', 'last_name4', true),
	('test4@email.com', 'username4', 'password4', 'first_name4', 'last_name5', true),
	('test5@email.com', 'username5', 'password5', 'first_name5', 'last_name6', true);

INSERT INTO `connection`
	(`requester_id`, `requestee_id`, `status`)
VALUES
	(1, 2, 1),
	(3, 1, 1);