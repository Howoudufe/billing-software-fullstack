package com.Lindsey.BillingSoftware.service;

import com.Lindsey.BillingSoftware.io.CategoryRequest;
import com.Lindsey.BillingSoftware.io.CategoryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {
    CategoryResponse add(CategoryRequest request, MultipartFile file);
    List<CategoryResponse> read();
    void delete(String categoryID);
}
