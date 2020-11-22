SELECT @user_id := (SELECT `id` FROM `user` WHERE `email` = 'verification_test_email');
DELETE FROM `verification` WHERE `user_id` = @user_id;
DELETE FROM `user` WHERE `id` = @user_id;