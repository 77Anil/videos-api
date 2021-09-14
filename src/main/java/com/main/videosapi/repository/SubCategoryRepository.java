package com.main.videosapi.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.main.videosapi.entity.Category;
import com.main.videosapi.entity.SubCategory;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Integer> {
	public Page<SubCategory> findByCategory(Category category, Pageable pageable);
}
