package com.main.videosapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.main.videosapi.entity.Partner;

public interface PartnerRepository extends JpaRepository<Partner, Integer> {

	Optional<Partner> findByUsername(String username);
}
