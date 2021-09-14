package com.main.videosapi.repository;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.main.videosapi.entity.Category;
import com.main.videosapi.entity.Media;
import com.main.videosapi.entity.SubCategory;

@Repository
public interface MediaRepository extends PagingAndSortingRepository<Media, Serializable> {
	public Page<Media> findByCategory(Category category, Pageable pageable);

	public Page<Media> findBySubcategory(SubCategory subcategory, Pageable pageable);

}
