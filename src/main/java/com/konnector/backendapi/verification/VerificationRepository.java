package com.konnector.backendapi.verification;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface VerificationRepository extends CrudRepository<Verification, Integer> {
	Optional<Verification> findFirstByUserIdAndTypeOrderByCreatedOnDesc(Long userId, VerificationType type);
}
