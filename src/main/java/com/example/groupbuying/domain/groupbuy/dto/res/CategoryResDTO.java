package com.example.groupbuying.domain.groupbuy.dto.res;

import lombok.Builder;

public class CategoryResDTO {

    @Builder
    public record CategoryDTO(
            Long categoryId,
            String categoryName
    ) {}
}