package com.konnector.backendapi.connection;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConnectionControllerTest {

	@InjectMocks
	private ConnectionController connectionController = new ConnectionController();

	@Mock
	private ConnectionService connectionServiceMock;
	@Mock
	private ModelMapper modelMapperMock;
	@Mock
	private Connection connectionMock;
	@Mock
	private HttpServletResponse httpServletResponseMock;

	private final EasyRandom easyRandom = new EasyRandom();
	private final ConnectionDTO connectionDTO = easyRandom.nextObject(ConnectionDTO.class);

	@Test
	public void createConnection_returnsSuccessAndCreatedConnection() {
		when(modelMapperMock.map(any(ConnectionDTO.class), eq(Connection.class))).thenReturn(connectionMock);
		when(connectionServiceMock.createConnection(connectionMock)).thenReturn(connectionMock);
		when(modelMapperMock.map(connectionMock, ConnectionDTO.class)).thenReturn(connectionDTO);

		ConnectionDTO result = connectionController.createConnection(connectionDTO);

		assertEquals(connectionDTO, result);
	}

	@Test
	public void updateConnection_returnsSuccessAndUpdatedConnection() {
		when(modelMapperMock.map(any(ConnectionDTO.class), eq(Connection.class))).thenReturn(connectionMock);
		when(connectionServiceMock.updateConnection(connectionMock, 1L)).thenReturn(connectionMock);
		when(modelMapperMock.map(connectionMock, ConnectionDTO.class)).thenReturn(connectionDTO);

		ConnectionDTO result = connectionController.updateConnection(connectionDTO, 1L);

		assertEquals(connectionDTO, result);
	}

	@Test
	public void deleteConnection_returnsSuccessAndDeletesConnection() {
		connectionController.deleteConnection(1L);

		verify(connectionServiceMock).deleteConnection(1L);
	}
}
