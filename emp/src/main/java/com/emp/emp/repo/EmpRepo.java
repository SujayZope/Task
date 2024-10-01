package com.emp.emp.repo;


import java.util.List;

import com.emp.emp.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpRepo extends JpaRepository<Employee,Integer> {
	List<Employee> findByStatus(String status);
}
