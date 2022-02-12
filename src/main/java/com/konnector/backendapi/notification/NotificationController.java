package com.konnector.backendapi.notification;

import com.konnector.backendapi.http.Headers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class NotificationController {

	private static final Logger LOGGER = LogManager.getLogger(NotificationController.class);

	@Autowired
	private NotificationService notificationService;
	@Autowired
	private ModelMapper modelMapper;

	@GetMapping(value = "/api/notifications", params = { "userId", "pageNumber", "pageSize"})
	@ResponseStatus(HttpStatus.OK)
	public List<NotificationDTO> getNotifications(@RequestParam("userId") Long userId, @RequestParam("pageNumber") Integer pageNumber, @RequestParam("pageSize") Integer pageSize,
	                                                HttpServletResponse response) {
		List<Notification> notifications = notificationService.getNotifications(userId, pageNumber, pageSize);

		long totalNotificationsCount = notificationService.getNotificationsCount(userId);
		response.setHeader(Headers.HEADER_TOTAL_COUNT, String.valueOf(totalNotificationsCount));

		return modelMapper.map(notifications, new TypeToken<List<NotificationDTO>>() {}.getType());
	}

	@DeleteMapping("/api/notifications/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteNotification(@PathVariable("id") Long id) {
		notificationService.deleteNotification(id);
	}

}
