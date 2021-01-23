package com.konnector.backendapi.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
		assertEquals(userDTO, userDTOResponse);
	}

	@Test
	public void getUser_withoutAuthentication_returnsFailure() throws Exception {
		mockMvc.perform(get("/api/users/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser
	public void getUser_returnsSuccessAndUser() throws Exception {
		when(userServiceMock.getUser(1L)).thenReturn(userMock);
		when(modelMapperMock.map(userMock, UserDTO.class)).thenReturn(userDTO);

		MvcResult result = mockMvc.perform(get("/api/users/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
		UserDTO userDTOResponse = objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);

		userDTO.setPassword(null);
		assertEquals(userDTO, userDTOResponse);
	}
}
