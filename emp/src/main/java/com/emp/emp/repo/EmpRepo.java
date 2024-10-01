package com.emp.emp.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emp.emp.entity.employe;

public interface EmpRepo extends JpaRepository<employe,Integer> {
	List<employe> findByApproved(String status);
}
