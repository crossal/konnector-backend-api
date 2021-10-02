package com.konnector.backendapi.contactdetail;

import com.konnector.backendapi.authentication.AuthenticationFacade;
import com.konnector.backendapi.data.Dao;
import com.konnector.backendapi.exceptions.NotFoundException;
import com.konnector.backendapi.user.User;
import com.konnector.backendapi.user.UserAuthorizationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ContactDetailServiceImpl implements ContactDetailService {

	@Autowired
	private Dao<ContactDetail> contactDetailDao;
	@Autowired
	private Dao<User> userDao;
	@Autowired
	private ContactDetailValidator contactDetailValidator;
	@Autowired
	private UserAuthorizationValidator userAuthorizationValidator;
	@Autowired
	private AuthenticationFacade authenticationFacade;
	@Autowired
	private ContactDetailRepository contactDetailRepository;

	public ContactDetailServiceImpl(Dao<ContactDetail> contactDetailDao, Dao<User> userDao,
	                                ContactDetailValidator contactDetailValidator,
	                                UserAuthorizationValidator userAuthorizationValidator,
	                                AuthenticationFacade authenticationFacade,
	                                ContactDetailRepository contactDetailRepository) {
		this.contactDetailDao = contactDetailDao;
		this.userDao = userDao;
		this.contactDetailValidator = contactDetailValidator;
		this.userAuthorizationValidator = userAuthorizationValidator;
		this.authenticationFacade = authenticationFacade;
		this.contactDetailRepository = contactDetailRepository;
	}

	@Override
	@Transactional
	public ContactDetail createContactDetail(ContactDetail contactDetail) {
		contactDetailValidator.validateContactDetailCreationArgument(contactDetail);

		Authentication authentication = authenticationFacade.getAuthentication();
		userAuthorizationValidator.validateUserRequest(contactDetail.getUserId(), authentication);

		contactDetailDao.save(contactDetail);

		return contactDetail;
	}

	@Override
	@Transactional
	public ContactDetail updateContactDetail(ContactDetail contactDetail, Long contactDetailId) {
		Optional<ContactDetail> optionalContactDetail = contactDetailDao.get(contactDetailId);

		return optionalContactDetail.map(
				existingContactDetail -> {
					contactDetailValidator.validateContactDetailUpdateArgument(existingContactDetail, contactDetail, contactDetailId);

					Authentication authentication = authenticationFacade.getAuthentication();
					userAuthorizationValidator.validateUserRequest(contactDetail.getUserId(), authentication);

					existingContactDetail.merge(contactDetail);

					contactDetailDao.update(existingContactDetail);

					return existingContactDetail;
				}
		).orElseThrow(() -> new NotFoundException("Contact detail not found."));
	}

	@Override
	@Transactional
	public List<ContactDetail> getContactDetails(Long userId, Integer pageNumber, Integer pageSize) {
		contactDetailValidator.validateContactDetailsFetchRequest(userId, pageNumber, pageSize);

		Authentication authentication = authenticationFacade.getAuthentication();
		userAuthorizationValidator.validateUserRequest(userId, authentication);

		Pageable sortedByNameAscPageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by("name").ascending());
		Page page = contactDetailRepository.findByUserId(userId, sortedByNameAscPageable);

		return page.getContent();
	}

	@Override
	@Transactional
	public void deleteContactDetail(Long id) {
		Optional<ContactDetail> optionalContactDetail = contactDetailDao.get(id);

		optionalContactDetail.ifPresentOrElse(
				existingContactDetail -> {
					User user = userDao.get(existingContactDetail.getUserId()).get();

					Authentication authentication = authenticationFacade.getAuthentication();
					userAuthorizationValidator.validateUserRequest(user.getId(), authentication);

					contactDetailDao.delete(existingContactDetail);
				},
				() -> {
					throw new NotFoundException("Contact detail not found.");
				}
		);
	}
}
