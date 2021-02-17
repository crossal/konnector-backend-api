package com.konnector.backendapi.login;

import com.konnector.backendapi.user.User;
import com.konnector.backendapi.user.UserDTO;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

	@InjectMocks
	private LoginController loginController = new LoginController();

	@Mock
	private LoginService loginServiceMock;
	@Mock
	private ModelMapper modelMapperMock;
	@Mock
	private User userMock;
	@Mock
	private UserDTO userDTOMock;

	private final EasyRandom easyRandom = new EasyRandom();
	private final AuthenticationDTO authenticationDTO = easyRandom.nextObject(AuthenticationDTO.class);

	@Test
	public void login_logsInUser() {
		when(loginServiceMock.login(authenticationDTO.getUsernameOrEmail(), authenticationDTO.getPassword())).thenReturn(userMock);
		when(modelMapperMock.map(userMock, UserDTO.class)).thenReturn(userDTOMock);

		UserDTO result = loginController.login(authenticationDTO);

		assertEquals(userDTOMock, result);
	}
}
