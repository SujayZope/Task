package com.emp.emp.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emp.emp.entity.employe;

public interface EmpRepo extends JpaRepository<employe,Integer> {
}
