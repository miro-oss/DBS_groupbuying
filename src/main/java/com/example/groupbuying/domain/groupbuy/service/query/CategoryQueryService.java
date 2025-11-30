package com.example.groupbuying.domain.groupbuy.service.query;

import com.example.groupbuying.domain.groupbuy.dto.res.CategoryResDTO;

import java.util.List;

public interface CategoryQueryService {

    List<CategoryResDTO.CategoryDTO> getCategories();
}