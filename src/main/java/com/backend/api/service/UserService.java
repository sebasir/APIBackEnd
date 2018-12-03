package com.backend.api.service;

import com.backend.api.dto.ResponseObject;
import com.backend.api.dto.UserForm;

public interface UserService {
	ResponseObject saveUser(UserForm userForm, Long imcNumber);

	ResponseObject updateUserPassword(UserForm userForm, Long imcNumber, String rol);

	ResponseObject restorePassword(Long imcNumber, String rol);

}
