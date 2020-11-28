package com.konnector.backendapi.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
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
	@Mock
	private User userMock;

	private final ObjectMapper objectMapper = new ObjectMapper().configure(MapperFeature.USE_ANNOTATIONS, false).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	private final PodamFactory podamFactory = new PodamFactoryImpl();
	private final UserDTO userDTO = podamFactory.manufacturePojo(UserDTO.class);
	private String userJson;

	@BeforeEach
	public void setup() throws JsonProcessingException {
		userJson = objectMapper.writeValueAsString(userDTO);
	}

	@Test
	public void createUser_returnsSuccessAndCreatedUser() throws Exception {
		when(modelMapperMock.map(any(UserDTO.class), eq(User.class))).thenReturn(userMock);
		when(userServiceMock.createUser(userMock, userDTO.getPassword())).thenReturn(userMock);
		when(modelMapperMock.map(userMock, UserDTO.class)).thenReturn(userDTO);

		MvcResult result = mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(userJson)).andExpect(status().isCreated()).andReturn();
		UserDTO userDTOResponse = objectMapper.readValue(result.getResponse().getContentAsString(), UserDTO.class);

		userDTO.setPassword(null);
		assertEquals(userDTO, userDTOResponse);
	}
}
