package com.konnector.backendapi.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConnectionController {

	private static final Logger LOGGER = LogManager.getLogger(ConnectionController.class);

	@Autowired
	private ConnectionService connectionService;
	@Autowired
	private ModelMapper modelMapper;

	@PostMapping("/api/connections")
	@ResponseStatus(HttpStatus.CREATED)
	public ConnectionDTO createConnection(@RequestBody ConnectionDTO connectionDTO) {
		Connection connection = modelMapper.map(connectionDTO, Connection.class);

		connection = connectionService.createConnection(connection);

		return modelMapper.map(connection, ConnectionDTO.class);
	}

	@PutMapping("/api/connections/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ConnectionDTO updateConnection(@RequestBody ConnectionDTO connectionDTO, @PathVariable("id") Long connectionId) {
		Connection connection = modelMapper.map(connectionDTO, Connection.class);

		connection = connectionService.updateConnection(connection, connectionId);

		return modelMapper.map(connection, ConnectionDTO.class);
	}

	@DeleteMapping("/api/connections/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteConnection(@PathVariable("id") Long id) {
		connectionService.deleteConnection(id);
	}

	@DeleteMapping(value = "/api/connections", params = {"connected-user-id"})
	@ResponseStatus(HttpStatus.OK)
	public void deleteConnectionByConnectedUserId(@RequestParam("connected-user-id") Long connectedUserId) {
		connectionService.deleteConnectionByConnectedUserId(connectedUserId);
	}
}
