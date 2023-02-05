package com.konnector.backendapi.contactdetail;

import com.konnector.backendapi.authentication.AuthenticationFacade;
import com.konnector.backendapi.connection.Connection;
import com.konnector.backendapi.connection.ConnectionRepository;
import com.konnector.backendapi.connection.ConnectionStatus;
import com.konnector.backendapi.exceptions.NotFoundException;
import com.konnector.backendapi.exceptions.UnauthorizedException;
import com.konnector.backendapi.security.AuthenticationUtil;
import com.konnector.backendapi.user.UserAuthorizationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ContactDetailServiceImpl implements ContactDetailService {

	@Autowired
	private ContactDetailValidator contactDetailValidator;
	@Autowired
	private UserAuthorizationValidator userAuthorizationValidator;
	@Autowired
	private AuthenticationFacade authenticationFacade;
	@Autowired
	private ContactDetailRepository contactDetailRepository;
	@Autowired
	private ConnectionRepository connectionRepository;

	public ContactDetailServiceImpl(ContactDetailValidator contactDetailValidator,
	                                UserAuthorizationValidator userAuthorizationValidator,
	                                AuthenticationFacade authenticationFacade,
	                                ContactDetailRepository contactDetailRepository,
	                                ConnectionRepository connectionRepository) {
		this.contactDetailValidator = contactDetailValidator;
		this.userAuthorizationValidator = userAuthorizationValidator;
		this.authenticationFacade = authenticationFacade;
		this.contactDetailRepository = contactDetailRepository;
		this.connectionRepository = connectionRepository;
	}

	@Override
	@Transactional
	public ContactDetail createContactDetail(ContactDetail contactDetail) {
		contactDetailValidator.validateContactDetailCreationArgument(contactDetail);

		Authentication authentication = authenticationFacade.getAuthentication();
		userAuthorizationValidator.validateUserRequest(contactDetail.getUserId(), authentication);

		contactDetailRepository.save(contactDetail);

		return contactDetail;
	}

	@Override
	@Transactional
	public ContactDetail updateContactDetail(ContactDetail contactDetail, Long contactDetailId) {
		Optional<ContactDetail> optionalContactDetail = contactDetailRepository.findById(contactDetailId);

		return optionalContactDetail.map(
				existingContactDetail -> {
					contactDetailValidator.validateContactDetailUpdateArgument(existingContactDetail, contactDetail, contactDetailId);

					Authentication authentication = authenticationFacade.getAuthentication();
					userAuthorizationValidator.validateUserRequest(contactDetail.getUserId(), authentication);

					existingContactDetail.merge(contactDetail);

					contactDetailRepository.save(existingContactDetail);

					return existingContactDetail;
				}
		).orElseThrow(() -> new NotFoundException("Contact detail not found."));
	}

	@Override
	@Transactional
	public List<ContactDetail> getContactDetails(Long userId, Integer pageNumber, Integer pageSize) {
		contactDetailValidator.validateContactDetailsFetchRequest(userId, pageNumber, pageSize);

		Authentication authentication = authenticationFacade.getAuthentication();
		Long loggedInUserId = AuthenticationUtil.getUserId(authentication);
		if (!userId.equals(loggedInUserId)) {
			Collection<Connection> connections = connectionRepository.findConnectionBetweenUsersWithStatus(userId, loggedInUserId, ConnectionStatus.ACCEPTED);
			if (connections.isEmpty()) {
				throw new UnauthorizedException();
			}
		}

		Pageable sortedByTypeAscPageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by("type").ascending());
		Page<ContactDetail> page = contactDetailRepository.findByUserId(userId, sortedByTypeAscPageable);

		return page.getContent();
	}

	@Override
	@Transactional
	public long getContactDetailsCount(Long userId) {
		contactDetailValidator.validateContactDetailsCountFetchRequest(userId);

		Authentication authentication = authenticationFacade.getAuthentication();
		Long loggedInUserId = AuthenticationUtil.getUserId(authentication);
		if (!userId.equals(loggedInUserId)) {
			Collection<Connection> connections = connectionRepository.findConnectionBetweenUsersWithStatus(userId, loggedInUserId, ConnectionStatus.ACCEPTED);
			if (connections.isEmpty()) {
				throw new UnauthorizedException();
			}
		}

		return contactDetailRepository.countByUserId(userId);
	}

	@Override
	@Transactional
	public void deleteContactDetail(Long id) {
		Optional<ContactDetail> optionalContactDetail = contactDetailRepository.findById(id);

		optionalContactDetail.ifPresentOrElse(
				existingContactDetail -> {
					Authentication authentication = authenticationFacade.getAuthentication();
					userAuthorizationValidator.validateUserRequest(existingContactDetail.getUserId(), authentication);

					contactDetailRepository.delete(existingContactDetail);
				},
				() -> {
					throw new NotFoundException("Contact detail not found.");
				}
		);
	}
}
