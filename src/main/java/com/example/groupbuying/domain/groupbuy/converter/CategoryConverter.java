// CategoryConverter.java
package com.example.groupbuying.domain.groupbuy.converter;

import com.example.groupbuying.domain.groupbuy.dto.res.CategoryResDTO;
import com.example.groupbuying.domain.groupbuy.entity.Category;

public class CategoryConverter {

    public static CategoryResDTO.CategoryDTO toCategoryDTO(Category category) {
        return CategoryResDTO.CategoryDTO.builder()
                .categoryId(category.getId())
                .categoryName(category.getCategoryName())
                .build();
    }
}