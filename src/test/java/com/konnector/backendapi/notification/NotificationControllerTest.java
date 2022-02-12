package com.konnector.backendapi.notification;

import com.konnector.backendapi.contactdetail.ContactDetailDTO;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.Type;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificationControllerTest {

	@InjectMocks
	private NotificationController notificationController = new NotificationController();

	@Mock
	private NotificationService notificationServiceMock;
	@Mock
	private ModelMapper modelMapperMock;
	@Mock
	private Notification notificationMock;
	@Mock
	private HttpServletResponse httpServletResponseMock;

	private final EasyRandom easyRandom = new EasyRandom();
	private final NotificationDTO notificationDTO = easyRandom.nextObject(NotificationDTO.class);

	@Test
	public void getNotifications_returnsSuccessAndNotifications() {
		List<Notification> notifications = List.of(notificationMock);
		List<NotificationDTO> notificationDTOS = List.of(notificationDTO);
		when(notificationServiceMock.getNotifications(1L, 1, 1)).thenReturn(notifications);
		Type listType = new TypeToken<List<NotificationDTO>>() {}.getType();
		when(modelMapperMock.map(notifications, listType)).thenReturn(notificationDTOS);

		List<NotificationDTO> result = notificationController.getNotifications(1L, 1, 1, httpServletResponseMock);

		assertEquals(notificationDTOS, result);
	}

	@Test
	public void deleteNotification_returnsSuccessAndDeletesNotification() {
		notificationController.deleteNotification(1L);

		verify(notificationServiceMock).deleteNotification(1L);
	}
}
