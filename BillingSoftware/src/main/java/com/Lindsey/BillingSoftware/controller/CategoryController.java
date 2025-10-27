package com.Lindsey.BillingSoftware.controller;

import com.Lindsey.BillingSoftware.io.CategoryRequest;
import com.Lindsey.BillingSoftware.io.CategoryResponse;
import com.Lindsey.BillingSoftware.service.CategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/*
* 定义 RESTful API 端点，接收前端传来的 JSON 等数据，
* 调用 CategoryService 服务层的方法操作数据到数据库，然后返回 response 给前端
* */

@RestController // Web 控制器类，表明它的所有方法都直接返回 JSON
@RequestMapping("/categories") // 当前控制器的 基础 URL 路径前缀，当前类下的所有方法，访问路径都以 /categories 开头
@RequiredArgsConstructor
public class CategoryController {

    // CategoryService 是接口，实际注入的实现类是 CategoryServiceImpl，由 Spring Boot 的 IoC 容器负责自动装配
    private final CategoryService categoryService;

    @PostMapping // 向服务器提交（新增）数据
    @ResponseStatus(HttpStatus.CREATED) //指定返回的 HTTP 状态码为 201 Created
    // @RequestPart：用于接收 multipart/form-data 类型的请求中的不同部分（适用于文件上传 + JSON的场景）
    public CategoryResponse addCategory(@RequestPart("category") String categoryString,
                                        @RequestPart("file")MultipartFile file){
        ObjectMapper objectMapper = new ObjectMapper();
        CategoryRequest request = null;
        try{
            // 使用 Jackson 的 ObjectMapper 来把 JSON 字符串解析成一个 Java 对象 CategoryRequest
            request = objectMapper.readValue(categoryString, CategoryRequest.class);
            return categoryService.add(request, file); // 把 CategoryRequest 和文件对象一起传给Service层的add方法
        }catch (JsonProcessingException ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Exception occurred while parsing the json: " + ex.getMessage());
        }
    }

    @GetMapping // 获取（读取）服务器上的数据
    public List<CategoryResponse> fetchCategories(){
        return categoryService.read();
    }

    @DeleteMapping("/{categoryID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // @PathVariable：把 URL 路径里的 {categoryID} 提取出来传给方法参数
    public void remove(@PathVariable String categoryID){
        try {
            categoryService.delete(categoryID);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
