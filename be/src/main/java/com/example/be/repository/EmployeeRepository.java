package com.example.be.repository;

import com.example.be.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    List<Employee> findByFirstNameContainingOrLastNameContaining(String keyword1, String keyword2);
}
