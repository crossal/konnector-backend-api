package com.konnector.backendapi.user;

import com.konnector.backendapi.data.Dao;
import com.konnector.backendapi.notifications.EmailNotificationService;
import com.konnector.backendapi.security.password.HashedPassword;
import com.konnector.backendapi.security.password.PasswordHashingService;
import com.konnector.backendapi.verification.Verification;
import com.konnector.backendapi.verification.VerificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

	@InjectMocks
	private final UserService userService = new UserServiceImpl();

	@Mock
	private Dao<User> userDaoMock;
	@Mock
	private UserRepository userRepositoryMock;
	@Mock
	private UserValidator userValidatorMock;
	@Mock
	private VerificationService verificationServiceMock;
	@Mock
	private PasswordHashingService passwordHashingServiceMock;
	@Mock
	private EmailNotificationService emailNotificationServiceMock;

	@Mock
	private HashedPassword hashedPasswordMock;
	@Mock
	private Verification verificationMock;

	private final PodamFactory podamFactory = new PodamFactoryImpl();
	private final User user = podamFactory.manufacturePojo(User.class);
	private final String password = "password";
	private final Random random = new Random();
	private byte[] hash;
	private byte[] salt;
	private final String verificationCode = "1234";
	private final String verificationUrlToken = "5678";

	@BeforeEach
	public void setup() {
		hash = new byte[16];
		random.nextBytes(hash);
		salt = new byte[16];
		random.nextBytes(salt);

		when(hashedPasswordMock.getHash()).thenReturn(hash);
		when(hashedPasswordMock.getSalt()).thenReturn(salt);
		when(passwordHashingServiceMock.getHashedPassword(password)).thenReturn(hashedPasswordMock);
		when(verificationMock.getCode()).thenReturn(verificationCode);
		when(verificationMock.getUrlToken()).thenReturn(verificationUrlToken);
		when(verificationServiceMock.createVerificationForUser(anyLong())).thenReturn(verificationMock);
		doAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
				ReflectionTestUtils.setField(user, "id", 1L);
				return null;
			}
		}).when(userDaoMock).save(user);
	}

	@Test
	public void createUser_createsUser() {
		User createdUser = userService.createUser(user, password);

		verify(userValidatorMock, times(1)).validateUserCreationArgument(user);
		verify(userDaoMock, times(1)).save(user);
		verify(emailNotificationServiceMock, times(1)).sendVerificationEmail(user.getEmail(), verificationMock.getCode(), verificationMock.getUrlToken());
		assertEquals(user, createdUser);
		assertEquals(hash, user.getPassword());
		assertEquals(salt, user.getSalt());
	}
}
