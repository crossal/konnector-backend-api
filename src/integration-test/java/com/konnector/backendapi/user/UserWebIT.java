package com.konnector.backendapi.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.konnector.backendapi.http.Headers;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@TestPropertySource("classpath:application-integration-test.properties")
public class UserWebIT {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private UserService userServiceMock;
	@MockBean
	private ModelMapper modelMapperMock;
	@MockBean
	private UserDetailsService userDetailsService;
	@Mock
	private User userMock;

	private final ObjectMapper objectMapper = new ObjectMapper().configure(MapperFeature.USE_ANNOTATIONS, false).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	private final EasyRandom easyRandom = new EasyRandom();
	private final UserDTO userDTO = easyRandom.nextObject(UserDTO.class);
	private String userJson;

	@BeforeEach
	public void setup() throws JsonProcessingException {
		userJson = objectMapper.writeValueAsString(userDTO);
	}

	@Test
	public void createUser_returnsSuccessAndCreatedUser() throws Exception {
		when(modelMapperMock.map(any(UserDTO.class), eq(User.class))).thenReturn(userMock);
		when(userServiceMock.createUser(userMock)).thenReturn(userMock);
		when(modelMapperMock.map(userMock, UserDTO.class)).thenReturn(userDTO);

		MvcResult result = mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(userJson)).andExpect(status().isCreated()).andReturn();
		UserDTO userDTOResponse = objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);

		userDTO.setPassword(null);
		userDTO.setOldPassword(null);
		assertEquals(userDTO, userDTOResponse);
	}

	@Test
	public void updateUser_withoutAuthentication_returnsFailure() throws Exception {
		mockMvc.perform(put("/api/users/1").contentType(MediaType.APPLICATION_JSON).content(userJson)).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser
	public void updateUser_returnsSuccessAndUpdatedUser() throws Exception {
		when(modelMapperMock.map(any(UserDTO.class), eq(User.class))).thenReturn(userMock);
		when(userServiceMock.updateUser(userMock, 1L, userDTO.getOldPassword())).thenReturn(userMock);
		when(modelMapperMock.map(userMock, UserDTO.class)).thenReturn(userDTO);

		MvcResult result = mockMvc.perform(put("/api/users/1").contentType(MediaType.APPLICATION_JSON).content(userJson)).andExpect(status().isOk()).andReturn();
		UserDTO userDTOResponse = objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);

		userDTO.setPassword(null);
		userDTO.setOldPassword(null);
		assertEquals(userDTO, userDTOResponse);
	}

	@Test
	public void getUser_withoutAuthentication_returnsFailure() throws Exception {
		mockMvc.perform(get("/api/users/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser
	public void getUser_returnsSuccessAndUser() throws Exception {
		when(userServiceMock.getUser(1L)).thenReturn(userMock);
		when(modelMapperMock.map(userMock, UserDTO.class)).thenReturn(userDTO);

		MvcResult result = mockMvc.perform(get("/api/users/1?view-type=0").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		UserDTO userDTOResponse = objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);

		userDTO.setPassword(null);
		userDTO.setOldPassword(null);
		userDTO.setEmailVerified(null);
		userDTO.setEmail(null);
		assertEquals(userDTO, userDTOResponse);
	}

	@Test
	public void getUser_fullUserWithoutAuthentication_returnsFailure() throws Exception {
		mockMvc.perform(get("/api/users/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser
	public void getUser_fullUser_returnsSuccessAndFullUser() throws Exception {
		when(userServiceMock.getUser(1L)).thenReturn(userMock);
		when(modelMapperMock.map(userMock, UserDTO.class)).thenReturn(userDTO);

		MvcResult result = mockMvc.perform(get("/api/users/1?view-type=1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		UserDTO userDTOResponse = objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);

		userDTO.setPassword(null);
		userDTO.setOldPassword(null);
		assertEquals(userDTO, userDTOResponse);
	}

	@Test
	@WithMockUser
	public void getUsers_returnsSuccessAndUsers() throws Exception {
		List<User> users = List.of(userMock);
		List<UserDTO> userDTOs = List.of(userDTO);
		when(userServiceMock.getUsers(Optional.of(1L), true, "user1", 1, 1)).thenReturn(users);
		when(userServiceMock.getUsersCount(Optional.of(1L), true, "user1")).thenReturn(1L);
		Type listType = new TypeToken<List<UserDTO>>() {}.getType();
		when(modelMapperMock.map(users, listType)).thenReturn(userDTOs);

		MvcResult result = mockMvc.perform(get("/api/users?connections-of-user-id=1&page-number=1&page-size=1&username=user1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		List<UserDTO> userDTOsResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

		userDTO.setPassword(null);
		userDTO.setOldPassword(null);
		assertEquals(userDTOs, userDTOsResponse);
		assertEquals("1", result.getResponse().getHeader(Headers.HEADER_TOTAL_COUNT));
	}
}
