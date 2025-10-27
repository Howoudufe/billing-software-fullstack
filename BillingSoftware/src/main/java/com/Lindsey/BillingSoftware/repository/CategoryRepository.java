package com.Lindsey.BillingSoftware.repository;

import com.Lindsey.BillingSoftware.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*
* 一个 JPA（Java Persistence API）仓库接口，直接联系数据库
* <CategoryEntity, Long>：操作的实体类是 CategoryEntity，主键（@Id）的类型是 Long
* 继承后自动拥有 save(), findAll(), findById(), delete(), existsById() 等常用数据库方法
* */
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    // 自定义方法，Spring Data JPA 能自动根据方法名生成 SQL 查询，此处为 findBy + 字段名
    // Optional<CategoryEntity>：一种安全容器，可以避免空指针异常
    Optional<CategoryEntity> findByCategoryID(String categoryID);
}
