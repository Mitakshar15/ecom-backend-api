package com.ainkai.repository;

import com.ainkai.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepo extends JpaRepository<Category,Long> {

public Category findByName(String name);

@Query("SELECT c FROM Category c where c.name=:name And c.parentCategory.name=:parentCategoryName")
public  Category findByNameAndParent(@Param("name") String name,@Param("parentCategoryName")String parentCategoryName);

}
