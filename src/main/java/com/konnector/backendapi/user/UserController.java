package com.konnector.backendapi.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.konnector.backendapi.http.Headers;
import com.konnector.backendapi.http.Views;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class UserController {

	private static final Logger LOGGER = LogManager.getLogger(UserController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private ModelMapper modelMapper;

	@PostMapping("/api/users")
	@ResponseStatus(HttpStatus.CREATED)
	@JsonView(Views.Private.class)
	public UserDTO createUser(@RequestBody UserDTO userDTO) {
		User user = modelMapper.map(userDTO, User.class);

		user = userService.createUser(user);

		return modelMapper.map(user, UserDTO.class);
	}

	@PutMapping("/api/users/{id}")
	@ResponseStatus(HttpStatus.OK)
	@JsonView(Views.Private.class)
	public UserDTO updateUser(@RequestBody UserDTO userDTO, @PathVariable("id") Long userId) {
		User user = modelMapper.map(userDTO, User.class);

		user = userService.updateUser(user, userId, userDTO.getOldPassword());

		return modelMapper.map(user, UserDTO.class);
	}

	@GetMapping(value = "/api/users/{id}")
	@ResponseStatus(HttpStatus.OK)
	@JsonView(Views.Public.class)
	public UserDTO getUser(@PathVariable("id") Long userId, @RequestParam(name = "view-type", required = false) Integer viewType) {
		User user = userService.getUser(userId);

		return modelMapper.map(user, UserDTO.class);
	}

	@GetMapping(value = "/api/users/{id}", params = { "view-type=1" })
	@ResponseStatus(HttpStatus.OK)
	@JsonView(Views.Private.class)
	public UserDTO getFullUser(@PathVariable("id") Long userId, @RequestParam("view-type") Integer viewType) {
		User user = userService.getUser(userId);

		return modelMapper.map(user, UserDTO.class);
	}

	@GetMapping(value = "/api/users", params = {"connections-of-user-id", "page-number", "page-size"})
	@ResponseStatus(HttpStatus.OK)
	public List<UserDTO> getConnectedUsers(@RequestParam("connections-of-user-id") Long connectionsOfUserId, @RequestParam("page-number") Integer pageNumber,
	                                       @RequestParam("page-size") Integer pageSize,
	                                       HttpServletResponse response) {
		List<User> connections = userService.getConnections(connectionsOfUserId, pageNumber, pageSize);

		long totalConnectionsCount = userService.getConnectionsCount(connectionsOfUserId);
		response.setHeader(Headers.HEADER_TOTAL_COUNT, String.valueOf(totalConnectionsCount));

		return modelMapper.map(connections, new TypeToken<List<UserDTO>>() {}.getType());
	}

	@GetMapping(value = "/api/users", params = {"page-number", "page-size"})
	@ResponseStatus(HttpStatus.OK)
	public List<UserDTO> getUsers(@RequestParam("page-number") Integer pageNumber, @RequestParam("page-size") Integer pageSize,
	                                       HttpServletResponse response) {
		List<User> users = userService.getUsers(pageNumber, pageSize);

		long totalUsersCount = userService.getUsersCount();
		response.setHeader(Headers.HEADER_TOTAL_COUNT, String.valueOf(totalUsersCount));

		return modelMapper.map(users, new TypeToken<List<UserDTO>>() {}.getType());
	}
}
