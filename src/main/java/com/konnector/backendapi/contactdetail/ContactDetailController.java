package com.konnector.backendapi.contactdetail;

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

	@PostMapping("/api/contactDetails")
	@ResponseStatus(HttpStatus.CREATED)
	public ContactDetailDTO createContactDetail(@RequestBody ContactDetailDTO contactDetailDTO) {
		ContactDetail contactDetail = modelMapper.map(contactDetailDTO, ContactDetail.class);

		contactDetail = contactDetailService.createContactDetail(contactDetail);

		return modelMapper.map(contactDetail, ContactDetailDTO.class);
	}

	@PutMapping("/api/contactDetails/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ContactDetailDTO updateContactDetail(@RequestBody ContactDetailDTO contactDetailDTO, @PathVariable("id") Long contactDetailId) {
		ContactDetail contactDetail = modelMapper.map(contactDetailDTO, ContactDetail.class);

		contactDetail = contactDetailService.updateContactDetail(contactDetail, contactDetailId);

		return modelMapper.map(contactDetail, ContactDetailDTO.class);
	}

	@GetMapping(value = "/api/contactDetails", params = { "userId", "pageNumber", "pageSize"})
	@ResponseStatus(HttpStatus.OK)
	public List<ContactDetailDTO> getContactDetails(@RequestParam("userId") Long userId, @RequestParam("pageNumber") Integer pageNumber, @RequestParam("pageSize") Integer pageSize) {
		List<ContactDetail> contactDetails = contactDetailService.getContactDetails(userId, pageNumber, pageSize);

		return modelMapper.map(contactDetails, new TypeToken<List<ContactDetailDTO>>() {}.getType());
	}

	@DeleteMapping("/api/contactDetails/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteContactDetail(@PathVariable("id") Long id) {
		contactDetailService.deleteContactDetail(id);
	}
}
