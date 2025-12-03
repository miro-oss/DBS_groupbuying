package com.example.groupbuying.domain.groupbuy.controller;

import com.example.groupbuying.domain.groupbuy.dto.res.CategoryResDTO;
import com.example.groupbuying.domain.groupbuy.service.query.CategoryQueryService;
import com.example.groupbuying.global.apiPayload.ApiResponse;
import com.example.groupbuying.global.apiPayload.code.GeneralSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryQueryService categoryQueryService;

    @GetMapping
    public ApiResponse<List<CategoryResDTO.CategoryDTO>> getCategories() {
        var result = categoryQueryService.getCategories();
        return ApiResponse.onSuccess(GeneralSuccessCode.OK, result);
    }
}