package com.main.videosapi.repository;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.main.videosapi.entity.Category;
import com.main.videosapi.entity.SubCategory;
import com.main.videosapi.entity.Teaser;

@Repository
public interface TeaserRepository extends JpaRepository<Teaser, Serializable> {
	public Page<Teaser> findByCategory(Category category, Pageable pageable);

	public Page<Teaser> findBySubCategory(SubCategory subcategory, Pageable pageable);
}
