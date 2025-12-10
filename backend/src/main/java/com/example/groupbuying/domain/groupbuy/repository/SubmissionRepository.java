package com.example.groupbuying.domain.groupbuy.repository;

import com.example.groupbuying.domain.groupbuy.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    Optional<Submission> findByFormIdAndBuyerId(Long formId, Long buyerId);

    List<Submission> findByBuyerIdOrderByCreatedAtDesc(Long buyerId);

    List<Submission> findByFormIdOrderByCreatedAtAsc(Long formId);

    List<Submission> findByIdInAndFormId(List<Long> ids, Long formId);
}