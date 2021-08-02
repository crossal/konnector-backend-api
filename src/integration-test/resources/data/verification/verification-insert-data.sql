-- Account verification
INSERT INTO `user` (`email`, `username`, `password`, `first_name`, `last_name`, `is_email_verified`) VALUES ('email', 'username', '$2a$12$sqWRJxiHKNC63bpSHZFrG.muOCeMwX2iwDGXYp/BNGNTOtzpSk9Dm', 'first_name', 'last_name', false);
SELECT @user_id := (SELECT `id` FROM `user` WHERE `email` = 'email');
INSERT INTO `verification` (`user_id`, `type`, `status`, `code`, `code_attempts_left`, `url_token`, `expires_on`, `reverify_allowed_on`) VALUES (@user_id, 0, 0, '123456', 1, 'token', DATE_ADD(CURRENT_DATE, INTERVAL 2 DAY), DATE_SUB(CURRENT_DATE, INTERVAL 2 DAY));

-- Account verification not allowed to reverify yet
INSERT INTO `user` (`email`, `username`, `password`, `first_name`, `last_name`, `is_email_verified`) VALUES ('email_2', 'username_2', '$2a$12$sqWRJxiHKNC63bpSHZFrG.muOCeMwX2iwDGXYp/BNGNTOtzpSk9Dm', 'first_name_2', 'last_name_2', false);
SELECT @user_id := (SELECT `id` FROM `user` WHERE `email` = 'email_2');
INSERT INTO `verification` (`user_id`, `type`, `status`, `code`, `code_attempts_left`, `url_token`, `expires_on`, `reverify_allowed_on`) VALUES (@user_id, 0, 0, '123456', 1, 'token', DATE_ADD(CURRENT_DATE, INTERVAL 2 DAY), DATE_ADD(CURRENT_DATE, INTERVAL 2 DAY));

-- Reset password
INSERT INTO `user` (`email`, `username`, `password`, `first_name`, `last_name`, `is_email_verified`) VALUES ('email_3', 'username_3', '$2a$12$sqWRJxiHKNC63bpSHZFrG.muOCeMwX2iwDGXYp/BNGNTOtzpSk9Dm', 'first_name_3', 'last_name_3', false);
SELECT @user_id := (SELECT `id` FROM `user` WHERE `email` = 'email_3');
INSERT INTO `verification` (`user_id`, `type`, `status`, `code`, `code_attempts_left`, `url_token`, `expires_on`, `reverify_allowed_on`) VALUES (@user_id, 1, 0, '123456', 1, 'token', DATE_ADD(CURRENT_DATE, INTERVAL 2 DAY), DATE_SUB(CURRENT_DATE, INTERVAL 2 DAY));

-- Reset password not allowed again yet
INSERT INTO `user` (`email`, `username`, `password`, `first_name`, `last_name`, `is_email_verified`) VALUES ('email_4', 'username_4', '$2a$12$sqWRJxiHKNC63bpSHZFrG.muOCeMwX2iwDGXYp/BNGNTOtzpSk9Dm', 'first_name_4', 'last_name_4', false);
SELECT @user_id := (SELECT `id` FROM `user` WHERE `email` = 'email_4');
INSERT INTO `verification` (`user_id`, `type`, `status`, `code`, `code_attempts_left`, `url_token`, `expires_on`, `reverify_allowed_on`) VALUES (@user_id, 1, 0, '123456', 1, 'token', DATE_ADD(CURRENT_DATE, INTERVAL 2 DAY), DATE_ADD(CURRENT_DATE, INTERVAL 2 DAY));

-- Reset password
INSERT INTO `user` (`email`, `username`, `password`, `first_name`, `last_name`, `is_email_verified`) VALUES ('email_5', 'username_5', '$2a$12$sqWRJxiHKNC63bpSHZFrG.muOCeMwX2iwDGXYp/BNGNTOtzpSk9Dm', 'first_name_5', 'last_name_5', false);
SELECT @user_id := (SELECT `id` FROM `user` WHERE `email` = 'email_5');
INSERT INTO `verification` (`user_id`, `type`, `status`, `code`, `code_attempts_left`, `url_token`, `expires_on`, `reverify_allowed_on`) VALUES (@user_id, 1, 0, '123456', 1, 'token', DATE_ADD(CURRENT_DATE, INTERVAL 2 DAY), DATE_SUB(CURRENT_DATE, INTERVAL 2 DAY));