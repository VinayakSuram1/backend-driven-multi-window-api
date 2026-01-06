package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entities.WindowData;
import com.example.demo.entities.enums.WindowType;

public interface WindowDataRepository extends JpaRepository<WindowData, Long>{
	
	List<WindowData> findByUserIdAndWindowType(Long userId, WindowType windowType);
	
	

}
