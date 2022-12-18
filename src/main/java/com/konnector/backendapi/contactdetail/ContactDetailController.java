package com.konnector.backendapi.contactdetail;

import com.konnector.backendapi.http.Headers;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ContactDetailController {

	private static final Logger LOGGER = LogManager.getLogger(ContactDetailController.class);

	@Autowired
	private ContactDetailService contactDetailService;
	@Autowired
	private ModelMapper modelMapper;

	@PostMapping("/api/contact-details")
	@ResponseStatus(HttpStatus.CREATED)
	public ContactDetailDTO createContactDetail(@RequestBody ContactDetailDTO contactDetailDTO) {
		ContactDetail contactDetail = modelMapper.map(contactDetailDTO, ContactDetail.class);

		contactDetail = contactDetailService.createContactDetail(contactDetail);

		return modelMapper.map(contactDetail, ContactDetailDTO.class);
	}

	@PutMapping("/api/contact-details/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ContactDetailDTO updateContactDetail(@RequestBody ContactDetailDTO contactDetailDTO, @PathVariable("id") Long contactDetailId) {
		ContactDetail contactDetail = modelMapper.map(contactDetailDTO, ContactDetail.class);

		contactDetail = contactDetailService.updateContactDetail(contactDetail, contactDetailId);

		return modelMapper.map(contactDetail, ContactDetailDTO.class);
	}

	@GetMapping(value = "/api/contact-details", params = { "user-id", "page-number", "page-size"})
	@ResponseStatus(HttpStatus.OK)
	public List<ContactDetailDTO> getContactDetails(@RequestParam("user-id") Long userId, @RequestParam("page-number") Integer pageNumber,
	                                                @RequestParam("page-size") Integer pageSize,
	                                                HttpServletResponse response) {
		List<ContactDetail> contactDetails = contactDetailService.getContactDetails(userId, pageNumber, pageSize);

		long totalContactDetailsCount = contactDetailService.getContactDetailsCount(userId);
		response.setHeader(Headers.HEADER_TOTAL_COUNT, String.valueOf(totalContactDetailsCount));

		return modelMapper.map(contactDetails, new TypeToken<List<ContactDetailDTO>>() {}.getType());
	}

	@DeleteMapping("/api/contact-details/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteContactDetail(@PathVariable("id") Long id) {
		contactDetailService.deleteContactDetail(id);
	}
}
