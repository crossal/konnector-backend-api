package com.konnector.backendapi.user;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

	@InjectMocks
	private UserController userController = new UserController();

	@Mock
	private UserService userServiceMock;
	@Mock
	private ModelMapper modelMapperMock;
	@Mock
	private User userMock;

	private final EasyRandom easyRandom = new EasyRandom();
	private final UserDTO userDTO = easyRandom.nextObject(UserDTO.class);

	@Test
	public void createUser_returnsSuccessAndCreatedUser() {
		when(modelMapperMock.map(any(UserDTO.class), eq(User.class))).thenReturn(userMock);
		when(userServiceMock.createUser(userMock)).thenReturn(userMock);
		when(modelMapperMock.map(userMock, UserDTO.class)).thenReturn(userDTO);

		UserDTO result = userController.createUser(userDTO);

		userDTO.setPassword(null);
		assertEquals(userDTO, result);
	}

	@Test
	public void updateUser_returnsSuccessAndUpdatedUser() {
		when(modelMapperMock.map(any(UserDTO.class), eq(User.class))).thenReturn(userMock);
		when(userServiceMock.updateUser(userMock)).thenReturn(userMock);
		when(modelMapperMock.map(userMock, UserDTO.class)).thenReturn(userDTO);

		UserDTO result = userController.updateUser(userDTO);

		assertEquals(userDTO, result);
	}

	@Test
	public void getUser_returnsSuccessAndUser() {
		when(userServiceMock.getUser(userDTO.getId())).thenReturn(userMock);
		when(modelMapperMock.map(userMock, UserDTO.class)).thenReturn(userDTO);

		UserDTO result = userController.getUser(userDTO.getId().toString());

		userDTO.setPassword(null);
		assertEquals(userDTO, result);
	}
}
