package com.konnector.backendapi.connection;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConnectionController.class)
@TestPropertySource("classpath:application-integration-test.properties")
public class ConnectionWebIT {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ConnectionService connectionServiceMock;
	@MockBean
	private ModelMapper modelMapperMock;
	@MockBean
	private UserDetailsService userDetailsService;
	@Mock
	private Connection connectionMock;

	private final ObjectMapper objectMapper = new ObjectMapper().configure(MapperFeature.USE_ANNOTATIONS, false).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	private final EasyRandom easyRandom = new EasyRandom();
	private final ConnectionDTO connectionDTO = easyRandom.nextObject(ConnectionDTO.class);
	private String connectionJson;

	@BeforeEach
	public void setup() throws JsonProcessingException {
		connectionJson = objectMapper.writeValueAsString(connectionDTO);
	}

	@Test
	@WithMockUser
	public void createConnection_returnsSuccessAndCreatedConnection() throws Exception {
		when(modelMapperMock.map(any(ConnectionDTO.class), eq(Connection.class))).thenReturn(connectionMock);
		when(connectionServiceMock.createConnection(connectionMock)).thenReturn(connectionMock);
		when(modelMapperMock.map(connectionMock, ConnectionDTO.class)).thenReturn(connectionDTO);

		MvcResult result = mockMvc.perform(post("/api/connections").contentType(MediaType.APPLICATION_JSON).content(connectionJson)).andExpect(status().isCreated()).andReturn();
		ConnectionDTO connectionDTOResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ConnectionDTO.class);

		assertEquals(connectionDTO, connectionDTOResponse);
	}

	@Test
	public void createConnection_withoutAuthentication_returnsFailure() throws Exception {
		mockMvc.perform(post("/api/connections").contentType(MediaType.APPLICATION_JSON).content(connectionJson)).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser
	public void updateConnection_returnsSuccessAndUpdatedConnection() throws Exception {
		when(modelMapperMock.map(any(ConnectionDTO.class), eq(Connection.class))).thenReturn(connectionMock);
		when(connectionServiceMock.updateConnection(connectionMock, 1L)).thenReturn(connectionMock);
		when(modelMapperMock.map(connectionMock, ConnectionDTO.class)).thenReturn(connectionDTO);

		MvcResult result = mockMvc.perform(put("/api/connections/1").contentType(MediaType.APPLICATION_JSON).content(connectionJson)).andExpect(status().isOk()).andReturn();
		ConnectionDTO connectionDTOResponse = objectMapper.readValue(result.getResponse().getContentAsString(), ConnectionDTO.class);

		assertEquals(connectionDTO, connectionDTOResponse);
	}

	@Test
	public void updateConnection_withoutAuthentication_returnsFailure() throws Exception {
		mockMvc.perform(put("/api/connections/1").contentType(MediaType.APPLICATION_JSON).content(connectionJson)).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser
	public void deleteConnection_returnsSuccess() throws Exception {
		mockMvc.perform(delete("/api/connections/1")).andExpect(status().isOk());
	}

	@Test
	public void deleteConnection_withoutAuthentication_returnsFailure() throws Exception {
		mockMvc.perform(delete("/api/connections/1")).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser
	public void deleteConnectionByConnectedUserId_returnsSuccess() throws Exception {
		mockMvc.perform(delete("/api/connections?connectedUserId=1")).andExpect(status().isOk());
	}

	@Test
	public void deleteConnectionByConnectedUserId_withoutAuthentication_returnsFailure() throws Exception {
		mockMvc.perform(delete("/api/connections?connectedUserId=1")).andExpect(status().isUnauthorized());
	}
}
