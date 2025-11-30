package com.example.groupbuying.domain.groupbuy.service.query;

import com.example.groupbuying.domain.groupbuy.converter.CategoryConverter;
import com.example.groupbuying.domain.groupbuy.dto.res.CategoryResDTO;
import com.example.groupbuying.domain.groupbuy.entity.Category;
import com.example.groupbuying.domain.groupbuy.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryQueryServiceImpl implements CategoryQueryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryResDTO.CategoryDTO> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryConverter::toCategoryDTO)
                .toList();
    }
}