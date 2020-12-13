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
	private UserService userServiceMock;
	@Mock
	private ModelMapper modelMapperMock;
	@Mock
	private User userMock;

	private final PodamFactory podamFactory = new PodamFactoryImpl();
	private final UserDTO userDTO = podamFactory.manufacturePojo(UserDTO.class);

	@Test
	public void createUser_returnsSuccessAndCreatedUser() {
		when(modelMapperMock.map(any(UserDTO.class), eq(User.class))).thenReturn(userMock);
		when(userServiceMock.createUser(userMock, userDTO.getPassword())).thenReturn(userMock);
		when(modelMapperMock.map(userMock, UserDTO.class)).thenReturn(userDTO);

		UserDTO result = userController.createUser(userDTO);

		userDTO.setPassword(null);
		assertEquals(userDTO, result);
	}

	@Test
	public void getUser_returnsSuccessAndUser() {
		when(userServiceMock.getUser(userDTO.getId())).thenReturn(userMock);
		when(modelMapperMock.map(userMock, UserDTO.class)).thenReturn(userDTO);

		UserDTO result = userController.getUser(userDTO.getId());

		userDTO.setPassword(null);
		assertEquals(userDTO, result);
	}
}
