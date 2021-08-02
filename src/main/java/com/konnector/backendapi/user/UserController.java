package com.konnector.backendapi.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	private static final Logger LOGGER = LogManager.getLogger(UserController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private ModelMapper modelMapper;

	@PostMapping("/api/users")
	@ResponseStatus(HttpStatus.CREATED)
	public UserDTO createUser(@RequestBody UserDTO userDTO) {
		User user = modelMapper.map(userDTO, User.class);

		user = userService.createUser(user);

		return modelMapper.map(user, UserDTO.class);
	}

	@GetMapping("/api/users/{id}")
	@ResponseStatus(HttpStatus.OK)
	public UserDTO getUser(@PathVariable("id") String userId) {
		User user = userService.getUser(Long.parseLong(userId));

		return modelMapper.map(user, UserDTO.class);
	}
}
