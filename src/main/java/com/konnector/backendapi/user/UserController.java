package com.konnector.backendapi.user;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private ModelMapper modelMapper;

	@PostMapping("/api/users")
	@ResponseStatus(HttpStatus.CREATED)
	public UserDTO createUser(@RequestBody UserDTO userDTO) {
		User user = modelMapper.map(userDTO, User.class);
		user = userService.createUser(user, userDTO.getPassword());
		return modelMapper.map(user, UserDTO.class);
	}
}
