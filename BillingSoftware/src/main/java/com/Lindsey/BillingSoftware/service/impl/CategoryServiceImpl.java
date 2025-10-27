package com.Lindsey.BillingSoftware.service.impl;

import com.Lindsey.BillingSoftware.entity.CategoryEntity;
import com.Lindsey.BillingSoftware.io.CategoryRequest;
import com.Lindsey.BillingSoftware.io.CategoryResponse;
import com.Lindsey.BillingSoftware.repository.CategoryRepository;
import com.Lindsey.BillingSoftware.service.CategoryService;
import com.Lindsey.BillingSoftware.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/*
* 实现添加、获取、删除 Category 等业务逻辑。
* */

@Service
@RequiredArgsConstructor  //来自Lombok，自动为所有 final 字段生成构造函数
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final FileUploadService fileUploadService;

    /*
    * add 方法：
    * 调用 FileUploadService 上传图片到 S3；把返回的图片 URL 设置到 Category 实体；保存到数据库；
    * 最后返回封装好的 CategoryResponse
    * */
    @Override
    public CategoryResponse add(CategoryRequest request, MultipartFile file) {
        String imgUrl = fileUploadService.uploadFile(file);
        // 接收从 Controller 层传来的 CategoryRequest 数据，把请求对象转换成数据库实体对象
        CategoryEntity newCategory = convertToEntity(request);
        newCategory.setImgUrl(imgUrl);
        // 保存到数据库，返回一个包含数据库生成字段（e.g. 自增 id、createdAt、updatedAt）的新对象
        newCategory = categoryRepository.save(newCategory);
        return convertToResponse(newCategory); // 保存后的实体转换成用于返回的响应对象
    }

    @Override
    public List<CategoryResponse> read() {
        return categoryRepository.findAll() // 从数据库中查出所有的 CategoryEntity 记录
                .stream() // 把这个 List<CategoryEntity> 转换成 Java Stream 流
                .map(categoryEntity -> convertToResponse(categoryEntity)) // 对流中的每个 CategoryEntity 元素进行映射
                .collect(Collectors.toList()); // 把转换后的 Stream 元素重新收集成一个 List<CategoryResponse>
    }

    @Override
    public void delete(String categoryID) {
        CategoryEntity existingCategory  = categoryRepository.findByCategoryID(categoryID)
                .orElseThrow(() -> new RuntimeException("Category not found: " + categoryID));
        fileUploadService.deleteFile(existingCategory.getImgUrl());
        categoryRepository.delete(existingCategory);
    }

    private CategoryResponse convertToResponse(CategoryEntity newCategory) {
        // Lombok 的 @Builder 自动生成构建器方法
        return CategoryResponse.builder()
                .categoryID(newCategory.getCategoryID())
                .name(newCategory.getName())
                .description(newCategory.getDescription())
                .bgColor(newCategory.getBgColor())
                .imgUrl(newCategory.getImgUrl())
                .createdAt(newCategory.getCreatedAt())
                .updatedAt(newCategory.getUpdatedAt())
                .build();
    }

    private CategoryEntity convertToEntity(CategoryRequest request) {
        return CategoryEntity.builder()
                .categoryID(UUID.randomUUID().toString()) // 生成唯一的字符串ID，用作业务层categoryID
                .name(request.getName())
                .description(request.getDescription())
                .bgColor(request.getBgColor())
                .build();
    }
}
