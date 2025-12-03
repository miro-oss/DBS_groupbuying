package com.example.groupbuying.domain.groupbuy.repository;

import com.example.groupbuying.domain.groupbuy.entity.Form;
import com.example.groupbuying.domain.groupbuy.enums.FormStatus;
import com.example.groupbuying.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface FormRepository extends JpaRepository<Form, Long> {
    List<Form> findBySeller(User seller);

    List<Form> findByStatus(FormStatus status);

    List<Form> findByCategory_Id(Long categoryId);

    List<Form> findByTitleContaining(String keyword);

    List<Form> findByStatusAndDeadlineBefore(FormStatus status, LocalDateTime now);
}