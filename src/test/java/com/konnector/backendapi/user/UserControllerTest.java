package com.konnector.backendapi.user;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import static com.konnector.backendapi.http.Headers.HEADER_TOTAL_COUNT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
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
	@Mock
	private HttpServletResponse httpServletResponseMock;

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
		when(userServiceMock.updateUser(userMock, 1L, userDTO.getOldPassword())).thenReturn(userMock);
		when(modelMapperMock.map(userMock, UserDTO.class)).thenReturn(userDTO);

		UserDTO result = userController.updateUser(userDTO, 1L);

		assertEquals(userDTO, result);
	}

	@Test
	public void getUser_returnsSuccessAndUser() {
		when(userServiceMock.getUser(userDTO.getId())).thenReturn(userMock);
		when(modelMapperMock.map(userMock, UserDTO.class)).thenReturn(userDTO);

		UserDTO result = userController.getUser(userDTO.getId(), 0);

		userDTO.setPassword(null);
		assertEquals(userDTO, result);
	}

	@Test
	public void getUser_full_returnsSuccessAndFullUser() {
		when(userServiceMock.getUser(userDTO.getId())).thenReturn(userMock);
		when(modelMapperMock.map(userMock, UserDTO.class)).thenReturn(userDTO);

		UserDTO result = userController.getUser(userDTO.getId(), 1);

		userDTO.setPassword(null);
		assertEquals(userDTO, result);
	}

	@Test
	public void getUsers_withUserId_returnsSuccessAndUsers() {
		List<User> users = List.of(userMock);
		List<UserDTO> userDTOs = List.of(userDTO);

		Long userId = 1L;
		String username = "user1";

		when(userServiceMock.getUsers(Optional.of(userId), true, username, 1, 1)).thenReturn(users);
		when(userServiceMock.getUsersCount(Optional.of(userId), true, username)).thenReturn(1L);
		Type listType = new TypeToken<List<UserDTO>>() {}.getType();
		when(modelMapperMock.map(users, listType)).thenReturn(userDTOs);

		List<UserDTO> result = userController.getUsers(userId, 1, 1, username, httpServletResponseMock);

		assertEquals(userDTOs, result);
		verify(httpServletResponseMock).setHeader(HEADER_TOTAL_COUNT, "1");
	}

	@Test
	public void getUsers_withoutUserId_returnsSuccessAndUsers() {
		List<User> users = List.of(userMock);
		List<UserDTO> userDTOs = List.of(userDTO);

		String username = "user1";

		when(userServiceMock.getUsers(Optional.ofNullable(null), false, username, 1, 1)).thenReturn(users);
		when(userServiceMock.getUsersCount(Optional.ofNullable(null), false, username)).thenReturn(1L);
		Type listType = new TypeToken<List<UserDTO>>() {}.getType();
		when(modelMapperMock.map(users, listType)).thenReturn(userDTOs);

		List<UserDTO> result = userController.getUsers(null, 1, 1, username, httpServletResponseMock);

		assertEquals(userDTOs, result);
		verify(httpServletResponseMock).setHeader(HEADER_TOTAL_COUNT, "1");
	}
}
