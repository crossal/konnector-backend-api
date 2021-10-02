INSERT INTO `user` (`email`, `username`, `password`, `first_name`, `last_name`, `is_email_verified`)
VALUES ('test@email.com', 'username', 'password', 'first_name', 'last_name', true);

INSERT INTO `contact_detail` (`user_id`, `name`, `value`)
VALUES (1, 'f', 'some value');

INSERT INTO `contact_detail` (`user_id`, `name`, `value`)
VALUES (1, 'e', 'some value');

INSERT INTO `contact_detail` (`user_id`, `name`, `value`)
VALUES (1, 'a', 'some value');

INSERT INTO `contact_detail` (`user_id`, `name`, `value`)
VALUES (1, 'b', 'some value');

INSERT INTO `contact_detail` (`user_id`, `name`, `value`)
VALUES (1, 'd', 'some value');

INSERT INTO `contact_detail` (`user_id`, `name`, `value`)
VALUES (1, 'c', 'some value');
