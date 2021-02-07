INSERT INTO `user` (`email`, `username`, `password`, `first_name`, `last_name`, `is_email_verified`) VALUES ('email', 'username', '$2a$11$BHG59UT6p7bgT6U2fQ/9wOyTIdejh4Rk1vWilvl4b6ysNPdhnViUS', 'first_name', 'last_name', false);
SELECT @user_id := (SELECT `id` FROM `user` WHERE `email` = 'email');
INSERT INTO `verification` (`user_id`, `type`, `status`, `code`, `code_attempts_left`, `url_token`, `expires_on`, `reverify_allowed_on`) VALUES (@user_id, 0, 0, '123456', 1, 'token', DATE_ADD(CURRENT_DATE, INTERVAL 2 DAY), DATE_SUB(CURRENT_DATE, INTERVAL 2 DAY));

INSERT INTO `user` (`email`, `username`, `password`, `first_name`, `last_name`, `is_email_verified`) VALUES ('email_2', 'username_2', '$2a$11$BHG59UT6p7bgT6U2fQ/9wOyTIdejh4Rk1vWilvl4b6ysNPdhnViUS', 'first_name_2', 'last_name_2', false);
SELECT @user_id := (SELECT `id` FROM `user` WHERE `email` = 'email_2');
INSERT INTO `verification` (`user_id`, `type`, `status`, `code`, `code_attempts_left`, `url_token`, `expires_on`, `reverify_allowed_on`) VALUES (@user_id, 0, 0, '123456', 1, 'token', DATE_ADD(CURRENT_DATE, INTERVAL 2 DAY), DATE_ADD(CURRENT_DATE, INTERVAL 2 DAY));