package com.example.groupbuying.domain.groupbuy.repository;

import com.example.groupbuying.domain.groupbuy.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
