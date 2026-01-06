package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entities.ApiKey;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
  
    void deleteByUserId(Long userId);
    
    List<ApiKey> findAllByUserId(Long userId);
    
    List<ApiKey> findByUserIdAndActiveTrue(Long userId);

	boolean existsByUserIdAndActiveTrue(Long userId);
    

}
