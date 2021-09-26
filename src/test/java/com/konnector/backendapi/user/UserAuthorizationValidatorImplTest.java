package com.konnector.backendapi.user;

import com.konnector.backendapi.exceptions.UnauthorizedException;
import com.konnector.backendapi.security.SecurityUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserAuthorizationValidatorImplTest {

	private final UserAuthorizationValidator userAuthorizationValidator = new UserAuthorizationValidatorImpl();

	@Mock
	private Authentication authenticationMock;
	@Mock
	private SecurityUser securityUserMock;

	@Test
	public void validateUserRequest_userIdNotEqualToAuthenticatedUserId_throwsException() {
		when(authenticationMock.getPrincipal()).thenReturn(securityUserMock);
		when(securityUserMock.getUserId()).thenReturn(2L);
		assertThrows(UnauthorizedException.class, () -> userAuthorizationValidator.validateUserRequest(1L, authenticationMock));
	}

	@Test
	public void validateUserRequest_userIdNotEqualToAuthenticatedUserId_doesNotThrowException() {
		when(authenticationMock.getPrincipal()).thenReturn(securityUserMock);
		when(securityUserMock.getUserId()).thenReturn(1L);
		userAuthorizationValidator.validateUserRequest(1L, authenticationMock);
	}
}
