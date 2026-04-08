package com.SpringBootWebTutorial.Module2.WebTutorial.repositories;

import com.SpringBootWebTutorial.Module2.WebTutorial.entities.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepo extends JpaRepository<EmployeeEntity, Long> {

}
