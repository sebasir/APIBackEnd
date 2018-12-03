package com.backend.api.service;

import com.backend.api.model.PasswordReset;
import com.backend.api.model.User;

public interface SendMailService {
	void sendResetPasswordMail(User user, PasswordReset passwordReset) throws Exception;
}
