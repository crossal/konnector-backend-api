package com.konnector.backendapi.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

	@InjectMocks
	private UserController userController = new UserController();

	@Mock
	private UserService userService;
	@Mock
	private ModelMapper modelMapper;
	@Mock
	private User user;

	private final PodamFactory podamFactory = new PodamFactoryImpl();
	private final UserDTO userDTO = podamFactory.manufacturePojo(UserDTO.class);

	@Test
	public void createUser_returnsSuccessAndCreatedUser() {
		when(modelMapper.map(any(UserDTO.class), eq(User.class))).thenReturn(user);
		when(userService.createUser(user, userDTO.getPassword())).thenReturn(user);
		when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

		UserDTO result = userController.createUser(userDTO);

		userDTO.setPassword(null);
		assertEquals(userDTO, result);
	}
}
