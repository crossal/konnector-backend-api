package com.konnector.backendapi.login;

import com.fasterxml.jackson.annotation.JsonView;
import com.konnector.backendapi.http.Views;
import com.konnector.backendapi.user.User;
import com.konnector.backendapi.user.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

	@Autowired
	private LoginService loginService;
	@Autowired
	private ModelMapper modelMapper;

	@PostMapping("/api/authenticate")
	@ResponseStatus(HttpStatus.OK)
	@JsonView(Views.Private.class)
	public UserDTO login(@RequestBody AuthenticationDTO authenticationDTO) {
		User user = loginService.login(authenticationDTO.getUsernameOrEmail(), authenticationDTO.getPassword());

		return modelMapper.map(user, UserDTO.class);
	}

	@PostMapping("/api/deauthenticate")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String logout() {
		loginService.logout();
		return "Ok";
	}
}
